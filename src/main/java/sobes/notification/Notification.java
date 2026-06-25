package sobes.notification;

public record Notification(String id, NotificationType type, String reciverId, String msg) {
}
