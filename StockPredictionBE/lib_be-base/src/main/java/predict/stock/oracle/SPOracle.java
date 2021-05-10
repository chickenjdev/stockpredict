package predict.stock.oracle;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface SPOracle {

    void select(String query, Handler<List<JsonObject>> callback);

    void select(String query, JsonArray param, Handler<List<JsonObject>> callback);

    void insert(String sql, JsonArray params, Handler<Boolean> callback);

    void update(String sql, JsonArray params, Handler<Boolean> callback);

    void call(String sql, Handler<List<JsonObject>> handler);

}
