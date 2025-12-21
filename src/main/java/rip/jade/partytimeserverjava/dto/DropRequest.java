package rip.jade.partytimeserverjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DropRequest {
    private int world;
    private int itemId;
    private String itemName;
    private int quantity;
    private int value;
}
