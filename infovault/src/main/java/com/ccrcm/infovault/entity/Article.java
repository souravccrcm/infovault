package com.ccrcm.infovault.entity;

import com.ccrcm.infovault.converter.ArticleStatusConverter;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_type_id", nullable = false)
    private UpdateType updateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_type_id", nullable = false)
    private ClinicalType clinicalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "impact_level_id", nullable = false)
    private ImpactLevel impactLevel;

    @Column(name = "article_content", columnDefinition = "TEXT")
    private String articleContent;

    @Convert(converter = ArticleStatusConverter.class)
    @Column(nullable = false)
    private ArticleStatus status;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploaded_by", nullable = false)
    private Long uploadedBy;

    @Column(nullable = false)
    private Boolean active = true;
}
