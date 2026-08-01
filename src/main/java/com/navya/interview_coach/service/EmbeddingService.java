package com.navya.interview_coach.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {
    private final Map<String, float[]> documentEmbeddingCache = new java.util.HashMap<>();
private final Map<String, float[]> queryEmbeddingCache = new java.util.HashMap<>();

    @Value("${cohere.api.key}")
    private String cohereApiKey;

    private final RestClient restClient = RestClient.create("https://api.cohere.com");

    public float[] getEmbedding(String text) {
        if (documentEmbeddingCache.containsKey(text)) {
            return documentEmbeddingCache.get(text);
        }
        Map<String, Object> requestBody = Map.of(
            "texts", List.of(text),
            "model", "embed-v4.0",
            "input_type", "search_document",
            "output_dimension", 1024,
            "embedding_types", List.of("float")
        );

        Map<String, Object> response = restClient.post()
            .uri("/v2/embed")
            .header("Authorization", "Bearer " + cohereApiKey)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(Map.class);

        Map<String, Object> embeddings = (Map<String, Object>) response.get("embeddings");
        List<List<Double>> floatEmbeddings = (List<List<Double>>) embeddings.get("float");
        List<Double> firstEmbedding = floatEmbeddings.get(0);

        float[] result = new float[firstEmbedding.size()];
        for (int i = 0; i < firstEmbedding.size(); i++) {
            result[i] = firstEmbedding.get(i).floatValue();
        }
        documentEmbeddingCache.put(text, result);
        return result;
    }
    public float[] getQueryEmbedding(String text) {
        if (queryEmbeddingCache.containsKey(text)) {
            return queryEmbeddingCache.get(text);
        }
        Map<String, Object> requestBody = Map.of(
            "texts", List.of(text),
            "model", "embed-v4.0",
            "input_type", "search_query",
            "output_dimension", 1024,
            "embedding_types", List.of("float")
        );
    
        Map<String, Object> response = restClient.post()
            .uri("/v2/embed")
            .header("Authorization", "Bearer " + cohereApiKey)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(Map.class);
    
        Map<String, Object> embeddings = (Map<String, Object>) response.get("embeddings");
        List<List<Double>> floatEmbeddings = (List<List<Double>>) embeddings.get("float");
        List<Double> firstEmbedding = floatEmbeddings.get(0);
    
        float[] result = new float[firstEmbedding.size()];
        for (int i = 0; i < firstEmbedding.size(); i++) {
            result[i] = firstEmbedding.get(i).floatValue();
        }
        queryEmbeddingCache.put(text, result);
        return result;
    }

    public String toVectorString(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        
        return sb.toString();
    }
}