package com.example.socketnotification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {
    
    private String memberName;
    private String title;
    private String content;

}
