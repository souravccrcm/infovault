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


}
