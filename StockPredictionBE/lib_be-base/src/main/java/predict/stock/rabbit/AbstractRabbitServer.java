package predict.stock.rabbit;

import com.google.common.base.Strings;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.BasicProperties;
import io.vertx.rabbitmq.Envelope;
import io.vertx.rabbitmq.RabbitMQMessage;
import predict.stock.obj.GlobalCfg;

import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractRabbitServer extends AbstractRabbitMQ{

    @Override
    protected void initRabbitMQ(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise) {
        var queue = verticleCfg.stream().filter(globalCfg -> "consumeEndpoint".equalsIgnoreCase(globalCfg.getSubKey()))
                .map(GlobalCfg::getContent)
                .findFirst().orElse(null);

        if(queue == null){
            startPromise.fail(new NullPointerException("Queue is not config"));
            return;
        }

        initConsume(queue).onComplete(event -> {
            if (event.failed()) {
                startPromise.fail("Start Failed RabbitMQ");
                return;
            }
            initRabbitServer(verticleCfg, config, startPromise);
        });
    }

    private Future<Boolean> initConsume(String queue){
        return Future.future(event -> consume(queue, event::complete));
    }

    @Override
    Handler<RabbitMQMessage> rabbitMQConsumerHandler() {
        return msg -> {
            var body = msg.body().toString(StandardCharsets.UTF_8);
            var consumerTag = msg.consumerTag();
            var properties = msg.properties();
            var envelop = msg.envelope();

            executeMessage(body, consumerTag, properties, envelop, message -> {
                var queueReply = properties.replyTo();
                if(!Strings.isNullOrEmpty(queueReply)){
                    replyMessage(queueReply, message, properties.correlationId());
                }
                ackMessage(envelop.deliveryTag());
            });
        };
    }

    protected abstract void initRabbitServer(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise);

    private void replyMessage(String replyTo, String message, String corId){
        send("", replyTo, buildMessage("", message, corId));
    }

    protected abstract void executeMessage(String body, String consumerTag, BasicProperties properties, Envelope envelope, Handler<String> callback);
}
