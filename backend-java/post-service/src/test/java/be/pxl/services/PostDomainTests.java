package be.pxl.services;

import be.pxl.services.domain.Notification;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostDomainTests {

    @Test
    public void testNotificationBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        Notification notification = Notification.builder()
                .id(1L)
                .message("Test Message")
                .author("Author Name")
                .receiver("Receiver Name")
                .createdDate(now)
                .build();

        assertEquals(1L, notification.getId());
        assertEquals("Test Message", notification.getMessage());
        assertEquals("Author Name", notification.getAuthor());
        assertEquals("Receiver Name", notification.getReceiver());
        assertEquals(now, notification.getCreatedDate());
    }

    @Test
    public void testNotificationNoArgsConstructor() {
        Notification notification = new Notification();

        assertNull(notification.getId());
        assertNull(notification.getMessage());
        assertNull(notification.getAuthor());
        assertNull(notification.getReceiver());
        assertNull(notification.getCreatedDate());
    }

    @Test
    public void testNotificationAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        Notification notification = new Notification(1L, "Test Message", "Author Name", "Receiver Name", now);

        assertEquals(1L, notification.getId());
        assertEquals("Test Message", notification.getMessage());
        assertEquals("Author Name", notification.getAuthor());
        assertEquals("Receiver Name", notification.getReceiver());
        assertEquals(now, notification.getCreatedDate());
    }

    @Test
    public void testNotificationSetters() {
        LocalDateTime now = LocalDateTime.now();

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test Message");
        notification.setAuthor("Author Name");
        notification.setReceiver("Receiver Name");
        notification.setCreatedDate(now);

        assertEquals(1L, notification.getId());
        assertEquals("Test Message", notification.getMessage());
        assertEquals("Author Name", notification.getAuthor());
        assertEquals("Receiver Name", notification.getReceiver());
        assertEquals(now, notification.getCreatedDate());
    }

    @Test
    public void testNotificationEquality() {
        LocalDateTime now = LocalDateTime.now();

        Notification notification1 = new Notification(1L, "Message", "Author", "Receiver", now);
        Notification notification2 = new Notification(1L, "Message", "Author", "Receiver", now);

        assertEquals(notification1, notification2);
        assertEquals(notification1.hashCode(), notification2.hashCode());
    }

    @Test
    public void testNotificationToString() {
        LocalDateTime now = LocalDateTime.now();

        Notification notification = Notification.builder()
                .id(1L)
                .message("Test Message")
                .author("Author Name")
                .receiver("Receiver Name")
                .createdDate(now)
                .build();

        String expected = "Notification(id=1, message=Test Message, author=Author Name, receiver=Receiver Name, createdDate=" + now + ")";
        assertEquals(expected, notification.toString());
    }
}
