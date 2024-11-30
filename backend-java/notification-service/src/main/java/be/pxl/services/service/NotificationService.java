package be.pxl.services.service;

import be.pxl.services.domain.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendMessage(Notification notification){

        System.out.println("Receiving notification...");
        System.out.println("Sending... " + notification.getMessage());
        System.out.println("To " + notification.getSender());
    }
}
