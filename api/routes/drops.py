from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sqlmodel import Session, select  # ty:ignore[unresolved-import]
from models.drop import Drop
from models.drop_party import Drop_Party
from db.database import get_session
from datetime import datetime, timezone, timedelta
import asyncio

app = FastAPI()


# Background task for automatic cleanup
async def auto_cleanup_task():
    """Background task that runs every minute to clean up inactive parties."""
    while True:
        try:
            with get_session() as session:
                current_time = datetime.now(timezone.utc)
                timeout_threshold = current_time - timedelta(minutes=5)

                # Find all active parties that haven't received a drop in 5 minutes
                statement = select(Drop_Party).where(
                    Drop_Party.is_active == True,
                    Drop_Party.last_drop_at < timeout_threshold,
                )
                inactive_parties = session.exec(statement).all()

                for party in inactive_parties:
                    party.is_active = False
                    session.add(party)
                    print(
                        f"Auto-ended party {party.id} on world {party.world} after 5 minutes of inactivity"
                    )

                if inactive_parties:
                    session.commit()

        except Exception as e:
            print(f"Error in auto_cleanup_task: {e}")

        # Sleep for 1 minute before checking again
        await asyncio.sleep(60)


@app.on_event("startup")
async def startup_event():
    """Start the background cleanup task when the app starts."""
    asyncio.create_task(auto_cleanup_task())


class DropRequest(BaseModel):
    world: int
    item_id: int
    item_name: str
    quantity: int
    value: int  # Value in GP


@app.post("/drops")
async def report_drop(drop_request: DropRequest):
    """
    Report a drop from a client.

    Steps to create a party:
    1. Receive call from client: World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP
    2. Lookup to see if world 444 has an active drop party -> it doesn't
    3. Create a drop party for world 444
    4. Add drop to the drop party
    5. Begin drop party counter

    Steps to add a drop to existing party:
    1. Receive call from client: World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP
    2. Lookup to see if world has an active drop party
    3. Check if the latest drop was within the last 0.5 seconds
    4. Add the drop
    5. Reset drop party timer

    Steps for duplicate drops from multiple clients:
    1. Receive call from client: World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP
    2. Lookup to see if world has an active drop party
    3. Check if the latest drop was within the last 0.5 seconds -> it was
    4. Ignore the item as it's PROBABLY a duplicate from another client

    If a drop party has not received a drop in 5 minutes it should end!
    """
    with get_session() as session:
        # Step 1: Look up active drop party for this world
        statement = select(Drop_Party).where(
            Drop_Party.world == drop_request.world, Drop_Party.is_active == True
        )
        active_party = session.exec(statement).first()

        current_time = datetime.now(timezone.utc)

        if active_party:
            # Ensure last_drop_at is timezone-aware (SQLite stores it as naive)
            last_drop_at = active_party.last_drop_at
            if last_drop_at.tzinfo is None:
                last_drop_at = last_drop_at.replace(tzinfo=timezone.utc)

            # Check if last drop was within 0.5 seconds (duplicate detection)
            time_since_last_drop = (current_time - last_drop_at).total_seconds()

            if time_since_last_drop < 0.5:
                # Likely a duplicate from another client - ignore it
                return {
                    "status": "ignored",
                    "reason": "duplicate_detected",
                    "message": f"Drop ignored - likely duplicate (last drop was {time_since_last_drop:.3f}s ago)",
                }

            # Add drop to existing party
            new_drop = Drop(
                item_id=drop_request.item_id,
                item_name=drop_request.item_name,
                quantity=drop_request.quantity,
                value=drop_request.value,
                drop_party_id=active_party.id,
            )

            # Update last_drop_at to reset the 5-minute timer
            active_party.last_drop_at = current_time

            # Recalculate average drop value
            total_value = (
                sum(drop.value for drop in active_party.drops) + drop_request.value
            )
            total_drops = len(active_party.drops) + 1
            active_party.avg_drop = total_value // total_drops

            session.add(new_drop)
            session.add(active_party)
            session.commit()
            session.refresh(active_party)

            return {
                "status": "drop_added",
                "party_id": active_party.id,
                "world": active_party.world,
                "total_drops": total_drops,
                "avg_drop_value": active_party.avg_drop,
                "message": f"Drop added to existing party on world {drop_request.world}",
            }
        else:
            # No active party - create a new one
            new_party = Drop_Party(
                world=drop_request.world,
                avg_drop=drop_request.value,
                is_active=True,
                created_at=current_time,
                last_drop_at=current_time,
            )
            session.add(new_party)
            session.commit()
            session.refresh(new_party)

            # Add the first drop
            first_drop = Drop(
                item_id=drop_request.item_id,
                item_name=drop_request.item_name,
                quantity=drop_request.quantity,
                value=drop_request.value,
                drop_party_id=new_party.id,
            )
            session.add(first_drop)
            session.commit()
            session.refresh(new_party)

            return {
                "status": "party_created",
                "party_id": new_party.id,
                "world": new_party.world,
                "total_drops": 1,
                "avg_drop_value": new_party.avg_drop,
                "message": f"New drop party created on world {drop_request.world}",
            }


@app.get("/parties")
async def get_active_parties():
    """Get all active drop parties."""
    with get_session() as session:
        statement = select(Drop_Party).where(Drop_Party.is_active == True)
        active_parties = session.exec(statement).all()

        parties_data = []
        for party in active_parties:
            # Ensure datetime fields are timezone-aware (SQLite stores them as naive)
            last_drop_at = party.last_drop_at
            if last_drop_at.tzinfo is None:
                last_drop_at = last_drop_at.replace(tzinfo=timezone.utc)

            parties_data.append(
                {
                    "id": party.id,
                    "world": party.world,
                    "avg_drop": party.avg_drop,
                    "total_drops": len(party.drops),
                    "created_at": party.created_at,
                    "last_drop_at": party.last_drop_at,
                    "time_since_last_drop": (
                        datetime.now(timezone.utc) - last_drop_at
                    ).total_seconds(),
                }
            )

        return {"active_parties": parties_data}


@app.post("/parties/cleanup")
async def cleanup_inactive_parties():
    """
    End parties that haven't received a drop in 5 minutes.
    This endpoint should be called periodically (e.g., by a cron job or background task).
    """
    with get_session() as session:
        current_time = datetime.now(timezone.utc)
        timeout_threshold = current_time - timedelta(minutes=5)

        # Find all active parties that haven't received a drop in 5 minutes
        statement = select(Drop_Party).where(
            Drop_Party.is_active == True, Drop_Party.last_drop_at < timeout_threshold
        )
        inactive_parties = session.exec(statement).all()

        ended_parties = []
        for party in inactive_parties:
            party.is_active = False
            session.add(party)
            ended_parties.append(
                {
                    "id": party.id,
                    "world": party.world,
                    "total_drops": len(party.drops),
                    "avg_drop": party.avg_drop,
                    "duration_minutes": (
                        party.last_drop_at - party.created_at
                    ).total_seconds()
                    / 60,
                }
            )

        session.commit()

        return {
            "status": "cleanup_complete",
            "parties_ended": len(ended_parties),
            "details": ended_parties,
        }
