package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.service.GttsSer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/audio")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class AudioCon {
    @Autowired
    private GttsSer ser;

    @GetMapping("/speak")
    public ResponseEntity<byte[]> speed(@RequestParam String text) {
        if (text == null || text.trim().isEmpty()) return ResponseEntity.badRequest().build();
        byte[] audio = ser.synthesizeKoreanSpeech(text);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .body(audio);
    }
}
