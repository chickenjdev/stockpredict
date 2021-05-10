package predict.stock.redis;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.List;

public abstract class SPRedisImpl implements SPRedis {

    WorkerExecutor workerExecutor;
    RedisClient redisClient;
    Vertx vertx;
    protected SPRedisImpl(Vertx vertx, RedisOptions options){
        this.vertx = vertx;
        this.redisClient = RedisClient.create(vertx, options);
        this.workerExecutor = vertx.createSharedWorkerExecutor("redis-pool", 200);
    }

    @Override
    public void get(String key, Handler<String> callback) {
        workerExecutor.<String>executeBlocking(future -> {
            redisClient.get(key, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("Failed Redis Key " + key));
                    return;
                }
                future.complete(asyncResult.result());
            });
        }, result -> {
            if(result.failed()){
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });

    }


    @Override
    public void set(String key, String value, Handler<Boolean> callback) {
        workerExecutor.<Boolean>executeBlocking(future -> {
            var args = List.of(key, value);
            redisClient.set(key, value, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("Set Redis Failed " + key));
                    return;
                }
                future.complete(true);
            });
        }, result -> {
            if(result.failed()){
                callback.handle(false);
                return;
            }
            callback.handle(result.result());
        });
    }

    @Override
    public void setEx(String key, long exp, String value, Handler<Boolean> callback) {
        workerExecutor.<Boolean>executeBlocking(future -> {
            redisClient.setex(key, exp, value, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("SetEx Redis Failed KEY " + key));
                    return;
                }
                future.complete("OK".equalsIgnoreCase(asyncResult.result()));
            });
        }, result -> {
            if(result.failed()){
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });
    }

    public void hget(String key, String sub, Handler<String> callback){
        workerExecutor.<String>executeBlocking(future -> {
            redisClient.hget(key, sub, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("HGET FAIELD KEY " + key));
                    return;
                }
                future.complete(asyncResult.result());
            });
        }, result -> {
            if(result.failed()){
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });

    }

    @Override
    public void hset(String key, String sub, String value, Handler<Long> callback){
        workerExecutor.<Long>executeBlocking(future -> {
            redisClient.hset(key, sub, value, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("Hset Redis Failed " + key));
                    return;
                }
                future.complete(asyncResult.result());
            });
        }, result -> {
            if(result.failed()){
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });
    }

    @Override
    public void hgetAll(String key, Handler<JsonObject> callback){
        workerExecutor.<JsonObject>executeBlocking(future -> {
            redisClient.hgetall(key, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("Hset Redis Failed " + key));
                    return;
                }
                future.complete(asyncResult.result());
            });
        }, result -> {
            if(result.failed()){
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });
    }

    @Override
    public void subcribe(String topic, Handler<JsonArray> callback){
        workerExecutor.<JsonArray>executeBlocking(future -> {
            redisClient.subscribe(topic, asyncResult -> {
                if(asyncResult.failed()){
                    future.fail(new NullPointerException("Hset Redis Failed " + topic));
                    return;
                }
                future.complete(asyncResult.result());
            });
        }, result -> {
            if(result.failed()){
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });
    }

    @Override
    public void expire(String key, long ttl, Handler<Long> callback) {
        workerExecutor.<Long>executeBlocking(future -> {
            redisClient.expire(key, ttl, asyncResult -> {
                if (asyncResult.failed()) {
                    future.fail(new NullPointerException("Set Expire Redis Failed " + key));
                    return;
                }
                future.complete(asyncResult.result());
            });
        }, result -> {
            if (result.failed()) {
                callback.handle(null);
                return;
            }
            callback.handle(result.result());
        });
    }
}
