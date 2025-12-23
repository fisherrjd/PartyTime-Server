package rip.jade.partytimeserverjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request payload for reporting an item drop")
public class DropRequest {
    @Schema(description = "OSRS world number where the drop occurred", example = "302", minimum = "0")
    @Min(value = 0, message = "World must be non-negative")
    private int world;

    @Schema(description = "Unique OSRS item ID", example = "11802", minimum = "1")
    @Min(value = 1, message = "Item ID must be positive")
    private int itemId;

    @Schema(description = "Display name of the dropped item", example = "Armadyl godsword")
    @NotBlank(message = "Item name is required")
    private String itemName;

    @Schema(description = "Quantity of items dropped", example = "1", minimum = "1")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Schema(description = "Total GP value of the drop (must be >= 15,000)", example = "35000000", minimum = "15000")
    @Min(value = 15000, message = "Value must be over 15,000 gp")
    private int value;
}
