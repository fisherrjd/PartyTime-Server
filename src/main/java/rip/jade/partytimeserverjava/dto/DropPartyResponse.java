package rip.jade.partytimeserverjava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DropPartyResponse {
    private Long id;
    private Integer world;
    private Integer avgDrop;
    private Boolean isActive;
    private Instant createdAt;
    private Instant lastDropAt;
    private Integer dropCount;
}

