package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import com.ladi.stour.embedded.LocalizedContent;
import com.ladi.stour.embedded.SocialLink;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsEntity extends BaseDocument {
    @Id
    private String id;

    private String email;
    private String phoneNumber;
    private String address;

    private LocalizedContent intro;

    private List<SocialLink> social;

    private LocalizedContent contentHTMLPageAbout;
}
