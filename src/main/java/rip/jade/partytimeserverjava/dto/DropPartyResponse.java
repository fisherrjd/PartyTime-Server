package rip.jade.partytimeserverjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Details of an active drop party")
public class DropPartyResponse {
    @Schema(description = "Unique identifier of the drop party", example = "123")
    private Long id;

    @Schema(description = "OSRS world number where the party is happening", example = "302")
    private Integer world;

    @Schema(description = "Average GP value of drops in this party", example = "25000000")
    private Integer avgDrop;

    @Schema(description = "Whether the party is currently active", example = "true")
    private Boolean isActive;

    @Schema(description = "Timestamp when the party was created", example = "2025-12-22T18:00:00Z")
    private Instant createdAt;

    @Schema(description = "Timestamp of the most recent drop in this party", example = "2025-12-22T18:05:00Z")
    private Instant lastDropAt;

    @Schema(description = "Total number of drops recorded in this party", example = "5")
    private Integer dropCount;
}

