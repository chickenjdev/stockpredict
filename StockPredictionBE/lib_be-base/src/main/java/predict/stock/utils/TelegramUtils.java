package predict.stock.utils;

import com.mservice.proxy.entity.ProxyRequest;
import com.mservice.proxy.entity.ProxyResponse;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import predict.stock.base.SPData;
import predict.stock.telegram.TelegramTracker;

public class TelegramUtils {
    private static final String SPLIT_CHARACTER = "|";

    public void sendTelegramWhenError(SPData input, ProxyRequest proxyRequest, ProxyResponse proxyResponse, JsonObject telegramCfg, Throwable throwable) {
        var info = input == null ? "" : getSplit(input.getUser()) + getSplit(input.getCmdId());
        var telegramTrackerMsg = TelegramTracker.buildTelegramMsg(info, proxyRequest, proxyResponse, throwable);
        TelegramTracker.trackEvent("TOPUP_ALERT", telegramTrackerMsg, telegramCfg);
    }

    public void sendTelegramWhenResponseError(ProxyRequest proxyRequest, ProxyResponse proxyResponse, JsonObject telegramCfg, Throwable throwable) {
        var info = proxyRequest.toString() == null ? "" : getSplit(proxyRequest.getInitiator()) + getSplit(proxyRequest.getRequestId());
        var telegramTrackerMsg = TelegramTracker.buildTelegramMsg(info, proxyRequest,proxyResponse, throwable);
        TelegramTracker.trackEvent("TOPUP_ALERT", telegramTrackerMsg, telegramCfg);
    }

    private static String getSplit(String input) {
        if (!StringUtil.isNullOrEmpty(input)) {
            return input + SPLIT_CHARACTER;
        }
        return "";
    }
}
