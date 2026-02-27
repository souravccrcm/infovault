package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.response.ArticleMediaResponse;
import com.ccrcm.infovault.dto.response.MediaResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.entity.ArticleImage;
import com.ccrcm.infovault.entity.ArticleVideo;
import com.ccrcm.infovault.repository.ArticleImageRepository;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.repository.ArticleVideoRepository;
import com.ccrcm.infovault.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {


    private final ArticleImageRepository imageRepository;
    private final ArticleVideoRepository videoRepository;
    private final ArticleRepository articleRepository;

    @Override
    public ArticleMediaResponse getMediaByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        ArticleMediaResponse response = new ArticleMediaResponse();
        response.setArticleId(articleId);

        // Images
        List<MediaResponse> imageList = article.getImages()
                .stream()
                .filter(ArticleImage::getActive)
                .map(image -> {
                    MediaResponse media = new MediaResponse();
                    media.setId(image.getId());
                    media.setFileName(image.getFileName());
                    media.setFileSize(image.getFileSize());

                    // 🔐 Secure file endpoint
                    media.setUrl("/api/files/image/" + image.getId());

                    return media;
                })
                .toList();

        // Videos
        List<MediaResponse> videoList = article.getVideos()
                .stream()
                .filter(ArticleVideo::getActive)
                .map(video -> {
                    MediaResponse media = new MediaResponse();
                    media.setId(video.getId());
                    media.setFileName(video.getFileName());
                    media.setFileSize(video.getFileSize());

                    // 🔐 Secure streaming endpoint
                    media.setUrl("/api/files/video/" + video.getId());

                    return media;
                })
                .toList();

        response.setImages(imageList);
        response.setVideos(videoList);

        return response;
    }
}
