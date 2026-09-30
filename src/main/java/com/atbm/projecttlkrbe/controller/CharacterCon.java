package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.response.CharacterRes;
import com.atbm.projecttlkrbe.dto.response.CharactersRes;
import com.atbm.projecttlkrbe.service.CharacterSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/character")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class CharacterCon {
    private final CharacterSer ser;

    @PostMapping("/filter")
    public List<CharactersRes> getCharacters(@RequestBody Map<String, Boolean> body) {
        boolean isVowel = body.get("isVowel");
        return ser.getCharacters(isVowel);
    }

    @GetMapping("/all")
    public List<CharacterRes> getAllCharacters() {
        return ser.getAllCharacters();
    }
}
