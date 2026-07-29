package com.navya.interview_coach.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weak_areas")
public class WeakArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String topic;

    @Column(name = "times_flagged", nullable = false)
    private Integer timesFlagged = 0;

    @Column(name = "last_flagged_at")
    private LocalDateTime lastFlaggedAt;

    public WeakArea() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public Integer getTimesFlagged() { return timesFlagged; }
    public void setTimesFlagged(Integer timesFlagged) { this.timesFlagged = timesFlagged; }

    public LocalDateTime getLastFlaggedAt() { return lastFlaggedAt; }
    public void setLastFlaggedAt(LocalDateTime lastFlaggedAt) { this.lastFlaggedAt = lastFlaggedAt; }
}