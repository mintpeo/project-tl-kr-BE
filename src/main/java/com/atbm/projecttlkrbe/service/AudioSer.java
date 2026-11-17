package com.atbm.projecttlkrbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AudioSer {
    private final RestTemplate restTemplate;

    @Value("${ai.service.url}")
    private String pythonServiceUrl;

    @Value("${ai.service.key}")
    private String aiServiceKey;

    public ResponseEntity<String> checkPronunciation(MultipartFile audio, String kind, String expected) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-API-Key", aiServiceKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Đóng gói file audio dưới dạng ByteArrayResource kèm filename chuẩn
        body.add("audio", new ByteArrayResource(audio.getBytes()) {
            @Override
            public String getFilename() {
                String originalName = audio.getOriginalFilename();
                return (originalName != null && !originalName.isBlank()) ? originalName : "speech.wav";
            }
        });

        if (kind != null && !kind.isBlank()) body.add("kind", kind);
        if (expected != null && !expected.isBlank()) body.add("expected", expected);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Gọi sang endpoint /speech/check của FastAPI
        return restTemplate.postForEntity(pythonServiceUrl + "/speech/check", requestEntity, String.class);
    }
}
