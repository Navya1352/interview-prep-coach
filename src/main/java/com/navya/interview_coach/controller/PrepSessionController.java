package com.navya.interview_coach.controller;

import com.navya.interview_coach.entity.PrepSession;
import com.navya.interview_coach.repositary.PrepSessionRepository;
//import com.navya.interview_coach.repository.PrepSessionRepository;
import com.navya.interview_coach.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class PrepSessionController {

    @Autowired
    private PrepSessionRepository prepSessionRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @PostMapping
    public PrepSession createSession(@RequestBody PrepSession session) {
        float[] embedding = embeddingService.getQueryEmbedding(session.getJobDescription());
        session.setJdEmbedding(embedding);
        return prepSessionRepository.save(session);
    }

    @GetMapping
    public List<PrepSession> getAllSessions() {
        return prepSessionRepository.findAll();
    }

    
}