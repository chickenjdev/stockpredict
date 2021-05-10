package predict.stock.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface SPRedis {

    void get(String key, Handler<String> callback);

    void set(String key, String value, Handler<Boolean> callback);

    void setEx(String key, long exp, String value, Handler<Boolean> callback);

    void hget(String key, String sub, Handler<String> callback);

    void hset(String key, String sub, String value, Handler<Long> callback);

    void hgetAll(String key, Handler<JsonObject> callback);

    void subcribe(String key, Handler<JsonArray> callback);

    void expire(String key, long ttl, Handler<Long> callback);

}
