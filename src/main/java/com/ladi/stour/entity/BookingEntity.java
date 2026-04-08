package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import com.ladi.stour.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity extends BaseDocument {
    @Id
    private String id;

    private String name;

    @Indexed
    private String email;

    private String phone;
    private String message;
    private String nameTour;
    private Integer numberOfGuests;
    private BookingStatus status;
    private boolean notificationSent;
    private String notificationError;
    private Instant notificationSentAt;
}
