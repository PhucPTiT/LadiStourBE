package com.ladi.stour.service.impl;

import com.ladi.stour.common.SlugGenerator;
import com.ladi.stour.dto.MessageResponse;
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
                .slug(resolveSlugForCreate(req.getSlug(), req.getTitle()))
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
                .slug(resolveSlugForCreate(req.getSlug(), req.getTitle()))
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
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            post.setSlug(resolveSlugForUpdate(req.getSlug(), id));
        }
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
    public MessageResponse delete(String id) {
        postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.deleteById(id);
        return new MessageResponse("Xoa post thanh cong");
    }

    @Override
    public PostsEntity getById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
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

    @Override
    public List<PostsEntity> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<PostsEntity> getPublished(String locale) {
        return postRepository.findByLocaleAndStatus(locale, ContentStatus.published);
    }


    private SEOMeta mapSeo(PostsCreateRequest req) {
        if (req.getSeo() == null) return null;

        SEOMeta seo = new SEOMeta();
        seo.setTitle(req.getSeo().getTitle());
        seo.setDescription(req.getSeo().getDescription());
        seo.setKeywords(req.getSeo().getKeywords());
        return seo;
    }

    private String resolveSlugForCreate(String requestedSlug, String fallbackTitle) {
        String baseSlug = buildBaseSlug(requestedSlug, fallbackTitle);
        String slug = baseSlug;
        int counter = 1;

        while (postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String resolveSlugForUpdate(String requestedSlug, String id) {
        String baseSlug = buildBaseSlug(requestedSlug, null);
        String slug = baseSlug;
        int counter = 1;

        while (postRepository.existsBySlugAndIdNot(slug, id)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String buildBaseSlug(String requestedSlug, String fallbackTitle) {
        String source = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug : fallbackTitle;
        String slug = SlugGenerator.generateSlug(source);
        if (slug.isBlank()) {
            throw new RuntimeException("Unable to generate slug");
        }
        return slug;
    }
}
