package rip.jade.partytimeserverjava.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DropRequest {
    @Min(value = 0, message = "World must be non-negative")
    private int world;

    @Min(value = 1, message = "Item ID must be positive")
    private int itemId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Min(value = 15000, message = "Value must be over 15,000 gp")
    private int value;
}
