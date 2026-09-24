package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.CharactersRes;
import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.model.CharacterType;
import com.atbm.projecttlkrbe.repository.CharacterRep;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterSer {
    private final CharacterRep rep;

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

    // Get all character
    public List<CharacterEntity> getAllCharacters() {
        return rep.findAll();
    }
}
