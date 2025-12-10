from typing import TYPE_CHECKING
from sqlmodel import Field, SQLModel, Relationship  # ty:ignore[unresolved-import]

if TYPE_CHECKING:
    from .drop_party import Drop_Party


class Drop(SQLModel, table=True):
    id: int | None = Field(default=None, primary_key=True)
    item_id: int
    drop_party_id: int = Field(foreign_key="drop_party.id")
    item_name: str
    quantity: int
    value: int  # Value in GP

    drop_party: "Drop_Party" = Relationship(back_populates="drops")
