package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.DataUrlReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PredictSer {
    @Autowired
    private RestTemplate restTemplate;

    private static final String PYTHON_SERVICE_URL = "http://localhost:8000/predict";

    public ResponseEntity<String> predictFromDataUrl(DataUrlReq req) {
        String dataUrl = req.getDataUrl();

        // "data:image/png;base64,iVBORw0KGgo..." -> tach lay sau dau phay
        int commaIndex = dataUrl.indexOf(",");
        String base64Data = (commaIndex != -1) ? dataUrl.substring(commaIndex + 1) : dataUrl;

        base64Data = base64Data.replaceAll("\\s+", "");
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        return callPythonService(imageBytes, "drawing.png");
    }

    public ResponseEntity<String> predict(MultipartFile file) throws IOException {
        return callPythonService(file.getBytes(), file.getOriginalFilename());
    }

    private ResponseEntity<String> callPythonService(byte[] imageBytes, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(PYTHON_SERVICE_URL, requestEntity, String.class);
    }
}
