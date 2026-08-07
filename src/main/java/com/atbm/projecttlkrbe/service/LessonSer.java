package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.model.Lesson;
import com.atbm.projecttlkrbe.repository.LessonRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonSer {
    private final LessonRep rep;

    // Get All Lesson
    public List<Lesson> getAllLessons(){
        return rep.findAll();
    }
}
