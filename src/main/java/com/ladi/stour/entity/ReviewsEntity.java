package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import com.ladi.stour.enums.ReviewType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewsEntity extends BaseDocument {
    @Id
    private String id;

    private ReviewType type;
    private String tourId;
    private String companyId;

    private String locale;
    private Integer rating;
    private String comment;

    private String authorName;
    private String authorAvatar;

    private Boolean isApproved;
}
