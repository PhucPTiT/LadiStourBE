package com.ladi.stour.service;

import com.ladi.stour.dto.HomeHeroSectionUpdateRequest;
import com.ladi.stour.entity.HomeHeroSectionEntity;

public interface InterfaceHomeHeroSectionService {
    HomeHeroSectionEntity get();
    HomeHeroSectionEntity update(HomeHeroSectionUpdateRequest req);
}
