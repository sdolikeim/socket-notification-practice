package com.example.socketnotification.controller;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.socketnotification.dto.AnswerRequest;
import com.example.socketnotification.dto.ArticleRequest;
import com.example.socketnotification.dto.ArticleResponse;
import com.example.socketnotification.service.ArticleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

    private final ArticleService articleService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ArticleResponse createArticle(@RequestBody ArticleRequest request) {
        ArticleResponse article = articleService.createArticle(request);

        messagingTemplate.convertAndSend("/topic/admin",article);

        return article;
    }

    @GetMapping
    public List<ArticleResponse> getArticles() {
        return articleService.getArticles();
    }

    @PutMapping("/answer")
    public ArticleResponse answerArticle(@RequestBody AnswerRequest request) {
        ArticleResponse article = articleService.answerArticle(request);

        messagingTemplate.convertAndSend("/topic/user",article);

        return article;
    }
    
}
