package predict.stock.verticle;

import io.prometheus.client.CollectorRegistry;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;
import predict.stock.healthcheck.MetricsHandlerImpl;
import predict.stock.obj.GlobalCfg;

import java.util.List;

public class HealthCheckRouter extends WebServiceRouter {

    private HealthCheckHandler prometheusHandler;
    private HealthCheckHandler healthChecks;

    @Override
    protected void initRouter(List<GlobalCfg> verticleCfg, Promise<Void> promise) {
        prometheusHandler = new MetricsHandlerImpl(getVertx(), new CollectorRegistry());
        healthChecks = HealthCheckHandler.create(getVertx());
        promise.complete();
    }

    @Override
    protected Router buildRouters(List<GlobalCfg> verticleCfg, JsonObject config, Router router) {
        router.route("/health").handler(healthChecks);
        router.route("/health/metrics").handler(prometheusHandler);
        return router;
    }
}
