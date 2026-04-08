package com.ladi.stour.service;

import com.ladi.stour.dto.BookingCreateRequest;
import com.ladi.stour.dto.BookingStatusUpdateRequest;
import com.ladi.stour.entity.BookingEntity;
import com.ladi.stour.enums.BookingStatus;

import java.util.List;

public interface InterfaceBookingService {
    BookingEntity create(BookingCreateRequest req);
    List<BookingEntity> getAll(BookingStatus status);
    BookingEntity getById(String id);
    BookingEntity updateStatus(String id, BookingStatusUpdateRequest req);
}
