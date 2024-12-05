package by.chernenko.datamanagementsystem.controller;

import by.chernenko.datamanagementsystem.dto.AuthenticationRequest;
import by.chernenko.datamanagementsystem.dto.RegisterRequest;
import by.chernenko.datamanagementsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/sign-up")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> auth(
            @RequestBody AuthenticationRequest request
    ) {
        return service.authenticate(request);
    }
}
