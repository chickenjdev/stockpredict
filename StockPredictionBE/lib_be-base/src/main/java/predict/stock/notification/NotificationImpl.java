package predict.stock.notification;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import predict.stock.base.SPData;
import predict.stock.utils.KeyUtils;
import predict.stock.verticle.NotificationVerticle;

public class NotificationImpl implements Notification{
    Vertx vertx;
    JsonObject config;
    public NotificationImpl(Vertx vertx, JsonObject config){
        this.vertx = vertx;
        this.config = config;
    }

    @Override
    public void sendNotification(String content, String title, String phoneNumber, String extra, String serviceId) {
        var input = new SPData();
        var notification = buildNotification(content, title, phoneNumber, extra, serviceId);
        input.putExtra(KeyUtils.Common.NOTIFICATION, notification.toString());
        vertx.eventBus().send(NotificationVerticle.class.getSimpleName(), input);
    }

    private JsonObject buildNotification(String content, String title, String phoneNumber, String extra, String serviceId){
        var joNoti = new JsonObject();
        var msg = new JsonObject().put(KeyUtils.Notification.BODY, content)
                .put(KeyUtils.Notification.CAPTION, title)
                .put(KeyUtils.Notification.TYPE, NotificationType.NOTI_TOPUP.getValue())
                .put(KeyUtils.Notification.RECEIVER_NUMBER, phoneNumber)
                .put(KeyUtils.Notification.EXTRA, extra)
                .put(KeyUtils.Notification.CATEGORY, 11)
                .put(KeyUtils.Notification.SERVICE_NAME, serviceId)
                .put(KeyUtils.Notification.CLASS, "mservice.backend.entity.msg.Notification");
        joNoti.put(KeyUtils.Common.MSG_TYPE, "NOTI_MSG");
        joNoti.put(KeyUtils.Common.sp_MSG, msg);
        joNoti.put(KeyUtils.Notification.USER, phoneNumber);
        return joNoti;
    }
}
