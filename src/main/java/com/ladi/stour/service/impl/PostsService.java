package com.ladi.stour.service.impl;

import com.ladi.stour.dto.PostsCreateRequest;
import com.ladi.stour.dto.PostsUpdateRequest;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.entity.PostsEntity;
import com.ladi.stour.enums.ContentStatus;
import com.ladi.stour.repository.PostsRepository;
import com.ladi.stour.service.InterfacePostsService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService implements InterfacePostsService {
    private final PostsRepository postRepository;


    // CREATE bản gốc
    @Override
    public PostsEntity createDefault(PostsCreateRequest req) {

        String groupId = new ObjectId().toString();

        PostsEntity post = PostsEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(groupId)
                .originId(null)
                .isDefaultLocale(true)
                .title(req.getTitle())
                .slug(req.getSlug())
                .thumbnail(req.getThumbnail())
                .excerpt(req.getExcerpt())
                .contentHtml(req.getContentHtml())
                .categoryId(req.getCategoryId())
                .tags(req.getTags())
                .status(ContentStatus.draft)
                .seo(mapSeo(req))
                .publishedAt(null)
                .build();

        return postRepository.save(post);
    }

    @Override
    public PostsEntity createTranslation(String originPostId, PostsCreateRequest req) {

        PostsEntity origin = postRepository.findById(originPostId)
                .orElseThrow(() -> new RuntimeException("Origin post not found"));

        PostsEntity post = PostsEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(origin.getTranslationGroupId())
                .originId(origin.getId())
                .isDefaultLocale(false)
                .title(req.getTitle())
                .slug(req.getSlug())
                .thumbnail(req.getThumbnail())
                .excerpt(req.getExcerpt())
                .contentHtml(req.getContentHtml())
                .categoryId(req.getCategoryId())
                .tags(req.getTags())
                .status(ContentStatus.draft)
                .seo(mapSeo(req))
                .publishedAt(null)
                .build();

        return postRepository.save(post);
    }

    @Override
    public PostsEntity update(String id, PostsUpdateRequest req) {

        PostsEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (req.getTitle() != null) post.setTitle(req.getTitle());
        if (req.getSlug() != null) post.setSlug(req.getSlug());
        if (req.getThumbnail() != null) post.setThumbnail(req.getThumbnail());
        if (req.getExcerpt() != null) post.setExcerpt(req.getExcerpt());
        if (req.getContentHtml() != null) post.setContentHtml(req.getContentHtml());
        if (req.getCategoryId() != null) post.setCategoryId(req.getCategoryId());
        if (req.getTags() != null) post.setTags(req.getTags());

        if (req.getSeo() != null) {
            SEOMeta seo = new SEOMeta();
            seo.setTitle(req.getSeo().getTitle());
            seo.setDescription(req.getSeo().getDescription());
            seo.setKeywords(req.getSeo().getKeywords());
            post.setSeo(seo);
        }

        return postRepository.save(post);
    }

    @Override
    public PostsEntity publish(String id) {
        PostsEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setStatus(ContentStatus.published);
        post.setPublishedAt(Instant.now());

        return postRepository.save(post);
    }

    @Override
    public void delete(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostsEntity getBySlug(String slug, String locale) {
        return postRepository.findBySlugAndLocale(slug, locale)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public List<PostsEntity> getTranslations(String translationGroupId) {
        return postRepository.findByTranslationGroupId(translationGroupId);
    }


    private SEOMeta mapSeo(PostsCreateRequest req) {
        if (req.getSeo() == null) return null;

        SEOMeta seo = new SEOMeta();
        seo.setTitle(req.getSeo().getTitle());
        seo.setDescription(req.getSeo().getDescription());
        seo.setKeywords(req.getSeo().getKeywords());
        return seo;
    }
}
