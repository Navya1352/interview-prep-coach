package com.navya.interview_coach.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false, length = 100)
    private String topic;

    @Column(nullable = false, length = 20)
    private String difficulty;

    @Column(name = "ideal_answer_notes", columnDefinition = "TEXT")
    private String idealAnswerNotes;

    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1024)
    private float[] embedding;

    public Question() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getIdealAnswerNotes() { return idealAnswerNotes; }
    public void setIdealAnswerNotes(String idealAnswerNotes) { this.idealAnswerNotes = idealAnswerNotes; }

    public float[] getEmbedding() { return embedding; }
    public void setEmbedding(float[] embedding) { this.embedding = embedding; }
}