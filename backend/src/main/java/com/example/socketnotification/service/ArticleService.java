package com.example.socketnotification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.socketnotification.dto.AnswerRequest;
import com.example.socketnotification.dto.ArticleRequest;
import com.example.socketnotification.dto.ArticleResponse;
import com.example.socketnotification.entity.SocketArticle;
import com.example.socketnotification.repository.SocketArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {
    
    private final SocketArticleRepository socketArticleRepository;

    @Transactional
    public ArticleResponse createArticle(ArticleRequest request){
        SocketArticle article = new SocketArticle();

        article.setMemberName(request.getMemberName());
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setStatus("WAITING");

        SocketArticle savedArticle = socketArticleRepository.save(article);

        return new ArticleResponse(savedArticle);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles() {
        return socketArticleRepository.findAllOrderByArticleIdDesc()
        .stream()
        .map(ArticleResponse::new)
        .toList();
    }

    @Transactional
    public ArticleResponse answerArticle(AnswerRequest request){
        SocketArticle article = socketArticleRepository.findById(request.getArticleId())
            .orElseThrow(()->new IllegalArgumentException("존재하지 않는 상담입니다."));

        article.setAnswer(request.getAnswer());
        article.setStatus("ANSWERED");
        article.setAnsweredAt(LocalDateTime.now());

        return new ArticleResponse(article);
    }

}
