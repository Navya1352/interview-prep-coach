package com.navya.interview_coach.controller;

import com.navya.interview_coach.entity.Answer;
import com.navya.interview_coach.entity.PrepSession;
import com.navya.interview_coach.entity.Question;
import com.navya.interview_coach.entity.WeakArea;
import com.navya.interview_coach.repositary.AnswerRepository;
import com.navya.interview_coach.repositary.PrepSessionRepository;
import com.navya.interview_coach.repositary.QuestionRepository;
import com.navya.interview_coach.repositary.WeakAreaRepository;
import com.navya.interview_coach.service.EmbeddingService;
import com.navya.interview_coach.service.GroqResponse;
import com.navya.interview_coach.service.GroqService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private EmbeddingService embeddingService;
    @Autowired 
    private WeakAreaRepository weakAreaRepository;
    
    @PostMapping
    public Answer submitAnswer(@RequestParam Integer sessionId,
                                
                                @RequestParam String answerText) {

        PrepSession session = prepSessionRepository.findById(sessionId).orElseThrow();
        Question question = session.getCurrentQuestion();

        List<Question> similarQuestions = questionRepository.findSimilarQuestions(question.getId(), 2);

StringBuilder relatedContext = new StringBuilder();
for (Question q : similarQuestions) {
    relatedContext.append("- ").append(q.getQuestionText()).append("\n");
}

        GroqResponse groqResponse = groqService.evaluateAnswer(
            question.getQuestionText(),
            question.getIdealAnswerNotes(),
            answerText,
            relatedContext.toString()
        );
        String fullResponse = groqResponse.getContent();
      String evaluationText=null;
      int scoreIndex = fullResponse.indexOf("SCORE:");
      int evaluationIndex = fullResponse.indexOf("EVALUATION:");

      String decision=null;
      Integer score = null;
      int decisionIndex=fullResponse.indexOf("DECISION:");

       if(scoreIndex != -1 &&decisionIndex!=-1){
        evaluationText=fullResponse.substring(evaluationIndex+"EVALUATION:".length(),scoreIndex).trim();
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
        answer.setPromptTokens(groqResponse.getPromptTokens());
answer.setCompletionTokens(groqResponse.getCompletionTokens());
answer.setTotalTokens(groqResponse.getTotalTokens());
        Question nextQuestion = null;

if (decision != null) {
    if (decision.equals("HARDER_SAME_TOPIC")) {
        List<Question> sameTopic = questionRepository.findByTopic(question.getTopic());
        for (Question q : sameTopic) {
            if (!q.getId().equals(question.getId())) {
                nextQuestion = q;
                break;
            }
        }
    } else {
        String jdVectorString = embeddingService.toVectorString(session.getJdEmbedding());
        List<Question> otherTopics = questionRepository.findClosestQuestionsExcludingTopic(jdVectorString, question.getTopic(), 1);
        if (!otherTopics.isEmpty()) {
            nextQuestion = otherTopics.get(0);
        }
    }
}

if ("FLAG_WEAK_AREA".equals(decision)) {
    Optional<WeakArea> existing = weakAreaRepository.findByTopic(question.getTopic());
    WeakArea weakArea;
    if (existing.isPresent()) {
        weakArea = existing.get();
        weakArea.setTimesFlagged(weakArea.getTimesFlagged() + 1);
    } else {
        weakArea = new WeakArea();
        weakArea.setTopic(question.getTopic());
        weakArea.setTimesFlagged(1);
    }
    weakArea.setLastFlaggedAt(LocalDateTime.now());
    weakAreaRepository.save(weakArea);
}

if (nextQuestion != null) {
    session.setCurrentQuestion(nextQuestion);
    prepSessionRepository.save(session);
}


Answer savedAnswer = answerRepository.save(answer);

long answeredCount = answerRepository.countBySession(session);

if (answeredCount >= 5) {
    session.setStatus("completed");
    session.setCurrentQuestion(null);
    prepSessionRepository.save(session);
}

return savedAnswer;


    }
}
