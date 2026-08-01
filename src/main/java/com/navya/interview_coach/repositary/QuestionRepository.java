package com.navya.interview_coach.repositary;

import com.navya.interview_coach.entity.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM questions ORDER BY embedding <=> CAST(:jdEmbedding AS vector) LIMIT :limit", nativeQuery = true)
    List<Question> findClosestQuestions(@Param("jdEmbedding") String jdEmbedding, @Param("limit") int limit);
    @Query(value = "SELECT * FROM questions WHERE topic != :excludeTopic ORDER BY embedding <=> CAST(:jdEmbedding AS vector) LIMIT :limit", nativeQuery = true)
    List<Question> findClosestQuestionsExcludingTopic(@Param("jdEmbedding") String jdEmbedding, @Param("excludeTopic") String excludeTopic, @Param("limit") int limit);  
     List<Question> findByTopic(String topic);
     @Query(value = "SELECT * FROM questions WHERE id != :excludeId ORDER BY embedding <=> (SELECT embedding FROM questions WHERE id = :excludeId) LIMIT :limit", nativeQuery = true)
List<Question> findSimilarQuestions(@Param("excludeId") Integer excludeId, @Param("limit") int limit);

}
