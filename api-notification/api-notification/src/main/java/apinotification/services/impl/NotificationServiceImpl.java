package apinotification.services.impl;

import org.springframework.stereotype.Service;

import apinotification.repositories.NotificationRepository;
import apinotification.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

}
