package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.CharacterRes;
import com.atbm.projecttlkrbe.dto.response.CharactersRes;
import com.atbm.projecttlkrbe.model.AdminStrokeData;
import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.model.CharacterType;
import com.atbm.projecttlkrbe.repository.AdminStrokeDataRep;
import com.atbm.projecttlkrbe.repository.CharacterRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterSer {
    private final CharacterRep rep;
    private final AdminStrokeDataRep dataRep;

    // Update get all character
    public List<CharacterRes> getAllCharacters() {
        List<CharacterEntity> list = rep.findAll();
        List<CharacterRes> res = new ArrayList<>();
        for (CharacterEntity e : list) {
            CharacterRes c = new CharacterRes();
            c.setId(e.getId());
            c.setDouble(e.isDouble());
            c.setName(e.getName());
            c.setStrokeCount(e.getStrokeCount());
            c.setTranscription(e.getTranscription());
            c.setType(e.getType().toString());

            AdminStrokeData asd = dataRep.findByCharacterIdOrderByIdDesc(e.getId()).orElse(null);
            if (asd != null) {
                c.setImgUrl(asd.getActiveUrl());
                c.setFileName(asd.getActivePublicId());
                c.setStatus(asd.getStatus().toString());
                c.setPendingUrl(asd.getPendingUrl());
                c.setPendingPublicId(asd.getPendingPublicId());
                c.setNote(asd.getNote());
            } else {
                c.setImgUrl(null);
                c.setFileName(null);
                c.setStatus(null);
                c.setPendingUrl(null);
                c.setPendingPublicId(null);
                c.setNote(null);
            }

            res.add(c);
        }
        return res;
    }

    // Get Vowels/Consanants
    public List<CharactersRes> getCharacters(boolean isVowel) {
        List<CharacterEntity> characters = rep.findAll();
        if (isVowel) {
            return characters.stream()
                    .filter(character -> CharacterType.VOWEL.equals(character.getType()))
                    .map(character -> CharactersRes.builder()
                            .id(character.getId())
                            .name(character.getName())
                            .transcription(character.getTranscription())
                            .strokeUrl(character.getStrokeSvgUrl())
                            .build())
                    .toList();
        }

        return characters.stream()
                .filter(character -> CharacterType.CONSONANT.equals(character.getType()))
                .map(character -> CharactersRes.builder()
                        .id(character.getId())
                        .name(character.getName())
                        .transcription(character.getTranscription())
                        .strokeUrl(character.getStrokeSvgUrl())
                        .build())
                .toList();
    }
}
