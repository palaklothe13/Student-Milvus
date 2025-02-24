package com.example.studentmanagment.Student_Managment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class GroqAiHelperService {
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions"; // Ensure this is the correct endpoint
    private static final String API_KEY = "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"; // Your actual API key

    @Autowired
    private RestTemplate restTemplate;
 
    public String getNameMeaning(String name) {
        String prompt = "What is the meaning of the name " + name + "?Pls keep the answer in one line.Also i jsut need the english meaning of the name";
        String requestBody = "{ \"model\": \"llama-3.3-70b-versatile\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}], \"max_tokens\": 500 }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            return extractMeaningFromResponse(response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error fetching meaning: " + e.getMessage());
            return "Meaning not found"; // Return a default value
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return "Error fetching meaning";
        }
    }

    private String extractMeaningFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            System.out.println("LLM RESPONSE:"+jsonResponse);
            return jsonResponse.path("choices").get(0).path("message").path("content").asText("Meaning not found");
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
            return "Error fetching meaning";
        }
    }
}

