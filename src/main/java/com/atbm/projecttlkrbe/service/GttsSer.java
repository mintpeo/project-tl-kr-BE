package com.atbm.projecttlkrbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GttsSer {
    private final WebClient webClient;

    public GttsSer() {
        this.webClient = WebClient.builder()
                .baseUrl("https://translate.google.com/translate_tts")
                .defaultHeader(HttpHeaders.USER_AGENT,
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .build();
    }

    public byte[] synthesizeKoreanSpeech(String text) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("ie", "UTF-8")
                        .queryParam("tl", "ko")
                        .queryParam("client", "tw-ob")
                        .queryParam("q", text)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}