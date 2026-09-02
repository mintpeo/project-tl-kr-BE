package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.EditCharReq;
import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.model.CharacterType;
import com.atbm.projecttlkrbe.repository.CharacterRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCharacterSer {
    private final CharacterSer characterSer;
    private final CharacterRep characterRep;

    // Edit Character
    public boolean editChar(EditCharReq req) {
        long charId = req.getCharId();
        String name = req.getName();
        String transcription = req.getTranscription();
        Boolean isDouble = req.getIsDouble();
        CharacterType type = req.getType();
        Integer strokeCount = req.getStrokeCount();
        String strokeSvgUrl = req.getStrokeSvgUrl();

        CharacterEntity character = characterRep.findById(charId).orElseThrow(() -> new RuntimeException("Character not found: " + charId));

        if (name != null && !name.trim().isEmpty()) character.setName(req.getName());
        if (transcription != null && !transcription.trim().isEmpty()) character.setTranscription(transcription);
        if (isDouble != null && isDouble != character.isDouble()) character.setDouble(isDouble);
        if (type != null) character.setType(type);
        if (strokeCount != null && strokeCount > 0) character.setStrokeCount(strokeCount);
//        if (strokeSvgUrl != null && !strokeSvgUrl.trim().isEmpty()) character.setStrokeSvgUrl(strokeSvgUrl);

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
