package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.EditCharReq;
import com.atbm.projecttlkrbe.dto.request.UploadFileCharacterReq;
import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.model.CharacterType;
import com.atbm.projecttlkrbe.repository.CharacterRep;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCharacterSer {
    private final CharacterSer characterSer;
    private final CharacterRep characterRep;
    private final Path storageDirVowels = Paths.get("static/assets/svg/vowels");
    private final Path storageDirConsonants = Paths.get("static/assets/svg/consonants");

    // Upload File Character
    public ResponseEntity<String> uploadFile(UploadFileCharacterReq req, MultipartFile newFile) {
        if (newFile.isEmpty()) return ResponseEntity.badRequest().body("File không được rỗng.");

        Path targetDir;
        if (req.isVowels()) targetDir = storageDirVowels;
        else targetDir = storageDirConsonants;

        try {
            // Thu muc ton tai?
            if (!Files.exists(targetDir)) return ResponseEntity.badRequest().body("Thư mục không tồn tại.");
            Path targetFilePath = targetDir.resolve(req.getTargetFileName()).normalize();
            Files.copy(newFile.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Cập nhật và ghi đè ảnh thành công");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi trong quá trình ghi đè file: " + e.getMessage());
        }
    }

    // Edit Character
    public boolean editChar(EditCharReq req) {
        long charId = req.getCharId();
        String name = req.getName();
        String transcription = req.getTranscription();
        Boolean isDouble = req.getIsDouble();
        CharacterType type = req.getType();
        Integer strokeCount = req.getStrokeCount();

        CharacterEntity character = characterRep.findById(charId).orElseThrow(() -> new RuntimeException("Character not found: " + charId));

        if (name != null && !name.trim().isEmpty()) character.setName(req.getName());
        if (transcription != null && !transcription.trim().isEmpty()) character.setTranscription(transcription);
        if (isDouble != null && isDouble != character.isDouble()) character.setDouble(isDouble);
        if (type != null) character.setType(type);
        if (strokeCount != null && strokeCount > 0) character.setStrokeCount(strokeCount);

        characterRep.save(character);
        return true;
    }

    // Search Characters
    public List<CharacterEntity> searchCharacters(String keyword) {
        return characterRep.searchCharacters(keyword);
    }

    // Get All Characters
    public List<CharacterEntity> getAllCharacters() {
        return characterSer.getAllCharacters();
    }
}
