package rip.jade.partytimeserverjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response returned after processing a drop request")
public class DropResponse {
    @Schema(description = "Status of the drop operation",
            example = "party_created",
            allowableValues = {"party_created", "drop_added", "duplicate"})
    private String status;

    @Schema(description = "OSRS world number", example = "302")
    private int world;

    @Schema(description = "ID of the drop party (null if duplicate)", example = "123", nullable = true)
    private Long dropPartyId;

    @Schema(description = "Human-readable message describing the result",
            example = "New drop party created for world 302")
    private String message;
}