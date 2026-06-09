package com.example.socketnotification.dto;

import java.time.LocalDateTime;

import com.example.socketnotification.entity.SocketArticle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponse {
    
    private Long articleId;
    private String memberName;
    private String title;
    private String content;
    private String status;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    public ArticleResponse(SocketArticle article){
        this.articleId = article.getArticleId();
        this.memberName = article.getMemberName();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.status = article.getStatus();
        this.answer = article.getAnswer();
        this.createdAt = article.getCreatedAt();
        this.answeredAt = article.getAnsweredAt();
    }

}
