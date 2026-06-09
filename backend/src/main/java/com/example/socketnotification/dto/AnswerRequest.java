package com.example.socketnotification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequest {
    
    private Long articleId;
    private String answer;
}
