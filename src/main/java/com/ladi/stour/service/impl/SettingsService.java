package com.ladi.stour.service.impl;

import com.ladi.stour.dto.SettingsCreateRequest;
import com.ladi.stour.dto.SettingsUpdateRequest;
import com.ladi.stour.entity.SettingsEntity;
import com.ladi.stour.repository.SettingsRepository;
import com.ladi.stour.service.InterfaceSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingsService implements InterfaceSettingsService {
    private final SettingsRepository settingsRepository;

    @Override
    public SettingsEntity create(SettingsCreateRequest req) {
        SettingsEntity settings = SettingsEntity.builder()
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .address(req.getAddress())
                .intro(req.getIntro())
                .social(req.getSocial())
                .contentHTMLPageAbout(req.getContentHTMLPageAbout())
                .build();

        return settingsRepository.save(settings);
    }

    @Override
    public SettingsEntity update(String id, SettingsUpdateRequest req) {
        SettingsEntity settings = settingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        if (req.getEmail() != null) settings.setEmail(req.getEmail());
        if (req.getPhoneNumber() != null) settings.setPhoneNumber(req.getPhoneNumber());
        if (req.getAddress() != null) settings.setAddress(req.getAddress());
        if (req.getIntro() != null) settings.setIntro(req.getIntro());
        if (req.getSocial() != null) settings.setSocial(req.getSocial());
        if (req.getContentHTMLPageAbout() != null) settings.setContentHTMLPageAbout(req.getContentHTMLPageAbout());

        return settingsRepository.save(settings);
    }

    @Override
    public SettingsEntity getById(String id) {
        return settingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
    }

    @Override
    public SettingsEntity getDefault() {
        List<SettingsEntity> all = settingsRepository.findAll();
        if (all.isEmpty()) {
            throw new RuntimeException("No settings found");
        }
        return all.getFirst();
    }

    @Override
    public void delete(String id) {
        settingsRepository.deleteById(id);
    }
}
