package com.example.socketnotification.repository;

import com.example.socketnotification.entity.SocketArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SocketArticleRepository extends JpaRepository<SocketArticle, Long> {

    @Query("SELECT a FROM SocketArticle a ORDER BY a.articleId DESC")
    List<SocketArticle> findAllOrderByArticleIdDesc();
}