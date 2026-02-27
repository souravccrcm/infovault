package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.entity.ArticleImage;
import com.ccrcm.infovault.entity.ArticleVideo;
import com.ccrcm.infovault.repository.ArticleImageRepository;
import com.ccrcm.infovault.repository.ArticleVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final ArticleImageRepository articleImageRepository;
    private final ArticleVideoRepository articleVideoRepository;
}
