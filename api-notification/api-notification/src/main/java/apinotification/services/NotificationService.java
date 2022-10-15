package apinotification.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import apinotification.models.NotificationModel;

public interface NotificationService {

	NotificationModel saveNotification(NotificationModel notificationModel);

	Page<NotificationModel> findAllNotificationsByUser(UUID userId, Pageable pageable);

	Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId);

}
