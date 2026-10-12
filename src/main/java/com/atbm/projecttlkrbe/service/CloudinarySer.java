package com.atbm.projecttlkrbe.service;

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

    // Upload character temp
    public Map<String, Object> uploadCharTemp(MultipartFile file, String fileName) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", "temp",
                "public_id", fileName + "_" + System.currentTimeMillis(),
                "resource_type", "image"
        ));
    }

    // Move Pending to Active
    public Map<String, Object> promoteToActive(String pendingPublicId) throws IOException {
        String newPublicId = pendingPublicId.replace("strokes/pending", "strokes/active");
        return cloudinary.uploader().rename(pendingPublicId, newPublicId, ObjectUtils.asMap(
                "overwrite", true,
                "resource_type", "image"
        ));
    }

    // Upload file to pending
    public Map<String, Object> uploadPending(MultipartFile file, String fileName) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", "strokes/pending",
                "public_id", fileName + "_" + System.currentTimeMillis(),
                "resource_type", "image"
        ));
    }

    // Delete File
    public void deleteResource(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException e) {
            // Ghi log lỗi xóa file
            e.printStackTrace();
        }
    }
}