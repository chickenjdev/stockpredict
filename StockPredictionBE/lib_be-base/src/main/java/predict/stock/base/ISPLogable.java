package predict.stock.base;

import com.google.inject.Injector;
import io.vertx.core.Vertx;
import predict.stock.log.SPLog;



public interface ISPLogable {

    Vertx getVertx();

    SPLog getLogger();

    Injector getInjector();

}
