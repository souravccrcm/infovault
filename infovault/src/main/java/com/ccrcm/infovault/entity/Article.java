package com.ccrcm.infovault.entity;

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

    private String title;
    private String source;
    private String country;
    private String type;
    private String clinicalType;
    private String status;
    private String actions;

    private String filePath;
}