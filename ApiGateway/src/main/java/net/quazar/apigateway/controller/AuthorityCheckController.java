package net.quazar.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check")
public class AuthorityCheckController {

    @PreAuthorize("hasAuthority(#authority)")
    @GetMapping("/{authority}")
    public ResponseEntity<?> checkAuthority(@PathVariable String authority) {
        return ResponseEntity.ok().build();
    }
}
