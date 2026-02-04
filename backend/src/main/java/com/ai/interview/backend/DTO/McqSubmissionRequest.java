package com.ai.interview.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class McqSubmissionRequest {
    /**
     * Index-based answers submitted by the user.
     * 0–3 represent selected option index.
     * -1 means no option was selected for that question.
     */

    // This must be in sync with List<McqQuestionDto> length, both length should be same
    private List<Integer> listOfAnswers;

}
