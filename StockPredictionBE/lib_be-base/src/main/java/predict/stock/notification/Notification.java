package predict.stock.notification;

import com.google.inject.ImplementedBy;

@ImplementedBy(NotificationImpl.class)
public interface Notification {
    void sendNotification(String content, String title, String phoneNumber, String extra, String serviceId);
}
