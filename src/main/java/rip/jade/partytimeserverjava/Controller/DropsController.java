package rip.jade.partytimeserverjava.Controller;

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
public class DropsController {

    private final DropService dropService;

    @PostMapping()
    public ResponseEntity<DropResponse> drops(@Valid @RequestBody DropRequest dropRequest) {
        DropResponse response = dropService.handleDrop(dropRequest);
        return ResponseEntity.ok(response);
    }
}
