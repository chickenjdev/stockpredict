package predict.stock;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.spi.VertxMetricsFactory;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.*;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.micrometer.backends.PrometheusBackendRegistry;
import io.vertx.micrometer.impl.VertxMetricsFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.EnumSet;

public class SPLauncher extends Launcher {

    private static final Logger logger = LogManager.getLogger(SPLauncher.class);


    public static void main(String[] args) {
        new SPLauncher().dispatch(args);
    }

    @Override
    public void beforeStartingVertx(VertxOptions options) {
        options.setMetricsOptions(buildMetricsOptitions());
        options.setHAEnabled(true);
        bindAndPublishMetrics();
    }

    private static MicrometerMetricsOptions buildMetricsOptitions() {
        MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
                .setMicrometerRegistry(Metrics.globalRegistry)
                .setEnabled(true)
                //work around for issue: https://github.com/vert-x3/vertx-micrometer-metrics/issues/38
                .addLabels(Label.EB_ADDRESS)
                .addLabels(Label.EB_ADDRESS, Label.POOL_NAME, Label.POOL_TYPE)
                .addLabelMatch(new Match()
                        .setDomain(MetricsDomain.EVENT_BUS)
                        .setType(MatchType.REGEX)
                        .setLabel("address")
                        .setValue("^\\d+$")
                        .setAlias("reply-address"));

        metricsOptions.setDisabledMetricsCategories(EnumSet.allOf(MetricsDomain.class));
        VertxMetricsFactory vertxMetricsFactory = new VertxMetricsFactoryImpl();
        BackendRegistries.setupBackend(metricsOptions);
        metricsOptions.setFactory(vertxMetricsFactory);
        return metricsOptions;
    }

    private static void bindAndPublishMetrics() {
        Metrics.addRegistry(getPrometheusBackendRegistry(true));
        Iterable<Tag> tags = Collections.singletonList(Tag.of("server", "localhost"));

        new JvmMemoryMetrics(tags).bindTo(Metrics.globalRegistry);
        new JvmGcMetrics(tags).bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics(tags).bindTo(Metrics.globalRegistry);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        //Deploy HTTP Server healthcheck
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(createRouters(vertx))
                .listen(2022);
    }

    @Override
    public void beforeStoppingVertx(Vertx vertx) {
        vertx.deploymentIDs().forEach(vertx::undeploy);
    }

    protected Router createRouters(Vertx vertx){
        var router = Router.router(vertx);
        router.route("/liveness").handler(event ->
                event.response().setChunked(true).setStatusCode(200).end());
        router.route("/readiness").handler(createHealthCheck(vertx));
        router.route("/terminate").handler(event ->
                vertx.close(result ->
                        event.response().setStatusCode(200).setChunked(true).end()));
        return router;
    }

    private HealthCheckHandler createHealthCheck(Vertx vertx){
        HealthCheckHandler hc = HealthCheckHandler.create(vertx);
        hc.register("dummy-health-check", future -> future.complete(Status.OK()));
        return hc;
    }

    private static MeterRegistry getPrometheusBackendRegistry(boolean isPublishQuantiles) {
        VertxPrometheusOptions vertxPrometheusOptions = new VertxPrometheusOptions();
        vertxPrometheusOptions.setEnabled(true)
                .setStartEmbeddedServer(true)
                .setEmbeddedServerOptions(new HttpServerOptions().setPort(2332))
                .setPublishQuantiles(isPublishQuantiles)
                .setEmbeddedServerEndpoint("/metrics/vertx");
        logger.info("Metrics export port : " + 2332 + " - Path : /metrics/vertx");
        PrometheusBackendRegistry prometheusBackendRegistry = new PrometheusBackendRegistry(vertxPrometheusOptions);
        prometheusBackendRegistry.init();
        return prometheusBackendRegistry.getMeterRegistry();
    }
}
