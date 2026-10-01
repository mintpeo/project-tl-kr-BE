package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AdminStrokeOptionDataReq;
import com.atbm.projecttlkrbe.dto.request.FlagDraftReq;
import com.atbm.projecttlkrbe.dto.response.AdminStrokeDataRes;
import com.atbm.projecttlkrbe.dto.response.AdminStrokeOptionDataRes;
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

    // Handle Stroke Option Data
    public boolean handleStrokeOption(List<AdminStrokeOptionDataReq> reqs) {
        AdminStrokeData data = rep.findById(reqs.get(0).getStrokeId()).orElseThrow(() -> new RuntimeException("Stroke Data Not Found:"));
        List<AdminStrokeOption> res = new ArrayList<>();

        for (AdminStrokeOptionDataReq item : reqs) {
            AdminStrokeOption a = new AdminStrokeOption();
            a.setContent(item.getContent());
            a.setAccepted(item.isAccepted());
            a.setAdminStrokeData(data);
            res.add(a);
        }
        optionRep.saveAll(res);
        return true;
    }

    // Get Admin Stroke Option Data
    public List<StrokeOptionData> getAllOptions() {
        return optionDataRep.findAll();
    }

    // Get all admin stroke data
    public List<AdminStrokeDataRes> getAllStroke() {
        List<CharacterEntity> characters = characterRep.findAll();
        List<AdminStrokeDataRes> res = new ArrayList<>();
        for (CharacterEntity character : characters) {
            AdminStrokeData a = rep.findByCharacterIdOrderByIdDesc(character.getId()).orElse(null);

            AdminStrokeDataRes r = new AdminStrokeDataRes();
            r.setCharId(character.getId());
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

                List<AdminStrokeOption> options = optionRep.findTop3ByAdminStrokeDataIdOrderByIdDesc(a.getId());
                List<AdminStrokeOptionDataRes> strokes = new ArrayList<>();
                for (AdminStrokeOption o : options) {
                    AdminStrokeOptionDataRes stroke = new AdminStrokeOptionDataRes();
                    stroke.setContent(o.getContent());
                    stroke.setAccepted(o.isAccepted());
                    strokes.add(stroke);
                }
                r.setOptions(strokes);
            } else {
                r.setStatus(AdminStrokeStatus.MISSING.toString());
            }
            res.add(r);
        }
        return res;
    }

    // Change Status -> Flag
    public boolean flagDraft(FlagDraftReq req) {
        AdminStrokeData data = rep.findById(req.getId()).orElseThrow(() -> new RuntimeException("Stroke not found"));
        // Delete
        if (data.getPendingPublicId() != null) {
            cloudinarySer.deleteResource(data.getPendingPublicId());
            data.setPendingPublicId(null);
            data.setPendingUrl(null);
        }

        data.setNote(req.getNote());
        data.setStatus(AdminStrokeStatus.FLAGGED);
        rep.save(data);
        return true;
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
