from pydantic import BaseModel

class Drop(BaseModel):
    item_name: str
    value: int
