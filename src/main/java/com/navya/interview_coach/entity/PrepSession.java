package com.navya.interview_coach.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "prep_sessions")
public class PrepSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_description", nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "jd_embedding")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1024)
    private float[] jdEmbedding;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false, length = 20)
    private String status = "active";

    @ManyToOne
@JoinColumn(name = "current_question_id")
private Question currentQuestion;

    @PrePersist
    public void prePersist() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }

    public PrepSession() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public float[] getJdEmbedding() { return jdEmbedding; }
    public void setJdEmbedding(float[] jdEmbedding) { this.jdEmbedding = jdEmbedding; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Question getCurrentQuestion() { return currentQuestion; }
    public void setCurrentQuestion(Question currentQuestion) { this.currentQuestion = currentQuestion; }


}
