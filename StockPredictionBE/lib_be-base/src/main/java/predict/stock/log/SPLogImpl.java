package predict.stock.log;

import com.google.common.base.Strings;

import com.google.inject.Inject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import predict.stock.base.SPData;

import javax.annotation.Nullable;
import java.util.Optional;

public class SPLogImpl implements SPLog {

    Logger logger;
    private static final String SPLIT_CHARACTER = "|";

    @Inject
    public SPLogImpl(){
        logger = LoggerFactory.getLogger(this.getClass());
    }

    private String basicLog(@Nullable SPData spData, String message){
        if(spData == null){
            return message;
        }

        var requestId = Optional.ofNullable(spData.getRequest().getRequestId()).filter(s -> !Strings.isNullOrEmpty(s))
                .orElse("n/a");
        var user = Optional.ofNullable(spData.getUser()).filter(s -> !Strings.isNullOrEmpty(s))
                .orElse("n/a");
        var cmdId = Optional.ofNullable(spData.getCmdId()).filter(s -> !Strings.isNullOrEmpty(s))
                .orElse("n/a");
        var errorDesc = Optional.ofNullable(spData.getErrorDesc()).filter(s -> !Strings.isNullOrEmpty(s))
                .orElse("n/a");
        return String.join(SPLIT_CHARACTER, user, requestId, cmdId, String.valueOf(spData.getErrorCode()), errorDesc, message);
    }

    @Override
    public void info(SPData input, String message) {
        logger.info(basicLog(input, message));
    }

    @Override
    public void info(String message) {
        info(null, message);
    }

    @Override
    public void error(SPData input, String message, Throwable throwable) {
        logger.error(basicLog(new SPData(), message), throwable);
    }

    @Override
    public void error(SPData input, String message) {
        logger.error(basicLog(input, message));
    }

    @Override
    public void error(String message) {
        error(null, message);
    }

    @Override
    public void warn(SPData input, String message) {
        logger.warn(basicLog(input, message));
    }

    @Override
    public void warn(String message) {
        warn(null, message);
    }
}
