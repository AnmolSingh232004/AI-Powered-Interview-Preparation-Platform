package com.ai.interview.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreEvaluationDto {
    private int totalQuestions;
    private int correctAnswers;
    private double scorePercentage;
    private String aiFeedback;

}
