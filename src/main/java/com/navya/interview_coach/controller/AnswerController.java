package com.navya.interview_coach.controller;

import com.navya.interview_coach.entity.Answer;
import com.navya.interview_coach.entity.PrepSession;
import com.navya.interview_coach.entity.Question;
import com.navya.interview_coach.repositary.AnswerRepository;
import com.navya.interview_coach.repositary.PrepSessionRepository;
import com.navya.interview_coach.repositary.QuestionRepository;
import com.navya.interview_coach.service.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private PrepSessionRepository prepSessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private GroqService groqService;

    @PostMapping
    public Answer submitAnswer(@RequestParam Integer sessionId,
                                @RequestParam Integer questionId,
                                @RequestParam String answerText) {

        PrepSession session = prepSessionRepository.findById(sessionId).orElseThrow();
        Question question = questionRepository.findById(questionId).orElseThrow();

        String fullResponse = groqService.evaluateAnswer(
            question.getQuestionText(),
            question.getIdealAnswerNotes(),
            answerText
        );
       
      String evaluationText=null;
      int scoreIndex = fullResponse.indexOf("SCORE:");
      String decision=null;
      Integer score = null;

      int decisionIndex=fullResponse.indexOf("DECISION:");
       if(scoreIndex != -1 &&decisionIndex!=-1){
        evaluationText=fullResponse.substring(0,scoreIndex).trim();
        String scoreText = fullResponse.substring(scoreIndex + "SCORE:".length(), decisionIndex).trim();
        score = Integer.parseInt(scoreText);
        decision=fullResponse.substring(decisionIndex+"DECISION:".length()).trim();
       }

        Answer answer = new Answer();
        answer.setSession(session);
        answer.setQuestion(question);
        answer.setAnswerText(answerText);
        answer.setAiEvaluation(evaluationText);
        answer.setDecision(decision);
        answer.setScore(score);

        return answerRepository.save(answer);
    }
}
