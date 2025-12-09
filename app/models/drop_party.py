from sqlmodel import Field, SQLModel
from drop import Drop


class drop_party(SQLModel, table =True): 
    id: int | None = Field(default=None, primary_key=True)
    world: int
    avg_drop: int
    drops: list[Drop]
    lastDrop: datetime