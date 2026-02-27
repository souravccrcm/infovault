package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.MediaResponse;
import com.ccrcm.infovault.entity.ArticleImage;
import com.ccrcm.infovault.entity.ArticleVideo;
import com.ccrcm.infovault.repository.ArticleImageRepository;
import com.ccrcm.infovault.repository.ArticleVideoRepository;
import com.ccrcm.infovault.service.MediaService;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping("/{articleId}/media")
    public ResponseEntity<List<MediaResponse>> getAllMediaByArticle(
            @PathVariable Long articleId) {

        return ResponseEntity.ok(
                mediaService.getAllMediaByArticle(articleId)
        );
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws IOException {

        Resource resource = mediaService.getImageById(id);

        Path path = resource.getFile().toPath();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        Files.probeContentType(path)))
                .body(resource);
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<Resource> getVideo(@PathVariable Long id) throws IOException {

        Resource resource = mediaService.getVideoById(id);

        Path path = resource.getFile().toPath();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        Files.probeContentType(path)))
                .body(resource);
    }
}


