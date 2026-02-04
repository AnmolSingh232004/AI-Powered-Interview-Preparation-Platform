package com.ai.interview.backend.service;

import com.ai.interview.backend.DTO.GenerateQuestionsRequest;
import com.ai.interview.backend.DTO.McqQuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final AIService aiService;

    public List<McqQuestionDto> generateQuestions(GenerateQuestionsRequest request) {

        validateRequest(request);

        // 1️⃣ Let AI decide what matters for this role
        List<String> topics = aiService.generateTopicsForRole(request.getRole());

        // 2️⃣ Ask AI to generate MCQs
        List<McqQuestionDto> aiQuestions =
                aiService.generateMcqQuestionsFromAI(
                        request.getRole(),
                        request.getDifficulty(),
                        request.getCount(),
                        topics
                );

        // 3️⃣ Enforce business rule
        return enforceQuestionCount(aiQuestions, request.getCount());
    }

    private List<McqQuestionDto> enforceQuestionCount(
            List<McqQuestionDto> questions,
            int requestedCount
    ) {
        if (questions == null || questions.isEmpty())
            throw new IllegalStateException("AI returned no questions");

        if (questions.size() < requestedCount)
            throw new IllegalStateException(
                    "AI returned fewer questions than requested"
            );

        return questions.size() > requestedCount
                ? questions.subList(0, requestedCount)
                : questions;
    }

    private void validateRequest(GenerateQuestionsRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request cannot be null");

        if (request.getRole() == null || request.getRole().isBlank())
            throw new IllegalArgumentException("Role cannot be empty");

        if (request.getDifficulty() == null || request.getDifficulty().isBlank())
            throw new IllegalArgumentException("Difficulty cannot be empty");

        if (request.getCount() <= 0 || request.getCount() > 20)
            throw new IllegalArgumentException("Count must be 1–20");
    }
}
