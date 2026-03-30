package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriesEntity extends BaseDocument {
    @Id
    private String id;

    private String locale;
    private String translationGroupId;
    private String originId;
    private boolean isDefaultLocale;

    private String name;
    private String slug;
}
