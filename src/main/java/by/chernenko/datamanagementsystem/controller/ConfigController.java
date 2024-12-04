package by.chernenko.datamanagementsystem.controller;

import by.chernenko.datamanagementsystem.service.CAPublicKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private CAPublicKeyService publicKeyService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(publicKeyService.findAll());
    }
}
