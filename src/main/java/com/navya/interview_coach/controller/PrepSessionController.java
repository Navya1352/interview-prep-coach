package com.navya.interview_coach.controller;

import com.navya.interview_coach.entity.PrepSession;
import com.navya.interview_coach.repositary.PrepSessionRepository;
import com.navya.interview_coach.repositary.QuestionRepository;
//import com.navya.interview_coach.repository.PrepSessionRepository;
import com.navya.interview_coach.service.EmbeddingService;
import java.util.List;
import com.navya.interview_coach.entity.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class PrepSessionController {

    @Autowired
    private PrepSessionRepository prepSessionRepository;

    @Autowired
    private EmbeddingService embeddingService;
    @Autowired
     private QuestionRepository questionRepository;


    @PostMapping
    public PrepSession createSession(@RequestBody PrepSession session) {
        float[] embedding = embeddingService.getQueryEmbedding(session.getJobDescription());
        session.setJdEmbedding(embedding);
        
        PrepSession saved = prepSessionRepository.save(session);

        String jdVectorString = embeddingService.toVectorString(embedding);
    List<Question> matches = questionRepository.findClosestQuestions(jdVectorString, 5);

    if (!matches.isEmpty()) {
        saved.setCurrentQuestion(matches.get(0));
        saved = prepSessionRepository.save(saved);
    }

    return saved;
    }

    @GetMapping
    public List<PrepSession> getAllSessions() {
        return prepSessionRepository.findAll();
    }
    @GetMapping("/{id}")
    public PrepSession getSession(@PathVariable Integer id) {
    return prepSessionRepository.findById(id).orElseThrow();
}

    
}