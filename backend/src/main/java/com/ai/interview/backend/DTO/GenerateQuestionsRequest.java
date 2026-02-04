package com.ai.interview.backend.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuestionsRequest {
    private String role;
    private String difficulty;
    private int count;
}
