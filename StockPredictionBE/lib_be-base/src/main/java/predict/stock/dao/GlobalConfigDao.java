package predict.stock.dao;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import predict.stock.oracle.SPOracle;


public class GlobalConfigDao {

    public SPOracle oracle;

    public GlobalConfigDao(SPOracle SPOracle){
        this.oracle = SPOracle;
    }

    public void selectGlobalConfig(Handler<List<JsonObject>> callback){
        oracle.select("SELECT *" +
                        " FROM GLOBAL_CONFIG WHERE TYPE IN (?,?,?,?,?,?) AND ISDELETED = ?",
                new JsonArray().add("TOPUP_VERTICLE_CFG")
                .add("TOPUP_MISC").add("TOPUP_NOTI").add("TOPUP_ERROR_DESC").add("TOPUP_SPECIAL_AGENT").add("TOPUP_PROVIDER")
                .add(0), callback);
    }
}
