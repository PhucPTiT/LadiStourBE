package com.ladi.stour.service;

import com.ladi.stour.dto.ServiceHighlightsSectionUpdateRequest;
import com.ladi.stour.entity.ServiceHighlightsSectionEntity;

public interface InterfaceServiceHighlightsSectionService {
    ServiceHighlightsSectionEntity get();
    ServiceHighlightsSectionEntity update(ServiceHighlightsSectionUpdateRequest req);
}
