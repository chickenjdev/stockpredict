package predict.stock.oracle;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import predict.stock.log.SPLog;

public class SPOracleLrac extends SPOracleImpl {

    public SPOracleLrac(Vertx vertx, JsonObject config, SPLog SPLog) {
        super(vertx, config, SPLog);
    }
}
