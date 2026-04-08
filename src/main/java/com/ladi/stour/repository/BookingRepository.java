package com.ladi.stour.repository;

import com.ladi.stour.entity.BookingEntity;
import com.ladi.stour.enums.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<BookingEntity, String> {
    List<BookingEntity> findAllByOrderByCreatedAtDesc();
    List<BookingEntity> findByStatusOrderByCreatedAtDesc(BookingStatus status);
}
