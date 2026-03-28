package com.example.urlshortner.service;

import com.example.urlshortner.model.Url;
import com.example.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository repository;

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    private String generateShortCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            shortCode.append(chars.charAt(random.nextInt(chars.length())));
        }

        return shortCode.toString();
    }

    public Url saveUrl(String originalUrl) {
        String shortCode;

        do {
            shortCode = generateShortCode();
        } while (repository.findByShortCode(shortCode).isPresent());

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        return repository.save(url);
    }

    public Url getOriginalUrl(String shortCode) {
        Url url = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        // increase click count
        url.setClickCount(url.getClickCount() + 1);
        repository.save(url);

        return url;
    }
}