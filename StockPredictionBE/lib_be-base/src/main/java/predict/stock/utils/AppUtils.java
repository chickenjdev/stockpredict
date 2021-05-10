package predict.stock.utils;

import com.google.common.base.Strings;
import com.mservice.proxy.entity.ProxyRequest;
import com.mservice.proxy.entity.ProxyResponse;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import predict.stock.base.SPData;
import predict.stock.config.spConfig;
import predict.stock.errors.ErrorDesc;
import predict.stock.errors.Errors;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeoutException;

public class AppUtils {
    public static void setError(SPData input, int error, spConfig spConfig){
        input.setErrorCode(error);
        input.setResult(false);
        setErrorDesc(input, error, spConfig);
    }

    public static void setResult(SPData input, int errCode, String errDesc) {
        errCode = Errors.mapCode(errCode);
        input.setErrorCode(errCode);
        input.setErrorDesc(errDesc);
    }

    public static void setResult(@Nonnull SPData input, int errCode, String errDesc, spConfig spConfig) {
        setResult(input, errCode, spConfig);
        if (!Strings.isNullOrEmpty(errDesc)) {
            input.setErrorDesc(errDesc);
        }
    }

    public static void setResult(@Nonnull SPData input, int errCode, spConfig spConfig) {
        errCode = Errors.mapCode(errCode);
        input.setResult(Errors.SUCCESS == errCode);
        input.setErrorCode(errCode);
        setErrorDesc(input, errCode, spConfig);
    }

    public static void setErrorDesc(SPData input, int errCode, spConfig spConfig){
        var errorCfg =  spConfig.getErrorByErrorCode(String.valueOf(errCode));
        if(errorCfg == null){
            input.setErrorDesc(ErrorDesc.getDescByVal(errCode));
            return;
        }
        if("vn".equalsIgnoreCase(input.getLang())){
            input.setErrorDesc(errorCfg.getExtContent());
            return;
        }
        input.setErrorDesc(errorCfg.getContent());
    }

    public static void setResponse(ProxyResponse response, ProxyRequest request){
        response.setRequest(request);
        response.setResultCode(Errors.SYSTEM_ERROR);
        response.setResultMessage(ErrorDesc.getDescByVal(response.getResultCode()));
    }

    public static void setResponse(ProxyResponse response, ProxyRequest request, int error){
        response.setRequest(request);
        response.setResultCode(error);
        response.setResultMessage(ErrorDesc.getDescByVal(error));
    }

    public static void setResponse(ProxyResponse response, ProxyRequest request, int error, String errorDesc){
        response.setRequest(request);
        response.setResultCode(error);
        response.setResultMessage(errorDesc);
    }

    public static void setResponse(SPData input, ProxyResponse response, ProxyRequest request, int error, String errorDesc){
        input.setResult(error == Errors.SUCCESS);
        response.setRequest(request);
        response.setResultCode(error);
        response.setResultMessage(errorDesc);
    }

    public static void setResponse(ProxyResponse response, ProxyRequest request, int errCode, Throwable throwable){
        if (throwable instanceof ReplyException) {
            ReplyException replyException = (ReplyException) throwable;
            if (ReplyFailure.TIMEOUT.equals(replyException.failureType())) {
                errCode = Errors.IN_PROCESS;
            }
        } else if (throwable instanceof TimeoutException) {
            errCode = Errors.IN_PROCESS;
        }
        response.setRequest(request);
        response.setResultCode(errCode);
        response.setResultMessage(ErrorDesc.getDescByVal(errCode));
    }
}
