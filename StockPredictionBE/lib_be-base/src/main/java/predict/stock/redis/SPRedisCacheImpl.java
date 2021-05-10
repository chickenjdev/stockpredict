package predict.stock.redis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.redis.RedisOptions;

@Singleton
public class SPRedisCacheImpl extends SPRedisImpl {

    @Inject
    public SPRedisCacheImpl(Vertx vertx, RedisOptions options){
        super(vertx, options);
    }
}
