package rip.jade.partytimeserverjava.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import rip.jade.partytimeserverjava.dto.DropRequest;
import rip.jade.partytimeserverjava.dto.DropResponse;

@RestController
@RequestMapping("/api/drops")
@RequiredArgsConstructor
public class DropsController {

    @PostMapping
    public ResponseEntity<DropResponse> createDrop(DropRequest dropRequest) {

    }
}
