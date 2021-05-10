package predict.stock.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisOptions;
import lombok.SneakyThrows;
import predict.stock.config.spConfig;
import predict.stock.kafka.KafkaClient;
import predict.stock.kafka.KafkaClientImpl;
import predict.stock.log.SPLog;
import predict.stock.notification.Notification;
import predict.stock.notification.NotificationImpl;
import predict.stock.oracle.SPOracle;
import predict.stock.oracle.SPOracleLrac;
import predict.stock.oracle.SPOracleUrac;
import predict.stock.provider.SPLogProvider;
import predict.stock.redis.SPRedis;
import predict.stock.redis.SPRedisCacheImpl;

import java.util.Optional;

public class SPModule extends AbstractModule {

    Vertx vertx;
    JsonObject config;
    Promise<Void> promise;

    public SPModule(Vertx vertx, JsonObject config, Promise<Void> promise){
        this.vertx = vertx;
        this.config = config;
        this.promise = promise;
    }

    @SneakyThrows
    @Override
    protected void configure() {

        bind(Vertx.class).toInstance(vertx);

        var logProvider = new SPLogProvider();
        var log = logProvider.get();
        bind(SPLog.class)
            .toProvider(logProvider);

        bind(SPRedis.class).annotatedWith(Names.named("Cache"))
                .toInstance(new SPRedisCacheImpl(vertx, new RedisOptions(config.getJsonObject("redisCacheCfg"))));

        var redisUser = new SPRedisCacheImpl(vertx, new RedisOptions(config.getJsonObject("redisUserCfg")));
        bind(SPRedis.class).annotatedWith(Names.named("User"))
                .toInstance(redisUser);

        Optional.ofNullable(config.getJsonObject("kafkaCfg"))
                .ifPresent(jsonObject -> bind(KafkaClient.class).toInstance(new KafkaClientImpl(vertx, jsonObject)));

        var urac = new SPOracleUrac(vertx, config.getJsonObject("oracleUracCfg"), log);
        bind(SPOracle.class).annotatedWith(Names.named("Urac"))
                .toInstance(urac);

        var lrac = new SPOracleLrac(vertx, config.getJsonObject("oracleLracCfg"), log);
        bind(SPOracle.class).annotatedWith(Names.named("Lrac"))
                .toInstance(lrac);

        bind(Notification.class).toInstance(new NotificationImpl(vertx, config));

        var cfg = new spConfig(vertx, lrac, log, redisUser);
        bind(spConfig.class).toInstance(cfg);

        cfg.load().onFailure(Throwable::printStackTrace)
            .onComplete(promise);
    }
}
