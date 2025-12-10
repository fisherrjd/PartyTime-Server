from datetime import datetime, timezone
from sqlmodel import Field, SQLModel, Relationship  # ty:ignore[unresolved-import]
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from .drop import Drop


class Drop_Party(SQLModel, table=True):
    id: int | None = Field(default=None, primary_key=True)
    world: int
    avg_drop: int
    is_active: bool = Field(default=True)
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))
    last_drop_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))
    drops: list["Drop"] = Relationship(back_populates="drop_party")
