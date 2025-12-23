package rip.jade.partytimeserverjava.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import rip.jade.partytimeserverjava.dto.DropRequest;
import rip.jade.partytimeserverjava.dto.DropResponse;
import rip.jade.partytimeserverjava.service.DropService;

@RestController
@RequestMapping("/api/drops")
@RequiredArgsConstructor
@Tag(name = "Drops", description = "API for reporting and managing item drops")
public class DropsController {

    private final DropService dropService;

    @PostMapping()
    @Operation(
        summary = "Report an item drop",
        description = "Reports a high-value item drop (>= 15,000 gp) for a specific world. " +
                      "Creates a new drop party if none exists, adds to an existing active party, " +
                      "or detects duplicate drops within a 500ms window."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Drop processed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DropResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - validation errors (e.g., value < 15,000, negative world, blank item name)",
            content = @Content
        )
    })
    public ResponseEntity<DropResponse> drops(@Valid @RequestBody DropRequest dropRequest) {
        DropResponse response = dropService.handleDrop(dropRequest);
        return ResponseEntity.ok(response);
    }
}
