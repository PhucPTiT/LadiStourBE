package com.ladi.stour.controller;

import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.PostsCreateRequest;
import com.ladi.stour.dto.PostsGetByIdRequest;
import com.ladi.stour.dto.PostsUpdateRequest;
import com.ladi.stour.entity.PostsEntity;
import com.ladi.stour.service.InterfacePostsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {
    private final InterfacePostsService postsService;

    // create default locale
    @PostMapping
    public PostsEntity createDefault(@RequestBody @Valid PostsCreateRequest req) {
        return postsService.createDefault(req);
    }

    // create translation
    @PostMapping("/translations/{originId}")
    public PostsEntity createTranslation(
            @PathVariable String originId,
            @RequestBody @Valid PostsCreateRequest req
    ) {
        return postsService.createTranslation(originId, req);
    }

    @PutMapping("/{id}")
    public PostsEntity update(@PathVariable String id, @RequestBody PostsUpdateRequest req) {
        return postsService.update(id, req);
    }

    @PatchMapping("/{id}/publish")
    public PostsEntity publish(@PathVariable String id) {
        return postsService.publish(id);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        return postsService.delete(id);
    }

    @PostMapping("/detail")
    public PostsEntity getById(@RequestBody @Valid PostsGetByIdRequest req) {
        return postsService.getById(req.getId());
    }

    @GetMapping("/slug/{slug}")
    public PostsEntity getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return postsService.getBySlug(slug, locale);
    }

    @GetMapping("/translations/{groupId}")
    public List<PostsEntity> getTranslations(@PathVariable String groupId) {
        return postsService.getTranslations(groupId);
    }

    @GetMapping("/all")
    public List<PostsEntity> getAll() {
        return postsService.getAll();
    }

    @GetMapping("/published")
    public List<PostsEntity> getPublished(@RequestParam(defaultValue = "vi") String locale) {
        return postsService.getPublished(locale);
    }
}
