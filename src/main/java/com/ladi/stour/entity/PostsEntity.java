package com.ladi.stour.entity;

import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.enums.ContentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsEntity {
    @Id
    private String id;

    private String locale;
    private String translationGroupId;
    private String originId;
    private boolean isDefaultLocale;

    private String title;
    private String slug;
    private String thumbnail;
    private String excerpt;
    private String contentHtml;

    private String categoryId;
    private List<String> tags;

    private ContentStatus status;
    private SEOMeta seo;

    private Instant publishedAt;
}
