package com.ai.interview.backend.service;

import com.ai.interview.backend.DTO.McqEvaluationRequest;
import com.ai.interview.backend.DTO.McqQuestionDto;
import com.ai.interview.backend.DTO.ScoreEvaluationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultCalculationService {

    private final AIService aiService;

    public ScoreEvaluationDto evaluate(McqEvaluationRequest request) {

        List<McqQuestionDto> questions = request.getQuestions();
        List<Integer> answers = request.getAnswers();

        if (questions == null || answers == null || questions.size() != answers.size()) {
            throw new IllegalArgumentException("Invalid submission");
        }

        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (answers.get(i) == -1) continue;
            if (questions.get(i).getCorrectOptionIndex() == answers.get(i)) {
                score++;
            }
        }

        int total = questions.size();
        double percentage = (double) score / total * 100;

        // ✅ NOW role is available
        String aiFeedback = aiService.generateFeedbackFromAI(
                request.getRole(),
                request.getDifficulty(),
                questions,
                answers,
                score,
                total
        );

        return new ScoreEvaluationDto(
                total,
                score,
                percentage,
                aiFeedback
        );
    }
}
