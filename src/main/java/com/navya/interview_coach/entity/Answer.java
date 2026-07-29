package com.navya.interview_coach.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private PrepSession session;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "ai_evaluation", columnDefinition = "TEXT")
    private String aiEvaluation;

    @Column(length = 30)
    private String decision;

    @Column(name = "answered_at", nullable = false)
    private LocalDateTime answeredAt;

    @PrePersist
    public void prePersist() {
        if (answeredAt == null) {
            answeredAt = LocalDateTime.now();
        }
    }

    public Answer() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public PrepSession getSession() { return session; }
    public void setSession(PrepSession session) { this.session = session; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getAiEvaluation() { return aiEvaluation; }
    public void setAiEvaluation(String aiEvaluation) { this.aiEvaluation = aiEvaluation; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }
}
