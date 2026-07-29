package com.navya.interview_coach.repositary;

import com.navya.interview_coach.entity.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM questions ORDER BY embedding <=> CAST(:jdEmbedding AS vector) LIMIT :limit", nativeQuery = true)
    List<Question> findClosestQuestions(@Param("jdEmbedding") String jdEmbedding, @Param("limit") int limit);

}
