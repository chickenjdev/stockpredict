package predict.stock.route;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import predict.stock.base.SPData;


public abstract class PostRoute extends BaseRoute {

    @Override
    void executeRequest(SPData input, JsonObject headers, String body, HttpServerRequest request
            , HttpServerResponse httpResponse, Handler<JsonObject> response) {
        executePostRequest(input, body, response);
    }

    protected abstract void executePostRequest(SPData input, String body, Handler<JsonObject> response);
}
