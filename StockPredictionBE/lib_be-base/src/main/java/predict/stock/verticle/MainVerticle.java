package predict.stock.verticle;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import predict.stock.base.ISPLogable;
import predict.stock.base.SPBusCodec;
import predict.stock.base.SPData;
import predict.stock.log.SPLog;
import predict.stock.module.SPModule;
import predict.stock.utils.Utils;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public class MainVerticle extends AbstractVerticle implements ISPLogable {

    @Inject
    protected SPLog logger;

    protected static Injector injector;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        JsonObject config = config();
        vertx.eventBus().registerDefaultCodec(SPData.class, new SPBusCodec());
        vertx.executeBlocking(this::buildInjector, false, asyncResult
                -> deployVerticles(config, startPromise));
    }

    private void buildInjector(Promise<Void> future) {
        injector = Guice.createInjector(new SPModule(vertx, config(), future));
        injector.injectMembers(this);
    }

    @Override
    public SPLog getLogger() {
        return logger;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    private void deployVerticles(JsonObject config, Promise<Void> promise) {

        JsonArray verticles = readVerticleFile(config);
        System.setProperty("moduleName", config.getString("moduleName","telco"));

        var iterator = verticles.iterator();

        deploy(iterator, promise);

    }

    private void deploy(Iterator<Object> iterator, Promise<Void> promise) {
        if (!iterator.hasNext()) {
            //Add log deploy success
            promise.complete();
            return;
        }
        var verticle = JsonObject.mapFrom(iterator.next()).put("config", config());

        if (!verticle.getBoolean("isDeploy", true)) {
            deploy(iterator, promise);
            return;
        }

        String name = verticle.getString("address");
        var deploymentOption = new DeploymentOptions(verticle);
        try {
            var clazz = (Class<Verticle>) Class.forName(name);
            Supplier<Verticle> a = () -> injector.getInstance(clazz);
            vertx.deployVerticle(a, deploymentOption, stringAsyncResult -> {
                if(stringAsyncResult.failed()){
                    logger.error(null, String.format("Deploy verticle %s failed", name));
                    return;
                }
                logger.info(null, String.format("Deploy verticle %s success ", name));

                deploy(iterator, promise);
            });
        } catch (ClassNotFoundException e) {
            logger.error(null, e.getMessage());
        }
    }

    private JsonArray readVerticleFile(JsonObject config) {
        var path = Optional.ofNullable(config.getString("verticlePath"))
                .orElse("verticle.json");

        return Utils.readJsonArrayFromFile(path);
    }
}
