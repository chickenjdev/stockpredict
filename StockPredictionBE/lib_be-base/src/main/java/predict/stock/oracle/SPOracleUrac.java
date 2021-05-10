package predict.stock.oracle;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import predict.stock.log.SPLog;

public class SPOracleUrac extends SPOracleImpl {

    public SPOracleUrac(Vertx vertx, JsonObject config, SPLog SPLog) {
        super(vertx, config, SPLog);
    }
}
