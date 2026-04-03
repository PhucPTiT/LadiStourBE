package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import com.ladi.stour.embedded.Location;
import com.ladi.stour.embedded.SEOMeta;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "destinations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationsEntity extends BaseDocument {
    @Id
    private String id;

    private String locale;
    private String translationGroupId;
    private String originId;
    private boolean isDefaultLocale;

    private String name;
    @Indexed(unique = true)
    private String slug;
    private String thumbnail;
    private String banner;

    private String shortDescription;
    private String description;

    private Location location;
    private boolean isFeatured;

    private SEOMeta seo;
}
