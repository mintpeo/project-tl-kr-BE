package com.atbm.projecttlkrbe.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GttsSer {
    private final WebClient webClient;

    public GttsSer() {
        this.webClient = WebClient.builder()
                .baseUrl("https://translate.google.com/translate_tts")
                .defaultHeader(
                        HttpHeaders.USER_AGENT,
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
                                + "AppleWebKit/537.36 "
                                + "(KHTML, like Gecko) "
                                + "Chrome/155.0.0.0 Safari/537.36"
                )
                .build();
    }

    public byte[] synthesizeKoreanSpeech(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text không được để trống");
        }
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("ie", "UTF-8")
                        .queryParam("tl", "ko")
                        .queryParam("client", "tw-ob")
                        .queryParam("q", text.trim())
                        .build()
                )
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}