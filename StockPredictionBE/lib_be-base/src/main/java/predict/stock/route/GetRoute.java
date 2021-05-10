package predict.stock.route;

import com.google.common.hash.Hashing;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import predict.stock.base.SPData;
import predict.stock.crypto.BlowFish;
import predict.stock.obj.GlobalCfg;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class GetRoute extends BaseRoute {

    @Override
    void executeRequest(SPData input, JsonObject headers, String body, HttpServerRequest request, HttpServerResponse httpResponse,
                        Handler<JsonObject> response) {

        try {
            if(!validateSign(input, headers, request)){
                httpResponse.setStatusCode(401).setChunked(true).end();
                return;
            }

            var params = request.params();
            executeGetRequest(input, params, response);
        } catch (Exception ex) {
            SPLog.error("Get Exception method " + request.path());
            httpResponse.setStatusCode(401).setChunked(true).end();
        }
    }

    protected boolean validateSign(SPData input, JsonObject headers, HttpServerRequest request){
        if(true){
            //TODO implement code validate sign with app
            return true;
        }
        var isEncryped = isEnrypted(headers);
        if (isEncryped) {
            //TODO decrypt data
        }
        var validReq = validateRequest(input, Optional.ofNullable(request.query()).orElse(""), headers);

        return false;
    }

    protected boolean validateRequest(SPData input, String data, JsonObject headers) {
        var lastSessionKey = Optional.ofNullable(getRawLastSessionKey(input.getLastSessionKey()))
                .orElse("");
        var sub = getSubHash(String.valueOf(input.getAgentId()), data, Optional.ofNullable(headers.getString("M-Timestamp")).orElse(""), lastSessionKey);
        var hash = getHash(data, sub, lastSessionKey);
        return hash.equalsIgnoreCase(headers.getString("M-Signature"));
    }

    private boolean isEnrypted(JsonObject headers) {
        boolean isEncrypt = false;
        try {
            isEncrypt = Optional.ofNullable(headers.getString("M-IsEncrypt"))
                    .map(Boolean::parseBoolean).orElse(false);
        } catch (Exception ex) {
            SPLog.error("parse isEncrypt got exception " + ex.getMessage());
        }
        return isEncrypt;
    }

    private String getRawLastSessionKey(String sessionKey) {
        var key = Optional.ofNullable(spConfig.getMiscByName("KEY_ENCRYPT_JWT"))
                .map(GlobalCfg::getContent).orElse(null);
        if (key == null) return sessionKey;
        return BlowFish.decrypt(sessionKey, key);
    }


    private String getSubHash(String agentId, String path, String timestamp, String lastSessionKey) {
        var key = String.join("", agentId, path, timestamp, lastSessionKey);
        return Hashing.hmacSha256(lastSessionKey.getBytes())
                .hashString(key, StandardCharsets.UTF_8).toString();
    }

    private String getHash(String data, String subHash, String lastSessionKey) {
        var key = String.join("", data, subHash);
        return Hashing.hmacSha256(lastSessionKey.getBytes())
                .hashString(key, StandardCharsets.UTF_8).toString();
    }

    protected abstract void executeGetRequest(SPData input, MultiMap params, Handler<JsonObject> response);
}
