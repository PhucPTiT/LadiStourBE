package com.ladi.stour.controller;

import com.ladi.stour.dto.BookingCreateRequest;
import com.ladi.stour.dto.BookingStatusUpdateRequest;
import com.ladi.stour.entity.BookingEntity;
import com.ladi.stour.enums.BookingStatus;
import com.ladi.stour.service.InterfaceBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final InterfaceBookingService bookingService;

    @PostMapping
    public BookingEntity create(@RequestBody @Valid BookingCreateRequest req) {
        return bookingService.create(req);
    }

    @GetMapping
    public List<BookingEntity> getAll(@RequestParam(required = false) BookingStatus status) {
        return bookingService.getAll(status);
    }

    @GetMapping("/{id}")
    public BookingEntity getById(@PathVariable String id) {
        return bookingService.getById(id);
    }

    @PatchMapping("/{id}/status")
    public BookingEntity updateStatus(@PathVariable String id, @RequestBody @Valid BookingStatusUpdateRequest req) {
        return bookingService.updateStatus(id, req);
    }
}
