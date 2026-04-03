package com.ladi.stour.service;

import com.ladi.stour.dto.SettingsCreateRequest;
import com.ladi.stour.dto.SettingsUpdateRequest;
import com.ladi.stour.entity.SettingsEntity;

public interface InterfaceSettingsService {
    SettingsEntity create(SettingsCreateRequest req);
    SettingsEntity get();
    SettingsEntity update(SettingsUpdateRequest req);
    SettingsEntity reset();
}
