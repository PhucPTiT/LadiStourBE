package com.ladi.stour.controller;

import com.ladi.stour.dto.HomeHeroSectionUpdateRequest;
import com.ladi.stour.entity.HomeHeroSectionEntity;
import com.ladi.stour.service.InterfaceHomeHeroSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home-hero-section")
@RequiredArgsConstructor
public class HomeHeroSectionController {
    private final InterfaceHomeHeroSectionService homeHeroSectionService;

    @GetMapping
    public HomeHeroSectionEntity get() {
        return homeHeroSectionService.get();
    }

    @PutMapping
    public HomeHeroSectionEntity update(@RequestBody @Valid HomeHeroSectionUpdateRequest req) {
        return homeHeroSectionService.update(req);
    }
}
