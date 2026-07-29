package com.navya.interview_coach.controller;

import com.navya.interview_coach.entity.PrepSession;
import com.navya.interview_coach.entity.Question;
import com.navya.interview_coach.repositary.PrepSessionRepository;
import com.navya.interview_coach.repositary.QuestionRepository;
//import com.navya.interview_coach.repository.PrepSessionRepository;
//import com.navya.interview_coach.repository.QuestionRepository;
import com.navya.interview_coach.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private PrepSessionRepository prepSessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @GetMapping("/{sessionId}")
    public List<Question> getMatchingQuestions(@PathVariable Integer sessionId) {
        PrepSession session = prepSessionRepository.findById(sessionId).orElseThrow();
        String jdVectorString = embeddingService.toVectorString(session.getJdEmbedding());
        return questionRepository.findClosestQuestions(jdVectorString, 5);
    }
}
