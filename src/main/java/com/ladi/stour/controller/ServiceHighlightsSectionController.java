package com.ladi.stour.controller;

import com.ladi.stour.dto.ServiceHighlightsSectionUpdateRequest;
import com.ladi.stour.entity.ServiceHighlightsSectionEntity;
import com.ladi.stour.service.InterfaceServiceHighlightsSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-highlights-section")
@RequiredArgsConstructor
public class ServiceHighlightsSectionController {
    private final InterfaceServiceHighlightsSectionService serviceHighlightsSectionService;

    @GetMapping
    public ServiceHighlightsSectionEntity get() {
        return serviceHighlightsSectionService.get();
    }

    @PutMapping
    public ServiceHighlightsSectionEntity update(@RequestBody @Valid ServiceHighlightsSectionUpdateRequest req) {
        return serviceHighlightsSectionService.update(req);
    }
}
