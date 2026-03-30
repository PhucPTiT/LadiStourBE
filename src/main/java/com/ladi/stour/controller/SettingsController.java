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

    @PutMapping("/{id}")
    public SettingsEntity update(@PathVariable String id, @RequestBody @Valid SettingsUpdateRequest req) {
        return settingsService.update(id, req);
    }

    @GetMapping("/{id}")
    public SettingsEntity getById(@PathVariable String id) {
        return settingsService.getById(id);
    }

    @GetMapping("/default")
    public SettingsEntity getDefault() {
        return settingsService.getDefault();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        settingsService.delete(id);
    }
}
