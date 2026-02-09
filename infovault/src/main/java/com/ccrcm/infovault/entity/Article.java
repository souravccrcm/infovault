package com.ccrcm.infovault.entity;

import com.ccrcm.infovault.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "articles")
@Getter
@Setter
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String source;

    @Column(nullable = false)
    private String country;

    @Column(name = "article_type")
    private String articleType;

    @Column(name = "clinical_type")
    private String clinicalType;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status; // DRAFT / PUBLISHED

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploaded_by", nullable = false)
    private Long uploadedBy; // users.id (logical)

    @Column(nullable = false)
    private Boolean active = true;
}
