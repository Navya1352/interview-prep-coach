package com.navya.interview_coach.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final RestClient restClient = RestClient.create("https://api.groq.com");

    public String evaluateAnswer(String questionText, String idealAnswerNotes, String userAnswer) {
        String prompt = "Question: " + questionText + "\n" +
                "Ideal answer should cover: " + idealAnswerNotes + "\n" +
                "Candidate's answer: " + userAnswer + "\n\n" +
                "Evaluate the candidate's answer in 2-3 sentences. Then on a new line, output exactly one of: DECISION: HARDER_SAME_TOPIC, DECISION: NEW_TOPIC, or DECISION: FLAG_WEAK_AREA.";

        Map<String, Object> requestBody = Map.of(
            "model", "llama-3.3-70b-versatile",
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            )
        );

        Map<String, Object> response = restClient.post()
            .uri("/openai/v1/chat/completions")
            .header("Authorization", "Bearer " + groqApiKey)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        return (String) message.get("content");
    }
}