package com.navya.interview_coach.repositary;

import com.navya.interview_coach.entity.PrepSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrepSessionRepository extends JpaRepository<PrepSession, Integer> {
}