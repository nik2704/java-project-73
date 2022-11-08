package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public final class WelcomeController {
    @Autowired
    private final Rollbar rollbar;
    @Operation(summary = "Welcome page")
    @GetMapping("/welcome")
    public String root() {
        rollbar.debug("Welcome to Spring -> Rollbar");
        return "Welcome to Spring";
    }
}
