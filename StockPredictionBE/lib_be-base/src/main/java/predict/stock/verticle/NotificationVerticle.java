package predict.stock.verticle;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.BasicProperties;
import io.vertx.rabbitmq.Envelope;
import predict.stock.base.SPData;
import predict.stock.log.SPLog;
import predict.stock.obj.GlobalCfg;
import predict.stock.rabbit.AbstractRabbitClient;
import predict.stock.utils.KeyUtils;

import javax.inject.Inject;
import java.util.List;

public class NotificationVerticle extends AbstractRabbitClient {
    @Inject
    SPLog log;
    @Override
    protected Handler<Message<SPData>> consumeEventBus() {
        return msg -> {
            var data = msg.body();
            var notification = data.popExtra(KeyUtils.Common.NOTIFICATION);
            if(notification == null){
                log.error("Notification is empty!!!");
                msg.reply(null);
                return;
            }
            log.info("Noti: "+ notification.toString());
            sendDirect(notification.toString(), msg);
        };
    }

    @Override
    protected void initRabbitClient(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> future) {
        future.complete();
    }

    @Override
    protected void executeConsume(SPData input, String body, BasicProperties properties, Envelope envelope, String tag, Message<SPData> message) {

    }
}
