package com.url.shortner.controller;

import com.url.shortner.Exception.CustomAliasAlreadyExistsException;
import com.url.shortner.dtos.ClickEventDTO;
import com.url.shortner.dtos.CustomUrl;
import com.url.shortner.dtos.UrlMappingDTO;
import com.url.shortner.models.User;
import com.url.shortner.service.urlService.UrlMappingService;
import com.url.shortner.service.userService.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
@Tag(name = "URl Mapping", description = "Url Mapping related APIs")
public class UrlMappingController {

    private UrlMappingService urlMappingService;
    private UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a short url")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request,
            Principal principal) {
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());

        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all urls for a user")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDTO> urls = urlMappingService.getUrlByUser(user);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get click analytics for a short url")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<ClickEventDTO> clickEventDTOS = urlMappingService.getCLickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventDTOS);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get total clicks for a user")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        User user = userService.findByUsername(principal.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, start, end);
        return ResponseEntity.ok(totalClicks);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete a short url")
    public ResponseEntity<Void> deleteUrl(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        urlMappingService.deleteUrl(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shorten/custom")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a short URL with a custom alias")
    public ResponseEntity<?> createCustomShortUrl(
            @Valid @RequestBody CustomUrl request,
            Principal principal) {

        String originalUrl = request.getOriginalUrl();
        String customAlias = request.getCustomUrl();
        if (!isValidCustomAlias(customAlias)) {
            return ResponseEntity.badRequest().body(
                    "Custom url must be 1-20 characters and can only contain letters, numbers");
        }

        User user = userService.findByUsername(principal.getName());

        try {
            UrlMappingDTO urlMappingDTO = urlMappingService.createCustomShortUrl(
                    originalUrl,
                    user,
                    customAlias);
            return ResponseEntity.ok(urlMappingDTO);
        } catch (CustomAliasAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Custom alias is already in use");
        }
    }

    private boolean isValidCustomAlias(String alias) {
        return alias.matches("^[a-zA-Z0-9_-]{1,20}$");
    }
}
