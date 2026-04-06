package com.ladi.stour.service.impl;

import com.ladi.stour.dto.ServiceHighlightsSectionUpdateRequest;
import com.ladi.stour.embedded.ServiceHighlightItem;
import com.ladi.stour.entity.ServiceHighlightsSectionEntity;
import com.ladi.stour.repository.ServiceHighlightsSectionRepository;
import com.ladi.stour.service.InterfaceServiceHighlightsSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceHighlightsSectionService implements InterfaceServiceHighlightsSectionService {
    private final ServiceHighlightsSectionRepository serviceHighlightsSectionRepository;

    @Override
    public ServiceHighlightsSectionEntity get() {
        return serviceHighlightsSectionRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(() -> ServiceHighlightsSectionEntity.builder()
                        .items(Collections.emptyList())
                        .build());
    }

    @Override
    public ServiceHighlightsSectionEntity update(ServiceHighlightsSectionUpdateRequest req) {
        ServiceHighlightsSectionEntity section = serviceHighlightsSectionRepository.findFirstByOrderByCreatedAtAsc()
                .orElseGet(ServiceHighlightsSectionEntity::new);

        if (req.getEyebrow() != null) section.setEyebrow(req.getEyebrow());
        if (req.getTitle() != null) section.setTitle(req.getTitle());
        if (req.getDescription() != null) section.setDescription(req.getDescription());
        if (req.getItems() != null) section.setItems(mapItems(req.getItems()));

        if (section.getItems() == null) section.setItems(Collections.emptyList());

        return serviceHighlightsSectionRepository.save(section);
    }

    private List<ServiceHighlightItem> mapItems(List<ServiceHighlightsSectionUpdateRequest.ItemRequest> items) {
        return items.stream().map(entry -> {
            ServiceHighlightItem item = new ServiceHighlightItem();
            item.setIcon(entry.getIcon());
            item.setTitle(entry.getTitle());
            item.setText(entry.getText());
            item.setSortOrder(entry.getSortOrder());
            item.setActive(entry.getActive());
            return item;
        }).toList();
    }
}
