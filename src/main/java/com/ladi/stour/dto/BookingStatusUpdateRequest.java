package com.ladi.stour.dto;

import com.ladi.stour.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingStatusUpdateRequest {
    @NotNull
    private BookingStatus status;
}
