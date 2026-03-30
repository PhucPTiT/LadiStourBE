package com.ladi.stour.dto;

import com.ladi.stour.embedded.LocalizedContent;
import com.ladi.stour.embedded.SocialLink;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.List;

@Data
public class SettingsCreateRequest {
    @Email
    private String email;

    private String phoneNumber;
    private String address;

    private LocalizedContent intro;

    private List<SocialLink> social;

    private LocalizedContent contentHTMLPageAbout;
}
