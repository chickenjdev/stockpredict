package predict.stock.dao;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import predict.stock.oracle.SPOracle;

import java.util.List;

public class ServiceDao {

    public SPOracle oracle;

    public ServiceDao(SPOracle SPOracle){
        this.oracle = SPOracle;
    }

    public void selectAll(Handler<List<JsonObject>> callback){
        oracle.select("SELECT CODE, NAME, PARENTID, ICONURL," +
                " STATUS, NOTE, CONFIGDATA, SUBSTR(DEFAULTPARAM, 4000), HOST, PORT, URI" +
                " SERVICECODE, MERCHANT FROM SERVICE", callback);
    }
}
