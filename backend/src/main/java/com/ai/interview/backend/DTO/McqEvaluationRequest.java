package com.ai.interview.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class McqEvaluationRequest {
    private String role;
    private String difficulty;
    private List<McqQuestionDto> questions;
    private List<Integer> answers;
}
