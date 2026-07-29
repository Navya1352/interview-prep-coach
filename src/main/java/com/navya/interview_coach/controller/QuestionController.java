package com.navya.interview_coach.controller;
import com.navya.interview_coach.entity.Question;
import com.navya.interview_coach.repositary.QuestionRepository;
import com.navya.interview_coach.service.EmbeddingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        float[] embedding = embeddingService.getEmbedding(question.getQuestionText());
        question.setEmbedding(embedding);
        return questionRepository.save(question);
    }
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    @DeleteMapping("/{id}")
public void deleteQuestion(@PathVariable Integer id) {
    questionRepository.deleteById(id);
}

}
