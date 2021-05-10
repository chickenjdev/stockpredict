package predict.stock.route;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import predict.stock.base.SPData;
import predict.stock.config.spConfig;
import predict.stock.log.SPLog;
import predict.stock.obj.GlobalCfg;
import predict.stock.utils.MSISDNMapper;

import java.util.Optional;

public abstract class BaseRoute implements Handler<RoutingContext> {

    @Inject
    protected SPLog SPLog;

    @Inject
    protected spConfig spConfig;

    @Inject
    private Vertx vertx;
    @Override
    public void handle(RoutingContext context) {
        var request = context.request();
        var header = Optional.ofNullable(context.user()).map(User::principal).orElse(new JsonObject());
        request.bodyHandler(body -> {
            try {
                var data = Optional.ofNullable(body).map(String::valueOf).orElse(null);
                var input = Optional.ofNullable(header).map(this::getInput)
                        .orElse(null);
                var httpResponse = request.response();
                SPLog.info("Received request " + data);
                executeRequest(input, header, data, request, httpResponse, handler ->
                        request
                                .response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .setChunked(true)
                                .write(buildResponse(input, handler).encode())
                                .end());
            } catch (Exception ex){
                SPLog.error("Exception " + ex.getMessage());
                request.exceptionHandler(Throwable::printStackTrace);
            }
        });

        request.exceptionHandler(Throwable::printStackTrace);
    }

    private JsonObject buildResponse(SPData input, JsonObject responseData){
        if(input == null){
            return new JsonObject();
        }
        return new JsonObject().put("user", input.getUser())
                .put("result", input.getResult())
                .put("errorCode", input.getErrorCode())
                .put("errorDesc", input.getErrorDesc())
                .put("data", responseData);
    }

    private SPData getInput(JsonObject header){
        var input = new SPData();
        try {
            var user = header.getString("user");
            input.setspUser(MSISDNMapper.toMserviceMSISDN(user));
            input.setUser(user);
            input.setImei(Optional.ofNullable(header.getString("imei")).orElse(""));
            input.setAgentId(Optional.ofNullable(header.getInteger("agent_id")).orElse(0));
            input.setBankCode(Optional.ofNullable(header.getString("BANK_CODE")).orElse(""));
            input.setBankName(Optional.ofNullable(header.getString("BANK_NAME")).orElse(""));
            input.setValidWalletConfirm(Optional.ofNullable(header.getInteger("VALID_WALLET_CONFIRM")).orElse(-1));
            input.setMapSacomCard(Optional.ofNullable(header.getInteger("MAP_SACOM_CARD")).orElse(-1));
            input.setName(Optional.ofNullable(header.getString("NAME")).orElse(""));
            input.setIdentify(Optional.ofNullable(header.getString("IDENTIFY")).orElse(""));
            input.setDeviceOs(Optional.ofNullable(header.getString("DEVICE_OS")).orElse(""));
            input.setAppVer(Optional.ofNullable(header.getInteger("APP_VER")).orElse(-1));
            input.setLastSessionKey(Optional.ofNullable(header.getString("sessionKey")).orElse(""));
        } catch (NullPointerException exception){
            throw new NullPointerException(exception.getMessage());
        }
        return input;
    }

    abstract void executeRequest(SPData input, JsonObject headers, String body, HttpServerRequest request,
                                 HttpServerResponse httpResponse, Handler<JsonObject> response);

    protected void sendEventBus(String routeName, SPData input, Handler<AsyncResult<Message<SPData>>> handler){
        int timeout = Optional.ofNullable(spConfig.getVerticlesByName(routeName))
                .filter(globalCfg -> "EVENT_BUS_SENT_TIMEOUT".equalsIgnoreCase(globalCfg.getSubKey()))
                .map(GlobalCfg::getContent).map(Integer::parseInt).orElse(120000);
        if(handler == null) vertx.eventBus().send(routeName, input, new DeliveryOptions().setSendTimeout(timeout));
        vertx.eventBus().request(routeName, input, new DeliveryOptions().setSendTimeout(timeout), handler);
    }


}
