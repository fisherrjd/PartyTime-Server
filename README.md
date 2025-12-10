# PartyTime Server

A FastAPI-based server for tracking OSRS drop parties in real-time. This server aggregates drop reports from multiple clients and manages active drop parties across different worlds.

## Features

- **Automatic Party Creation**: Creates a drop party when the first drop is detected on a world
- **Duplicate Detection**: Ignores duplicate drop reports within 0.5 seconds (from multiple clients)
- **Smart Party Management**: Automatically ends parties after 5 minutes of inactivity
- **Real-time Tracking**: Tracks average drop values and total drops per party
- **Background Cleanup**: Runs a background task to automatically clean up inactive parties

## Installation

1. Clone the repository
2. Install dependencies using uv:

```bash
uv sync
```

## Running the Server

Start the server using uvicorn:

```bash
uvicorn main:app --reload
```

The server will be available at `http://localhost:8000`

## API Endpoints

### POST /drops

Report a drop from a client.

**Request Body:**
```json
{
  "world": 444,
  "item_id": 1319,
  "item_name": "Rune 2h sword",
  "quantity": 1,
  "value": 38052
}
```

**Response (Party Created):**
```json
{
  "status": "party_created",
  "party_id": 1,
  "world": 444,
  "total_drops": 1,
  "avg_drop_value": 38052,
  "message": "New drop party created on world 444"
}
```

**Response (Drop Added):**
```json
{
  "status": "drop_added",
  "party_id": 1,
  "world": 444,
  "total_drops": 5,
  "avg_drop_value": 25000,
  "message": "Drop added to existing party on world 444"
}
```

**Response (Duplicate Ignored):**
```json
{
  "status": "ignored",
  "reason": "duplicate_detected",
  "message": "Drop ignored - likely duplicate (last drop was 0.234s ago)"
}
```

### GET /parties

Get all active drop parties.

**Response:**
```json
{
  "active_parties": [
    {
      "id": 1,
      "world": 444,
      "avg_drop": 25000,
      "total_drops": 5,
      "created_at": "2025-12-09T12:00:00Z",
      "last_drop_at": "2025-12-09T12:05:00Z",
      "time_since_last_drop": 30.5
    }
  ]
}
```

### POST /parties/cleanup

Manually trigger cleanup of inactive parties (parties with no drops in 5+ minutes).

**Response:**
```json
{
  "status": "cleanup_complete",
  "parties_ended": 2,
  "details": [
    {
      "id": 1,
      "world": 444,
      "total_drops": 10,
      "avg_drop": 30000,
      "duration_minutes": 12.5
    }
  ]
}
```

## How It Works

### Creating a Party
1. Client sends drop report: `World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP`
2. Server checks if world 444 has an active drop party
3. No active party found - creates new drop party for world 444
4. Adds the drop to the party
5. Starts the 5-minute inactivity timer

### Adding to Existing Party
1. Client sends drop report: `World: 444 | Item: Dragon bones (ID: 526) x5 | Value: 15000 GP`
2. Server finds active party on world 444
3. Checks if last drop was within 0.5 seconds (duplicate detection)
4. Last drop was more than 0.5s ago - adds the drop
5. Resets the 5-minute inactivity timer
6. Recalculates average drop value

### Duplicate Detection
1. Client A sends drop report at timestamp `T`
2. Client B sends same drop report at timestamp `T + 0.3s`
3. Server detects last drop was 0.3 seconds ago (< 0.5s threshold)
4. Server ignores the duplicate report from Client B

### Automatic Party Cleanup
- Background task runs every 60 seconds
- Checks all active parties
- Ends any party that hasn't received a drop in 5+ minutes
- Marks party as `is_active = False` in database

## Database Schema

### Drop_Party
- `id`: Primary key
- `world`: World number
- `avg_drop`: Average drop value in GP
- `is_active`: Whether party is currently active
- `created_at`: When the party was created
- `last_drop_at`: When the last drop was added
- `drops`: Relationship to Drop items

### Drop
- `id`: Primary key
- `item_id`: OSRS item ID
- `item_name`: Item name
- `quantity`: Number of items dropped
- `value`: Total value in GP
- `drop_party_id`: Foreign key to Drop_Party
- `drop_party`: Relationship back to parent party

## Development

Run tests:
```bash
pytest
```

Format code:
```bash
black .
ruff check --fix .
```

Type checking:
```bash
ty check
```

## API Documentation

Once the server is running, visit:
- Swagger UI: `http://localhost:8000/docs`
- ReDoc: `http://localhost:8000/redoc`
