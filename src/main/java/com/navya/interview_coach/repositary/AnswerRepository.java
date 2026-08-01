package com.navya.interview_coach.repositary;

import com.navya.interview_coach.entity.Answer;
import com.navya.interview_coach.entity.PrepSession;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    long countBySession(PrepSession session);
}