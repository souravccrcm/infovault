package com.ccrcm.infovault.service.impl;

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
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final ArticleImageRepository articleImageRepository;
    private final ArticleVideoRepository articleVideoRepository;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public List<MediaResponse> getAllMediaByArticle(Long articleId) {

        List<MediaResponse> mediaList = new ArrayList<>();

        // Images
        List<ArticleImage> images =
                articleImageRepository.findByArticleIdAndActiveTrue(articleId);

        for (ArticleImage image : images) {
            mediaList.add(new MediaResponse(
                    image.getId(),
                    "IMAGE",
                    image.getFileName(),
                    contextPath + "/files/image/" + image.getId(),
                    image.getFileSize()
            ));
        }

        // Videos
        List<ArticleVideo> videos =
                articleVideoRepository.findByArticleIdAndActiveTrue(articleId);

        for (ArticleVideo video : videos) {
            mediaList.add(new MediaResponse(
                    video.getId(),
                    "VIDEO",
                    video.getFileName(),
                    contextPath + "/files/video/" + video.getId(),
                    video.getFileSize()
            ));
        }

        return mediaList;
    }

    @Override
    public Resource getImageById(Long id) throws MalformedURLException {

        ArticleImage image = articleImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Path path = Paths.get(image.getFilePath());

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found on disk");
        }

        return new UrlResource(path.toUri());
    }

    @Override
    public Resource getVideoById(Long id) throws MalformedURLException {

        ArticleVideo video = articleVideoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        Path path = Paths.get(video.getFilePath());

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found on disk");
        }

        return new UrlResource(path.toUri());
    }
}
