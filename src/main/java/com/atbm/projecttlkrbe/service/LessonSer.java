package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.LessonListRes;
import com.atbm.projecttlkrbe.dto.response.VideoByLessonIdRes;
import com.atbm.projecttlkrbe.model.Lesson;
import com.atbm.projecttlkrbe.repository.LessonRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonSer {
    private final LessonRep rep;

    // Get List Lesson
    public List<LessonListRes> getLessonList() {
        List<Lesson> lessonList = rep.findAll();
        List<LessonListRes> lessonListRes = new ArrayList<>();
        for (Lesson lesson : lessonList) {
            LessonListRes res = new LessonListRes();
            res.setId(lesson.getId());
            res.setTitle(lesson.getTitle());
            res.setDes(lesson.getDescription());
            lessonListRes.add(res);
        }
        return lessonListRes;
    }

    // Get Lesson By Id
    public VideoByLessonIdRes getLessonById(long lessonId) {
        Lesson l = rep.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found: " + lessonId));
        VideoByLessonIdRes res = new VideoByLessonIdRes();
        res.setId(l.getId());
        res.setTitle(l.getTitle());
        res.setYoutubeId(l.getYoutubeId());
        return res;
    }

    // Get All Lesson
    public List<Lesson> getAllLessons(){
        return rep.findAll();
    }
}
