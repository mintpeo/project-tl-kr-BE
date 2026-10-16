package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.AdminTempDataRes;
import com.atbm.projecttlkrbe.dto.response.SampleRes;
import com.atbm.projecttlkrbe.model.AdminStrokeData;
import com.atbm.projecttlkrbe.model.AdminStrokeStatus;
import com.atbm.projecttlkrbe.model.AdminTempData;
import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.repository.AdminTempDataRep;
import com.atbm.projecttlkrbe.repository.CharacterRep;
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
public class AdminTempDataSer {
    private final AdminTempDataRep rep;
    private final CharacterRep characterRep;
    private final CloudinarySer cloudinarySer;

    // Delete Char
    public void deleteCharTemp(Long id) {
        AdminTempData item = rep.findById(id).orElseThrow(() -> new RuntimeException("Temp Data not found"));
        cloudinarySer.deleteResource(item.getPublicId());
        rep.delete(item);
    }

    // Upload Char Temp
    public boolean uploadCharTemp(Long charId, MultipartFile file) throws IOException {
        CharacterEntity character = characterRep.findById(charId).orElseThrow(() -> new RuntimeException("Character not found"));
        List<AdminTempData> adminTempDatas = rep.findByCharacterId(charId);
        if (adminTempDatas.size() >= 3) return false;

        Map<String, Object> uploadResult = cloudinarySer.uploadCharTemp(file, character.getTranscription());
        AdminTempData newTemp = new AdminTempData();
        newTemp.setCharacter(character);
        newTemp.setPublicId(uploadResult.get("public_id").toString());
        newTemp.setImageUrl(uploadResult.get("secure_url").toString());
        newTemp.setUpdatedAt(LocalDate.now());
        rep.save(newTemp);
        return true;
    }

    // Get All Temps
    public List<AdminTempDataRes> getAdminTempData() {
        List<CharacterEntity> characterEntities = characterRep.findAll();
        List<AdminTempData> list = rep.findAll();
        List<AdminTempDataRes> res = new ArrayList<>();
        for (CharacterEntity c : characterEntities) {
            AdminTempDataRes adminTempDataRes = new AdminTempDataRes();
            adminTempDataRes.setId(c.getId());
            adminTempDataRes.setGlyph(c.getName());
            adminTempDataRes.setRomanization(c.getTranscription());
            adminTempDataRes.setCategory(c.getType().toString());
            List<SampleRes> imgs = list.stream()
                    .filter(i -> i.getCharacter().getId().equals(c.getId()))
                    .map(i -> new SampleRes(i.getId(), i.getImageUrl(),
                            i.getUpdatedAt()))
                    .toList();

            adminTempDataRes.setSamples(imgs);
            res.add(adminTempDataRes);
        }
        return res;
    }
}