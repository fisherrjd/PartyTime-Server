package rip.jade.partytimeserverjava.Controller;

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
public class PartyController {

    private final DropService dropService;

    /**
     * Get all active drop parties
     */
    @GetMapping
    public ResponseEntity<List<DropPartyResponse>> parties() {
        List<DropPartyResponse> activeParties = dropService.getAllActiveParties();
        return ResponseEntity.ok(activeParties);
    }

    /**
     * Manually trigger cleanup of inactive drop parties
     * Marks parties as inactive if they haven't received drops in 5 minutes
     */
    @PostMapping("/cleanup")
    public ResponseEntity<Map<String, Object>> cleanUp() {
        int cleanedCount = dropService.cleanupInactiveParties();

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Cleanup completed",
            "partiesCleaned", cleanedCount
        ));
    }
}