package com.ladi.stour.service;

import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.PostsCreateRequest;
import com.ladi.stour.dto.PostsUpdateRequest;
import com.ladi.stour.entity.PostsEntity;

import java.util.List;

public interface InterfacePostsService {
    PostsEntity createDefault(PostsCreateRequest req);
    PostsEntity createTranslation(String originPostId, PostsCreateRequest req);
    PostsEntity update(String id, PostsUpdateRequest req);
    PostsEntity publish(String id);
    MessageResponse delete(String id);
    PostsEntity getById(String id);
    PostsEntity getBySlug(String slug, String locale);
    List<PostsEntity> getTranslations(String translationGroupId);
    List<PostsEntity> getAll();
    List<PostsEntity> getPublished(String locale);
}
