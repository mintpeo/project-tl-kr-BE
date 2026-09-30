package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.AdminStrokeDataRes;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.AdminStrokeDataRep;
import com.atbm.projecttlkrbe.repository.AdminStrokeOptionRep;
import com.atbm.projecttlkrbe.repository.CharacterRep;
import com.atbm.projecttlkrbe.repository.StrokeOptionDataRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStrokeDataSer {
    private final AdminStrokeDataRep rep;
    private final AdminStrokeOptionRep optionRep;
    private final StrokeOptionDataRep optionDataRep;
    private final CharacterRep characterRep;
    private final CloudinarySer cloudinarySer;

    // Get Admin Stroke Option Data
    public List<StrokeOptionData> getAllOptions() {
        return optionDataRep.findAll();
    }

    // Get all admin stroke data
    public List<AdminStrokeDataRes> getAllStroke() {
        List<AdminStrokeData> list = rep.findAll();
        List<CharacterEntity> characters = characterRep.findAll();
        List<AdminStrokeDataRes> res = new ArrayList<>();
        for (CharacterEntity character : characters) {
            AdminStrokeData a = rep.findByCharacterIdOrderByIdDesc(character.getId()).orElse(null);
            AdminStrokeDataRes r = new AdminStrokeDataRes();
            r.setGlyph(character.getName());
            r.setRomanization(character.getTranscription());
            r.setDeclaredStrokes(character.getStrokeCount());
            if (a != null) {
                // Have Data
                r.setId(a.getId());
                r.setActivePublicId(a.getActivePublicId());
                r.setActiveUrl(a.getActiveUrl());
                r.setNote(a.getNote());
                r.setPendingPublicId(a.getPendingPublicId());
                r.setPendingUrl(a.getPendingUrl());
                r.setStatus(a.getStatus().toString());
                r.setUpdatedAt(a.getUpdatedAt());
            } else {
                r.setStatus(AdminStrokeStatus.MISSING.toString());
            }
            res.add(r);
        }
        return res;
    }

    // Change Status Pending -> Active
    public boolean approveDaft(Long charId) throws IOException {
        AdminStrokeData item = rep.findByCharacterIdOrderByIdDesc(charId).orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getPendingPublicId() == null) throw new RuntimeException("Pending Public Id not found");

        String oldActivePublicId = item.getActivePublicId();
        Map<String, Object> moveResult = cloudinarySer.promoteToActive(item.getPendingPublicId());
        item.setActivePublicId(moveResult.get("public_id").toString());
        item.setActiveUrl(moveResult.get("secure_url").toString());

        // Clear Status
        item.setPendingPublicId(null);
        item.setPendingUrl(null);
        item.setStatus(AdminStrokeStatus.VERIFIED);
        item.setNote(null);
        rep.save(item);

        if (oldActivePublicId != null) {
            cloudinarySer.deleteResource(oldActivePublicId);
        }
        return true;
    }

    // Upload File Pending
    public boolean uploadDraft(Long charId, MultipartFile file) throws IOException {
        CharacterEntity character = characterRep.findById(charId).orElseThrow(() -> new RuntimeException("Character not found"));
        AdminStrokeData item = rep.findByCharacterIdOrderByIdDesc(charId).orElseGet(AdminStrokeData::new);

        Map<String, Object> uploadResult = cloudinarySer.uploadPending(file, character.getTranscription());
        item.setCharacter(character);
        item.setPendingPublicId(uploadResult.get("public_id").toString());
        item.setPendingUrl(uploadResult.get("secure_url").toString());
        item.setStatus(AdminStrokeStatus.PENDING);
        item.setNote(null);
        item.setUpdatedAt(LocalDate.now());
        rep.save(item);
        return true;
    }
}
