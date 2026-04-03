package com.ladi.stour.controller;

import com.ladi.stour.dto.SettingsCreateRequest;
import com.ladi.stour.dto.SettingsUpdateRequest;
import com.ladi.stour.entity.SettingsEntity;
import com.ladi.stour.service.InterfaceSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final InterfaceSettingsService settingsService;

    @PostMapping
    public SettingsEntity create(@RequestBody @Valid SettingsCreateRequest req) {
        return settingsService.create(req);
    }

    @GetMapping
    public SettingsEntity get() {
        return settingsService.get();
    }

    @PutMapping
    public SettingsEntity update(@RequestBody @Valid SettingsUpdateRequest req) {
        return settingsService.update(req);
    }

    @PostMapping("/reset")
    public SettingsEntity reset() {
        return settingsService.reset();
    }
}
