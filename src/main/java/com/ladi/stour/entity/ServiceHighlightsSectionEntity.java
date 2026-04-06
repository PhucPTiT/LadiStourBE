package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import com.ladi.stour.embedded.LocalizedContent;
import com.ladi.stour.embedded.ServiceHighlightItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "service_highlights_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceHighlightsSectionEntity extends BaseDocument {
    @Id
    private String id;

    private LocalizedContent eyebrow;
    private LocalizedContent title;
    private LocalizedContent description;
    private List<ServiceHighlightItem> items;
}
