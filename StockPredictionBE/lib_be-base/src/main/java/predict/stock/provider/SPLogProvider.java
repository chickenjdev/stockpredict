package predict.stock.provider;

import com.google.inject.Provider;
import predict.stock.log.SPLogImpl;
import predict.stock.log.SPLog;

public class SPLogProvider implements Provider<SPLog> {

    @Override
    public SPLog get() {
        return new SPLogImpl();
    }
}
