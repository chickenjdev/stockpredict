package predict.stock.verticle;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import predict.stock.base.SPData;
import predict.stock.config.spConfig;
import predict.stock.obj.GlobalCfg;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class SPVerticle extends MainVerticle {

  @Inject
  protected spConfig spConfig;

  @Override
  public void start(Promise<Void> startPromise){
    init(config(), startPromise);
  }

  private void init(JsonObject config, Promise<Void> startPromise){

    vertx.eventBus().consumer(this.getClass().getSimpleName(), consumeEventBus());

    //Setup init verticle.
    var verticleCfg = spConfig.getVerticles().stream()
            .filter(globalCfg -> this.getClass().getSimpleName().equalsIgnoreCase(globalCfg.getMainKey()))
            .collect(Collectors.toList());
    initVerticle(verticleCfg, config, startPromise);
  }

  protected Handler<Message<SPData>> consumeEventBus() {
    return msg -> msg.reply(msg.body());
  }

  protected abstract void initVerticle(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise);

  protected void sendEventBus(String clzzName, SPData input, Handler<AsyncResult<Message<SPData>>> handler){
    var timeout = Optional.ofNullable(spConfig.getVerticlesByName(clzzName))
            .filter(globalCfg -> "EVENT_BUS_SENT_TIMEOUT".equalsIgnoreCase(globalCfg.getSubKey()))
            .map(GlobalCfg::getContent).map(Integer::parseInt).orElse(120000);
    vertx.eventBus().request(clzzName, input, new DeliveryOptions().setSendTimeout(timeout), handler);
  }
}