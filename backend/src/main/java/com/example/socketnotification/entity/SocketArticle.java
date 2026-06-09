package com.example.socketnotification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="SOCKET_ARTICLE")
@Getter
@Setter
public class SocketArticle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "socket_article_seq")
    @SequenceGenerator(
        name = "socket_article_seq",
        sequenceName = "SOCKET_ARTICLE_SEQ",
        allocationSize = 1
    )
    
    @Column(name = "ARTICLE_ID")
    private Long articleId;

    @Column(name = "MEMBER_NAME", nullable = false)
    private String memberName;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable =  false)
    private String content;
    
    @Column(name = "STATUS", nullable = false)
    private String status = "WAITING";
    
    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "ANSWERED_AT")
    private LocalDateTime answeredAt;

    @PrePersist
    public void PrePersist(){
        this.createdAt = LocalDateTime.now();
        if(this.status == null){
            this.status = "WAITING";
        }
    }
}
