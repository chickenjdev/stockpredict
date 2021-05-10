package predict.stock.rabbit;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.rabbitmq.client.Address;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.*;
import predict.stock.log.SPLog;
import predict.stock.obj.GlobalCfg;
import predict.stock.verticle.SPVerticle;

import java.util.*;
import java.util.stream.Collectors;

abstract class AbstractRabbitMQ extends SPVerticle {

    @Inject
    SPLog logger;

    private RabbitMQClient rabbitMQClient;

    @Override
    protected void initVerticle(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise) {

        //Load rabbit of class
        var rabbitCfg = config.getJsonObject("rabbitCfg");

        if(rabbitCfg == null){
            startPromise.fail(new NullPointerException("Configuration is null"));
            logger.error(null,"rabbitMQClient is null");
            return;
        }

        rabbitMQClient = RabbitMQClient.create(vertx, buildOptions(rabbitCfg));

       rabbitMQClient.start(startRabbit(verticleCfg, config, startPromise));
    }

    private RabbitMQOptions buildOptions(JsonObject rabbitCfg){
        var options = new RabbitMQOptions(rabbitCfg);
        var config = Optional.ofNullable(rabbitCfg.getString("addresses"))
                .orElse("");
        var addresses = Arrays.stream(config.split(","))
                .map(Address::new)
                .collect(Collectors.toList());
        options.setAddresses(addresses);
        return options;
    }
    protected Handler<AsyncResult<Void>> startRabbit(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise){
        return msg -> {
          if(msg.failed()){
              logger.error(null, "Start rabbit failed");
              return;
          }

          initRabbitMQ(verticleCfg, config, startPromise);
        };
    }

    protected abstract void initRabbitMQ(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise);

    void send(String exchange, String routing, JsonObject message){
        rabbitMQClient.basicPublish(exchange, routing, message, asyncPublishHandler(exchange, routing));
    }

    private Handler<AsyncResult<Void>> asyncPublishHandler(String exchange, String routing){
        return msg -> {
            if(msg.failed()){
                logger.error(null, String.format("Send message to exchange %s with routing %s failed", exchange, routing));
                return;
            }
            logger.info(null, String.format("Send message to exchange %s with routing %s success", exchange, routing));
        };
    }

    void consume(String queue, Handler<Boolean> callback){
        var options = new QueueOptions();
        options.setAutoAck(autoAck());
        rabbitMQClient.basicConsumer(queue, consumeMessage(queue, callback));
    }

    private Handler<AsyncResult<RabbitMQConsumer>> consumeMessage(String queue, Handler<Boolean> callback){
        return msg -> {
          if(msg.failed()){
            logger.error(null, String.format("Consume queue %s failed ", queue));
            callback.handle(false);
            return;
          }
          var consumer = msg.result();
          consumer.handler(rabbitMQConsumerHandler())
          .exceptionHandler(Throwable::printStackTrace)
          .endHandler(event -> logger.warn(null, String.format("Message from queue %s was cancelled ", queue)));
          callback.handle(true);
        };
    }

    protected RabbitMQClient getClient(){
        return rabbitMQClient;
    }

    abstract Handler<RabbitMQMessage> rabbitMQConsumerHandler();

    void createQueue(String queue, Handler<Boolean> callback){
        rabbitMQClient.queueDeclare(queue, true, false, false, asyncPublishHandler -> {
            callback.handle(asyncPublishHandler.succeeded());
        });
    }

    JsonObject buildMessage(String queue, String message, String corId){
        var random = String.valueOf(new Random().nextInt(100));
        if(Strings.isNullOrEmpty(corId)){
            corId = String.format("%s_%s", random, UUID.randomUUID().toString());
        }
        return new JsonObject().put("body", message)
                .put("properties", buildProperties(queue, corId));
    }

    String getCorId(JsonObject message){
        return message.getJsonObject("properties").getString("correlationId");
    }

    private JsonObject buildProperties(String queue, String corId){
        return new JsonObject().put("replyTo", queue)
                .put("correlationId", corId);
    }

    protected void ackMessage(long deliveryTag){
        if(!autoAck()){
            rabbitMQClient.basicAck(deliveryTag, true, result -> {
                if(result.failed()){
                    logger.error("basic Ack Failed :: " + deliveryTag);
                }
                var res = result.result();
                logger.info("basic Ack:: " + deliveryTag + " | " + res);
            });
        }
    }

    protected boolean autoAck(){
        return true;
    }
}
