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

    public GroqResponse evaluateAnswer(String questionText, String idealAnswerNotes, String userAnswer, String relatedQuestions) {
        String prompt = "You are an interview coach evaluating a candidate's answer.\n\n" +
        "Question: " + questionText + "\n" +
        "Ideal answer should cover: " + idealAnswerNotes + "\n" +
        "Candidate's answer: " + userAnswer + "\n\n" +
        "Related questions the candidate may want to connect this to:\n" + relatedQuestions + "\n" +
        "Respond in EXACTLY this format, with no extra text before or after:\n\n" +
        "EVALUATION: <2-3 sentence evaluation, optionally referencing a related question above if genuinely relevant>\n" +
        "SCORE: <a single integer from 1 to 5>\n" +
        "DECISION: <choose exactly one, using these rules:\n" +
        "  - FLAG_WEAK_AREA: the candidate showed little to no understanding, said 'I don't know', or gave an answer with major gaps (score 1-2)\n" +
        "  - HARDER_SAME_TOPIC: the candidate showed solid understanding but missed some depth or nuance (score 3-4)\n" +
        "  - NEW_TOPIC: the candidate answered very well with minimal gaps (score 5)>\n\n" +
        "Example of correct format:\n" +
        "EVALUATION: The candidate showed no understanding of the core concept and could not answer.\n" +
        "SCORE: 1\n" +
        "DECISION: FLAG_WEAK_AREA";
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
        String content = (String) message.get("content");

        Map<String, Object> usage = (Map<String, Object>) response.get("usage");
        Integer promptTokens = (Integer) usage.get("prompt_tokens");
        Integer completionTokens = (Integer) usage.get("completion_tokens");
        Integer totalTokens = (Integer) usage.get("total_tokens");
    
        return new GroqResponse(content, promptTokens, completionTokens, totalTokens);
    }
}