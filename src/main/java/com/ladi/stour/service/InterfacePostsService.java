package com.ladi.stour.service;

import com.ladi.stour.dto.PostsCreateRequest;
import com.ladi.stour.dto.PostsUpdateRequest;
import com.ladi.stour.entity.PostsEntity;

import java.util.List;

public interface InterfacePostsService {
    PostsEntity createDefault(PostsCreateRequest req);
    PostsEntity createTranslation(String originPostId, PostsCreateRequest req);
    PostsEntity update(String id, PostsUpdateRequest req);
    PostsEntity publish(String id);
    void delete(String id);
    PostsEntity getBySlug(String slug, String locale);
    List<PostsEntity> getTranslations(String translationGroupId);
}
