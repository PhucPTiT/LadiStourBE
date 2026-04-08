package com.ladi.stour.service.impl;

import com.ladi.stour.dto.BookingCreateRequest;
import com.ladi.stour.dto.BookingStatusUpdateRequest;
import com.ladi.stour.entity.BookingEntity;
import com.ladi.stour.entity.SettingsEntity;
import com.ladi.stour.enums.BookingStatus;
import com.ladi.stour.repository.BookingRepository;
import com.ladi.stour.repository.SettingsRepository;
import com.ladi.stour.service.InterfaceBookingService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements InterfaceBookingService {
    private final BookingRepository bookingRepository;
    private final SettingsRepository settingsRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String senderEmail;

    @Override
    public BookingEntity create(BookingCreateRequest req) {
        BookingEntity booking = BookingEntity.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .message(req.getMessage())
                .nameTour(req.getNameTour())
                .numberOfGuests(req.getNumberOfGuests())
                .status(BookingStatus.PENDING)
                .notificationSent(false)
                .build();

        booking = bookingRepository.save(booking);

        try {
            sendNotificationEmail(booking);
            booking.setNotificationSent(true);
            booking.setNotificationError(null);
            booking.setNotificationSentAt(Instant.now());
        } catch (MailException | jakarta.mail.MessagingException e) {
            booking.setNotificationSent(false);
            booking.setNotificationError(e.getMessage());
            log.error("Failed to send booking notification email for booking {}", booking.getId(), e);
        }

        return bookingRepository.save(booking);
    }

    @Override
    public List<BookingEntity> getAll(BookingStatus status) {
        if (status != null) {
            return bookingRepository.findByStatusOrderByCreatedAtDesc(status);
        }
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public BookingEntity getById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public BookingEntity updateStatus(String id, BookingStatusUpdateRequest req) {
        BookingEntity booking = getById(id);
        booking.setStatus(req.getStatus());
        return bookingRepository.save(booking);
    }

    private void sendNotificationEmail(BookingEntity booking) throws jakarta.mail.MessagingException {
        String adminBookingEmail = resolveAdminBookingEmail();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        if (hasText(senderEmail)) {
            helper.setFrom(senderEmail);
        }
        helper.setTo(adminBookingEmail);
        helper.setReplyTo(booking.getEmail());
        helper.setSubject(buildSubject(booking));
        helper.setText(buildEmailBody(booking), false);

        mailSender.send(message);
    }

    private String resolveAdminBookingEmail() {
        SettingsEntity settings = settingsRepository.findFirstByOrderByCreatedAtAsc()
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        if (!hasText(settings.getEmail())) {
            throw new RuntimeException("Admin booking email is not configured in settings");
        }

        return settings.getEmail();
    }

    private String buildSubject(BookingEntity booking) {
        if (hasText(booking.getNameTour())) {
            return "New booking inquiry - " + booking.getNameTour();
        }
        return "New booking inquiry from " + booking.getName();
    }

    private String buildEmailBody(BookingEntity booking) {
        return """
                New booking inquiry received

                Name: %s
                Email: %s
                Phone: %s
                Tour: %s
                Number of guests: %s
                Message:
                %s
                """.formatted(
                booking.getName(),
                booking.getEmail(),
                booking.getPhone(),
                hasText(booking.getNameTour()) ? booking.getNameTour() : "N/A",
                booking.getNumberOfGuests() != null ? booking.getNumberOfGuests() : "N/A",
                booking.getMessage()
        );
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
