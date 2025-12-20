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
    /*    Report a drop from a client.

    Steps to create a party:
    1. Receive call from client: World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP
    2. Lookup to see if world 444 has an active drop party -> it doesn't
    3. Create a drop party for world 444
    4. Add drop to the drop party
    5. Begin drop party counter

    Steps to add a drop to existing party:
    1. Receive call from client: World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP
    2. Lookup to see if world has an active drop party
    3. Check if the latest drop was within the last 0.5 seconds
    4. Add the drop
    5. Reset drop party timer

    Steps for duplicate drops from multiple clients:
    1. Receive call from client: World: 444 | Item: Rune 2h sword (ID: 1319) x1 | Value: 38052 GP
    2. Lookup to see if world has an active drop party
    3. Check if the latest drop was within the last 0.5 seconds -> it was
    4. Ignore the item as it's PROBABLY a duplicate from another client

    If a drop party has not received a drop in 5 minutes it should end!
*/

    /* Get all Drop parties */

}
