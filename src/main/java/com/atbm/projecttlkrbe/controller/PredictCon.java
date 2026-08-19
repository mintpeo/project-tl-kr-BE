package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.DataUrlReq;
import com.atbm.projecttlkrbe.service.PredictSer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/predict")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class PredictCon {
    private final PredictSer ser;

    @PostMapping("/data-url")
    public ResponseEntity<String> predictDataUrl(@RequestBody DataUrlReq req) {
        return ser.predictFromDataUrl(req);
    }

    @PostMapping("/file")
    public ResponseEntity<String> predict(@RequestParam("file") MultipartFile file) throws IOException {
        return ser.predict(file);
    }
}