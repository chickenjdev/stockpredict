package predict.stock.telegram;

import com.mservice.proxy.entity.ProxyRequest;
import com.mservice.proxy.entity.ProxyResponse;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.vertx.core.json.JsonObject;
import predict.stock.msg.TelegramTrackerMsg;
import predict.stock.utils.KeyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class TelegramTracker {

    private static final Logger logger = LogManager.getLogger(TelegramTracker.class);
    private static Map<String, TelegramBot> mapBot = new HashMap<>();

    private static TelegramBot getBot(String token) {
        if (!mapBot.containsKey(token)) {
            mapBot.put(token, new TelegramBot(token));
        }
        return mapBot.get(token);
    }

    public static void trackEvent(String trackerType, TelegramTrackerMsg telegramTrackerMsg, JsonObject config) {
        var joConfigOfType = config.getJsonObject(trackerType, new JsonObject());
        if (joConfigOfType.isEmpty()) {
            return;
        }

        if (!joConfigOfType.getBoolean("active")) {
            return;
        }

        var message = buildMessage(telegramTrackerMsg);
        var token = joConfigOfType.getString("token");
        var chatId = joConfigOfType.getString("chatId");
        var patternIgnore = config.getString("pattern_ignore", "");

        if (message.matches(patternIgnore)) {
            return;
        }

        var sendMessage = new SendMessage(chatId, message);
        getBot(token).execute(sendMessage, new Callback<>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                logger.info(String.format("send request is %s | %s.", response.message(), response.isOk()));
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                logger.error("Send request to telegram fail", e);
            }
        });
    }

    private static String buildMessage(TelegramTrackerMsg telegramTrackerMsg) {
        var sb = new StringBuilder();
        sb.append(telegramTrackerMsg.getTitle()).append("\n").append("\n");
        for (int i = 0; i < telegramTrackerMsg.getKeys().size(); i++) {
            sb.append(telegramTrackerMsg.getKeys().get(i)).append(": ").append(telegramTrackerMsg.getValues().get(i)).append("\n");
        }
        sb.append("Date/Time: ").append(telegramTrackerMsg.getTime().toString());
        return sb.toString();
    }

    public static TelegramTrackerMsg buildTelegramMsg(String info, ProxyRequest proxyRequest, ProxyResponse proxyResponse, Throwable throwable) {
        var telegramTrackerMsg = new TelegramTrackerMsg();
        telegramTrackerMsg.setTitle("************** ERROR **************");
        telegramTrackerMsg.addContent("Module", KeyUtils.SystemInfo.MODULE_NAME);
        telegramTrackerMsg.addContent("-----------------------------------\nDETAILS", "");
        telegramTrackerMsg.addContent("Info", info);
        telegramTrackerMsg.addContent("TransId", String.valueOf(proxyRequest.getCoreTransId()));
        telegramTrackerMsg.addContent("Ref1/Amount", String.join(" / ", proxyRequest.getReference1(), String.valueOf(proxyRequest.getAmount())));
        telegramTrackerMsg.addContent("ServiceCode", proxyRequest.getServiceCode());
        telegramTrackerMsg.addContent("Store", proxyRequest.getExtraValue(KeyUtils.Common.SUB_SERVICE).toString());
        telegramTrackerMsg.addContent("ErrorCode", String.valueOf(proxyResponse.getResultCode()));
        telegramTrackerMsg.addContent("sp - Message", proxyResponse.getResultMessage());
        telegramTrackerMsg.addContent("Partner - Message", proxyResponse.getPartnerResponseMessage());
        var throwMess = Optional.ofNullable(throwable).map(Throwable::getMessage).orElse("Blank throw");
        var throwStack = Optional.ofNullable(throwable).map(Throwable::getStackTrace);
        telegramTrackerMsg.addContent("ExceptionMessage", throwMess + "\n");
        if (throwStack.isPresent()) {
            telegramTrackerMsg.addContent("StackTrace", Arrays.toString(throwable.getStackTrace()) + "\n");
        }
        return telegramTrackerMsg;
    }
}