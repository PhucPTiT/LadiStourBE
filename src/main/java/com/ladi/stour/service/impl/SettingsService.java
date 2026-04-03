package com.ladi.stour.service.impl;

import com.ladi.stour.dto.SettingsCreateRequest;
import com.ladi.stour.dto.SettingsUpdateRequest;
import com.ladi.stour.entity.SettingsEntity;
import com.ladi.stour.repository.SettingsRepository;
import com.ladi.stour.service.InterfaceSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SettingsService implements InterfaceSettingsService {
    private final SettingsRepository settingsRepository;

    @Override
    public SettingsEntity create(SettingsCreateRequest req) {
        if (settingsRepository.findFirstByOrderByCreatedAtAsc().isPresent()) {
            throw new RuntimeException("Settings already exists");
        }

        return settingsRepository.save(buildSettings(req));
    }

    @Override
    public SettingsEntity get() {
        return settingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(() -> SettingsEntity.builder()
                        .social(Collections.emptyList())
                        .build());
    }

    @Override
    public SettingsEntity update(SettingsUpdateRequest req) {
        SettingsEntity settings = settingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(SettingsEntity::new);

        if (req.getEmail() != null) settings.setEmail(req.getEmail());
        if (req.getPhoneNumber() != null) settings.setPhoneNumber(req.getPhoneNumber());
        if (req.getAddress() != null) settings.setAddress(req.getAddress());
        if (req.getIntro() != null) settings.setIntro(req.getIntro());
        if (req.getSocial() != null) settings.setSocial(req.getSocial());
        if (req.getContentHTMLPageAbout() != null) settings.setContentHTMLPageAbout(req.getContentHTMLPageAbout());

        return settingsRepository.save(settings);
    }

    @Override
    public SettingsEntity reset() {
        SettingsEntity settings = getSettingsOrThrow();
        settings.setEmail(null);
        settings.setPhoneNumber(null);
        settings.setAddress(null);
        settings.setIntro(null);
        settings.setSocial(null);
        settings.setContentHTMLPageAbout(null);

        return settingsRepository.save(settings);
    }

    private SettingsEntity getSettingsOrThrow() {
        return settingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    private SettingsEntity buildSettings(SettingsCreateRequest req) {
        return SettingsEntity.builder()
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .address(req.getAddress())
                .intro(req.getIntro())
                .social(req.getSocial())
                .contentHTMLPageAbout(req.getContentHTMLPageAbout())
                .build();
    }
}
