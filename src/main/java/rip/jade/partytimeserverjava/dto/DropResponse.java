package rip.jade.partytimeserverjava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DropResponse {
    private String status;
    private int world;
    private Long dropPartyId;
    private String message;
}