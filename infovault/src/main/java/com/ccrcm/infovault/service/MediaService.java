package com.ccrcm.infovault.service;


import com.ccrcm.infovault.dto.response.MediaResponse;
import org.springframework.core.io.Resource;
import java.net.MalformedURLException;
import java.util.List;

public interface MediaService {

    List<MediaResponse> getAllMediaByArticle(Long articleId);

    Resource getImageById(Long id) throws MalformedURLException;

    Resource getVideoById(Long id) throws MalformedURLException;
}