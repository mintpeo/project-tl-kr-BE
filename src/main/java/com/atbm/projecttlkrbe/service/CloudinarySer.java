package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.repository.CharacterRep;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinarySer {
    private final Cloudinary cloudinary;
    private final CharacterRep characterRep;

    public String uploadAndOver(MultipartFile file, String fileName, Long charId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", fileName,
                "overwrite", true,
                "unique_filename", false,
                "use_filename", true,
                "invalidate", true,
                "resource_type", "image"
        ));
        String version = uploadResult.get("version").toString();
        CharacterEntity c = characterRep.findById(charId).orElseThrow(() -> new RuntimeException("Character not found: " + charId));
        c.setImageVersion(version);
        characterRep.save(c);

        return uploadResult.get("version").toString();
    }
}