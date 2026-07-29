package com.navya.interview_coach.repositary;

import com.navya.interview_coach.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}