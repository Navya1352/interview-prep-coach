package com.navya.interview_coach.repositary;

import com.navya.interview_coach.entity.WeakArea;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeakAreaRepository extends JpaRepository<WeakArea, Integer> {
    Optional<WeakArea> findByTopic(String topic);
}
