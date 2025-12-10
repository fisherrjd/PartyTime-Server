from db.database import create_db_and_tables
from api.routes.drops import app

# Create database tables on startup
create_db_and_tables()

# The FastAPI app is now imported from api/routes/drops.py
# Run with: uvicorn main:app --reload
