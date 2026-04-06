package com.ladi.stour.service.impl;

import com.ladi.stour.dto.HomeHeroSectionUpdateRequest;
import com.ladi.stour.embedded.HeroSlideItem;
import com.ladi.stour.entity.HomeHeroSectionEntity;
import com.ladi.stour.repository.HomeHeroSectionRepository;
import com.ladi.stour.service.InterfaceHomeHeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeHeroSectionService implements InterfaceHomeHeroSectionService {
    private final HomeHeroSectionRepository homeHeroSectionRepository;

    @Override
    public HomeHeroSectionEntity get() {
        return homeHeroSectionRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(() -> HomeHeroSectionEntity.builder()
                        .autoPlay(true)
                        .autoPlayDelayMs(5200)
                        .slides(Collections.emptyList())
                        .build());
    }

    @Override
    public HomeHeroSectionEntity update(HomeHeroSectionUpdateRequest req) {
        HomeHeroSectionEntity section = homeHeroSectionRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(HomeHeroSectionEntity::new);

        if (req.getAutoPlay() != null) section.setAutoPlay(req.getAutoPlay());
        if (req.getAutoPlayDelayMs() != null) section.setAutoPlayDelayMs(req.getAutoPlayDelayMs());
        if (req.getSlides() != null) section.setSlides(mapSlides(req.getSlides()));

        if (section.getAutoPlay() == null) section.setAutoPlay(true);
        if (section.getAutoPlayDelayMs() == null) section.setAutoPlayDelayMs(5200);
        if (section.getSlides() == null) section.setSlides(Collections.emptyList());

        return homeHeroSectionRepository.save(section);
    }

    private List<HeroSlideItem> mapSlides(List<HomeHeroSectionUpdateRequest.SlideRequest> slides) {
        return slides.stream().map(slide -> {
            HeroSlideItem item = new HeroSlideItem();
            item.setTitle(slide.getTitle());
            item.setSubtitle(slide.getSubtitle());
            item.setLabel(slide.getLabel());
            item.setCta(slide.getCta());
            item.setImage(slide.getImage());
            item.setHref(slide.getHref());
            item.setSortOrder(slide.getSortOrder());
            item.setActive(slide.getActive());
            return item;
        }).toList();
    }
}
