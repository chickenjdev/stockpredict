package predict.stock.log;

import predict.stock.base.SPData;

public interface SPLog {

    void info(SPData input, String message);

    void info(String message);

    void error(SPData input, String message, Throwable throwable);

    void error(SPData input, String message);

    void error(String message);

    void warn(SPData input, String message);

    void warn(String message);
}
