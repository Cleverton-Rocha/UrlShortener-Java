package com.rockit.urlshortener.controller;

import com.rockit.urlshortener.domain.Url;
import com.rockit.urlshortener.model.ShortenUrlRequest;
import com.rockit.urlshortener.model.ShortenUrlResponse;
import com.rockit.urlshortener.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

  private final UrlRepository urlRepository;

  public UrlController(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @PostMapping("/shorten-url")
  public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request,
      HttpServletRequest servletRequest) {

    String id;
    do {
      id = RandomStringUtils.randomAlphanumeric(5, 10);
    } while (urlRepository.existsById(id));

    urlRepository.save(new Url(id, request.url(), LocalDateTime.now().plusMinutes(1)));

    var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);

    return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
  }

  @GetMapping("{id}")
  public ResponseEntity<Void> redirectUrl(@PathVariable String id) {
    var url = urlRepository.findById(id);

    if (url.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(url.get().getOriginalUrl()));

    return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();

  }
}