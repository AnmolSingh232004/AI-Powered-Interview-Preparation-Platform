//package com.ai.interview.backend.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "interview_sessions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class InterviewSession {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // Taken from JWT-authenticated user
//    private Long userId;
//
//    private String role;
//
//    private String difficulty;
//
//    // JSON representation of List<McqQuestionDto>
//    @Column(columnDefinition = "TEXT")
//    private String questionsJson;
//
//    private LocalDateTime createdAt;
//}
