package predict.stock.verticle;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import predict.stock.log.SPLog;
import predict.stock.obj.GlobalCfg;

import java.util.List;

public abstract class WebServiceRouter extends SPVerticle {

    HttpServer httpServer;

    @Inject
    SPLog SPLog;

    @Override
    protected void initVerticle(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise) {
        httpServer = vertx.createHttpServer();
        init(verticleCfg)
                .onComplete(asyncResult -> startRouter(verticleCfg, config, startPromise));
    }

    protected void startRouter(List<GlobalCfg> verticleCfg, JsonObject config, Promise<Void> startPromise){
        var port = config.getInteger("port", 2021);
        httpServer.requestHandler(createRouters(verticleCfg, config))
                .listen(port);
        startPromise.complete();
    }

    protected Future<Void> init(List<GlobalCfg> verticleCfg){
        var promise = Promise.<Void>promise();
        initRouter(verticleCfg, promise);
        return promise.future();
    }

    protected  abstract void initRouter(List<GlobalCfg> verticleCfg, Promise<Void> promise);

    protected abstract Router buildRouters(List<GlobalCfg> verticleCfg, JsonObject config, Router router);

    protected Router createRouters(List<GlobalCfg> verticleCfg, JsonObject config){
        var router = Router.router(vertx);
        var options = new JWTAuthOptions();
        var auth = config.getJsonObject("auth");
        options.addPubSecKey(new PubSecKeyOptions(auth));
        /*JWTAuth authProvider = JWTAuth.create(vertx, options);
        router.route("/*").handler(JWTAuthHandler.create(authProvider)).failureHandler(
                event ->{
                    event.request()
                            .response().setStatusCode(401).setStatusMessage("Error Decrypt")
                            .write(new JsonObject().put("errorCode", 401)
                                    .put("errorDesc", "decrypt error").toString()).end();
                    try {
                        spLog.error("FAILED DESCRYPT: " + event.user().principal().toString());
                    } catch (Exception ex){
                        spLog.error("FAILED DESCRYPT: " + ex.getMessage());
                    }
                });*/
        return buildRouters(verticleCfg, config, router);
    }

}
