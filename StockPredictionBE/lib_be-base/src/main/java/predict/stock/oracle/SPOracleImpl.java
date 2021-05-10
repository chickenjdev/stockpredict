package predict.stock.oracle;

import com.google.inject.Inject;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import predict.stock.log.SPLog;

import java.util.List;

public abstract class SPOracleImpl implements SPOracle {

    JDBCClient jdbcClient;

    Vertx vertx;

    WorkerExecutor workerExecutor;

    @Inject
    SPLog logger;

    public SPOracleImpl(Vertx vertx, JsonObject config, SPLog SPLog) {
        this.jdbcClient = JDBCClient.createShared(vertx, config);
        this.vertx = vertx;
        this.logger = SPLog;
        initWorker(config);
    }

    private void initWorker(JsonObject config) {
        var poolSize = config.getInteger("maximumPoolSize", 20) * 10;
        this.workerExecutor = vertx.createSharedWorkerExecutor("pool-oracle", poolSize);
    }

    @Override
    public void select(String sql, Handler<List<JsonObject>> callback) {
        jdbcClient.query(sql, resultSetAsyncResult -> {
            if (resultSetAsyncResult.failed()) {
                logger.error(null, "Query fail", resultSetAsyncResult.cause());
                callback.handle(null);
                return;
            }
            var resulSet = resultSetAsyncResult.result();

            if (resulSet == null) {
                callback.handle(null);
                return;
            }
            callback.handle(resulSet.getRows());
        });
    }

    @Override
    public void select(String sql, JsonArray params, Handler<List<JsonObject>> callback) {
        jdbcClient.queryWithParams(sql, params, resultSetAsyncResult -> {
            if (resultSetAsyncResult.failed()) {
                logger.error(null, "Query fail", resultSetAsyncResult.cause());
                callback.handle(null);
                return;
            }
            var resulSet = resultSetAsyncResult.result();

            if (resulSet == null) {
                callback.handle(List.of());
                return;
            }
            callback.handle(resulSet.getRows());
        });
    }

    @Override
    public void insert(String sql, JsonArray params, Handler<Boolean> callback) {
        jdbcClient.updateWithParams(sql, params, resultSetAsyncResult -> {
            if (resultSetAsyncResult.failed()) {
                logger.error(null, "Query fail", resultSetAsyncResult.cause());
                callback.handle(false);
                return;
            }
            var updateResult = resultSetAsyncResult.result();

            if (updateResult == null) {
                callback.handle(false);
                return;
            }

            callback.handle(updateResult.getUpdated() > 0);
        });
    }

    @Override
    public void call(String sql, Handler<List<JsonObject>> callback) {
        try {
            workerExecutor.<List<JsonObject>>executeBlocking(future -> {
                jdbcClient.getConnection(asyncConnection -> {
                    try (var connection = asyncConnection.result()) {
                        connection.call(sql, resultSetAsyncResult -> {
                            if (resultSetAsyncResult.failed()) {
                                logger.error(null, "Query fail", resultSetAsyncResult.cause());
                                future.complete(null);
                                return;
                            }
                            var resultSet = resultSetAsyncResult.result();

                            if (resultSet == null) {
                                future.complete(null);
                                return;
                            }
                            future.complete(resultSet.getRows());
                        });
                    } catch (Exception ex) {
                        logger.error(null, "Error Exception callStore " + ex.getMessage());
                        future.complete(null);
                    }
                });
            }, false, result -> {
                if (result.failed() || result.result() == null) {
                    callback.handle(null);
                    return;
                }
                var list = (List<JsonObject>) result.result();
                callback.handle(list);
            });
        } catch (Exception e) {
            logger.error("Got Exception DB callStore " + e.getMessage());
            callback.handle(null);
        }
    }

    @Override
    public void update(String sql, JsonArray params, Handler<Boolean> callback) {
        insert(sql, params, callback);
    }
}
