package com.ai.interview.backend.controller;

import com.ai.interview.backend.DTO.*;
import com.ai.interview.backend.service.InterviewService;
import com.ai.interview.backend.service.ResultCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class InterviewController {
    public final InterviewService interviewService;
    private final ResultCalculationService resultCalculationService;


    // Should return a list of valid questions based on role, difficulty, count
    @PostMapping("/questions")
    public ResponseEntity<List<McqQuestionDto>> generateInterview(@RequestBody GenerateQuestionsRequest request) {
        List<McqQuestionDto> questions = interviewService.generateQuestions(request);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/submit")
    public ResponseEntity<ScoreEvaluationDto> submitInterview(@RequestBody McqEvaluationRequest request) {
        return ResponseEntity.ok(resultCalculationService.evaluate(request));
    }
}
