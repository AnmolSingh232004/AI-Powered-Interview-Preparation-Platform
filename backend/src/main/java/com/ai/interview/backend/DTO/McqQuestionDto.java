package com.ai.interview.backend.DTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class McqQuestionDto {
    private String question;

    // List of options will be always of size = 4
    private List<String> options;

    // Index of correct opiton 0 based
    private int correctOptionIndex;
}
