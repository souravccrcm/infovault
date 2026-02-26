package com.ccrcm.infovault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article_comments")
@Getter
@Setter
public class ArticleComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ArticleComment parent;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_admin_reply", nullable = false)
    private Boolean isAdminReply = false;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
