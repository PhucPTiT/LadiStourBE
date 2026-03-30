package com.ladi.stour.service;

import com.ladi.stour.dto.SettingsCreateRequest;
import com.ladi.stour.dto.SettingsUpdateRequest;
import com.ladi.stour.entity.SettingsEntity;

public interface InterfaceSettingsService {
    SettingsEntity create(SettingsCreateRequest req);
    SettingsEntity update(String id, SettingsUpdateRequest req);
    SettingsEntity getById(String id);
    SettingsEntity getDefault();
    void delete(String id);
}
