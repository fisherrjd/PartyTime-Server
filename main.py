from db.database import create_db_and_tables, get_session
from sqlmodel import select
from models.drop_party import Drop_Party
from models.drop import Drop
from datetime import datetime, timezone

# Create tables
create_db_and_tables()

with get_session() as session:
    # Create first drop party
    party1 = Drop_Party(world=302, avg_drop=150)

    # Add drops to party1
    party1.drops = [
        Drop(item_id=526, item_name="Dragon bones", quantity=5),
        Drop(item_id=526, item_name="Dragon bones", quantity=3),
        Drop(item_id=1163, item_name="Rune full helm", quantity=1),
        Drop(item_id=1201, item_name="Rune kiteshield", quantity=1),
    ]

    # Create second drop party
    party2 = Drop_Party(world=420, avg_drop=200)

    # Add drops to party2
    party2.drops = [
        Drop(item_id=1050, item_name="Santa hat", quantity=1),
        Drop(item_id=1038, item_name="Red partyhat", quantity=1),
        Drop(item_id=526, item_name="Dragon bones", quantity=10),
    ]

    # Save all to database
    session.add(party1)
    session.add(party2)
    session.commit()
    session.refresh(party1)
    session.refresh(party2)

# Query to verify
with get_session() as session:
    parties = session.exec(select(Drop_Party)).all()
    for party in parties:
        drops_str = ", ".join(
            [f"{drop.item_name} x{drop.quantity}" for drop in party.drops]
        )
        print(
            f"World: {party.world} | {party.avg_drop} gp | {drops_str} | {party.lastDrop}"
        )
