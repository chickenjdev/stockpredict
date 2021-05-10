package predict.stock.rabbit;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.BasicProperties;
import io.vertx.rabbitmq.Envelope;
import io.vertx.rabbitmq.RabbitMQMessage;
import predict.stock.base.SPData;
import predict.stock.errors.Errors;
import predict.stock.obj.GlobalCfg;
import predict.stock.utils.AppUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractRabbitClient extends AbstractRabbitMQ{

    Cache<String, Message<SPData>> cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private static final AtomicInteger counterInstance = new AtomicInteger();

    private String consumerQueue;

    private String producer;

    @Override
    protected void initRabbitMQ(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise) {
        producer = verticleCfg.stream().filter(globalCfg -> "produceEndpoint".equalsIgnoreCase(globalCfg.getSubKey()))
                .map(GlobalCfg::getContent)
                .findFirst().orElse(null);

        consumerQueue = generateAutoQueueName(this.getClass().getSimpleName());

        createAutoQueue(consumerQueue)
                .compose(next -> consumeReply(consumerQueue))
                .compose(next -> manualConfig(verticleCfg, config))
                .onComplete(startPromise);
    }

    Future<Void> manualConfig(List<GlobalCfg> verticleCfg, JsonObject config){
        return Future.future(handler -> initRabbitClient(verticleCfg, config, handler));
    }

    protected abstract void initRabbitClient(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> future);

    @Override
    protected Handler<RabbitMQMessage> rabbitMQConsumerHandler() {
        return msg -> {
            var body = msg.body().toString();
            var properties = msg.properties();
            var corId = properties.correlationId();

            var message = cache.getIfPresent(corId);

            if(message == null){
                return;
            }
            cache.invalidate(corId);

            executeConsume(message.body(), body, properties, msg.envelope(), msg.consumerTag(), message);
        };
    }

    protected abstract void executeConsume(SPData input, String body, BasicProperties properties, Envelope envelope, String tag, Message<SPData> message);

    protected void sendDirect(String message, Message<SPData> callback){
        var jMessage = buildMessage(consumerQueue, message, null);
        cacheCallBack(jMessage, callback);
        if(Strings.isNullOrEmpty(producer)){
            var input = callback.body();
            AppUtils.setError(input, Errors.SYSTEM_ERROR, spConfig);
            callback.reply(input);
            return;
        }
        send("", producer, jMessage);
    }

    protected void sendDirect(String queue, String message, Message<SPData> callback){
        var jMessage = buildMessage(consumerQueue, message, null);
        cacheCallBack(jMessage, callback);
        send("", queue, jMessage);
    }

    protected void sendExchange(String exchange, String routing, String message, Message<SPData> callback){
        var jMessage = buildMessage(consumerQueue, message, null);
        cacheCallBack(jMessage, callback);
        send(exchange, routing, jMessage);
    }

    private void cacheCallBack(JsonObject message, Message<SPData> callback){
        var corId = getCorId(message);
        if(callback != null) cache.put(corId, callback);
    }

    private Future<Boolean> createAutoQueue(String queue){
        return Future.future(handler -> createQueue(queue, handler::complete));
    }

    private String generateAutoQueueName(String clazz){
        return String.format("auto_%s_%s", clazz, UUID.randomUUID().toString());
    }

    private Future<Boolean> consumeReply(String queue){
        return Future.future(handler -> consume(queue, handler::complete));
    }
}
