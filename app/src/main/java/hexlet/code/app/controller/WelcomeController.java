package hexlet.code.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class WelcomeController {
    @Operation(summary = "Welcome page")
    @GetMapping("/welcome")
    public String root() {
        return "Welcome to Spring";
    }
}
