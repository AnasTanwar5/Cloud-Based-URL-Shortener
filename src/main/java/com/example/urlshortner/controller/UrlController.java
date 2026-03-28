package com.example.urlshortner.controller;

import com.example.urlshortner.model.Url;
import com.example.urlshortner.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @GetMapping("/shorten")
    public String shortenUrl(@RequestParam String url) {
        Url saved = service.saveUrl(url);
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/r/")
                .path(saved.getShortCode())
                .toUriString();
    }

    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        Url url = service.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(302)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }
    @GetMapping("/stats/{shortCode}")
    public String getStats(@PathVariable String shortCode) {
        Url url = service.getOriginalUrl(shortCode);
        return "Original URL: " + url.getOriginalUrl() +
                " | Clicks: " + url.getClickCount();
    }

}