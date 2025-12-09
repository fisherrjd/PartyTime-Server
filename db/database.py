from sqlmodel import create_engine, SQLModel, Session  # ty:ignore[unresolved-import]

# Import ALL your models here
from models.drop_party import Drop_Party

DATABASE_URL = "sqlite:///database.db"
engine = create_engine(DATABASE_URL)


def create_db_and_tables():
    SQLModel.metadata.create_all(engine)


def get_session():
    return Session(engine)
