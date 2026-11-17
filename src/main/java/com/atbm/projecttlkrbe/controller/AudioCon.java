package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.service.AudioSer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class AudioCon {
    private final AudioSer ser;

    @PostMapping("/check")
    public ResponseEntity<String> checkPronunciation(
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "kind", required = false) String kind,
            @RequestParam(value = "expected", required = false) String expected
    ) throws IOException {
        return ser.checkPronunciation(audio, kind, expected);
    }
}