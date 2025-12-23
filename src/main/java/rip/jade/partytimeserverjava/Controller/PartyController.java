package rip.jade.partytimeserverjava.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rip.jade.partytimeserverjava.dto.DropPartyResponse;
import rip.jade.partytimeserverjava.service.DropService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/party")
@RequiredArgsConstructor
@Tag(name = "Drop Parties", description = "API for querying and managing drop parties")
public class PartyController {

    private final DropService dropService;

    @GetMapping
    @Operation(
        summary = "Get all active drop parties",
        description = "Retrieves a list of all currently active drop parties across all worlds. " +
                      "Each party includes details like world number, average drop value, creation time, " +
                      "last drop time, and total drop count."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved active parties",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = DropPartyResponse.class))
            )
        )
    })
    public ResponseEntity<List<DropPartyResponse>> parties() {
        List<DropPartyResponse> activeParties = dropService.getAllActiveParties();
        return ResponseEntity.ok(activeParties);
    }

    @PostMapping("/cleanup")
    @Operation(
        summary = "Manually trigger party cleanup",
        description = "Manually triggers the cleanup process to mark inactive drop parties as inactive. " +
                      "Parties are considered inactive if they haven't received drops in 5 minutes. " +
                      "This operation runs automatically every 2 minutes in the background."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cleanup completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{\"status\": \"success\", \"message\": \"Cleanup completed\", \"partiesCleaned\": 2}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> cleanUp() {
        int cleanedCount = dropService.cleanupInactiveParties();

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Cleanup completed",
            "partiesCleaned", cleanedCount
        ));
    }
}