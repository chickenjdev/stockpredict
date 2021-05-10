package predict.stock.utils;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import predict.stock.errors.Errors;

import java.util.Map;

public class WebClientUtils {
    public static void callWebClientPartner(WebClient webClient, HttpMethod method, String url, String data, long timeout,
                                            Map<String, String> mapHeaders, Handler<JsonObject> handler) {
        JsonObject joResponse = new JsonObject();
        try {
            HttpRequest<Buffer> request = webClient.requestAbs(method, url);
            for (String key : mapHeaders.keySet()) {
                request.putHeader(key, mapHeaders.get(key));
            }
            request.timeout(timeout);
            request.sendJsonObject(new JsonObject(data), event -> {
                if (event.succeeded()) {
                    HttpResponse<Buffer> response = event.result();
                    int statusCode = response.statusCode();
                    if (statusCode != 408 && statusCode != 504 && statusCode != 200) {
                        joResponse.put(KeyUtils.Common.STATUS_CODE, response.statusCode());
                        joResponse.put(KeyUtils.Common.ERROR, Errors.CONNECTOR_ERROR);
                        joResponse.put(KeyUtils.Common.DATA, KeyUtils.Common.CONNECTOR_ERROR_PARTNER);
                        handler.handle(joResponse);
                        return;
                    } else if (statusCode != 200) {
                        joResponse.put(KeyUtils.Common.STATUS_CODE, statusCode);
                        joResponse.put(KeyUtils.Common.ERROR, Errors.IN_PROCESS);
                        joResponse.put(KeyUtils.Common.DATA, response.statusMessage());
                        handler.handle(joResponse);
                        return;
                    }
                    joResponse.put(KeyUtils.Common.STATUS_CODE, statusCode);
                    joResponse.put(KeyUtils.Common.ERROR, Errors.SUCCESS);
                    joResponse.put(KeyUtils.Common.DATA, response.bodyAsString());
                } else {
                    joResponse.put(KeyUtils.Common.STATUS_CODE, 1000);
                    joResponse.put(KeyUtils.Common.ERROR, Errors.IN_TIMEOUT);
                    joResponse.put(KeyUtils.Common.DATA, event.cause().getMessage());
                }
                handler.handle(joResponse);
            });
        } catch (Exception e) {
            joResponse.put(KeyUtils.Common.STATUS_CODE, 1000);
            joResponse.put(KeyUtils.Common.ERROR, Errors.SYSTEM_ERROR);
            joResponse.put(KeyUtils.Common.DATA, e.getMessage());
            handler.handle(joResponse);
        }
    }

    public static void callPartnerWithoutData(WebClient webClient, HttpMethod method, String url, long timeout,
                                              Map<String, String> mapHeaders, Handler<JsonObject> handler) {
        JsonObject joResponse = new JsonObject();
        try {
            HttpRequest<Buffer> request = webClient.requestAbs(method, url);
            for (String key : mapHeaders.keySet()) {
                request.putHeader(key, mapHeaders.get(key));
            }
            request.timeout(timeout);
            request.send(event -> {
                if (event.succeeded()) {
                    HttpResponse<Buffer> response = event.result();
                    int statusCode = response.statusCode();
                    if (statusCode != 408 && statusCode != 504 && statusCode != 200) {
                        joResponse.put(KeyUtils.Common.STATUS_CODE, response.statusCode());
                        joResponse.put(KeyUtils.Common.ERROR, Errors.CONNECTOR_ERROR);
                        joResponse.put(KeyUtils.Common.DATA, KeyUtils.Common.CONNECTOR_ERROR_PARTNER);
                        handler.handle(joResponse);
                        return;
                    } else if (statusCode != 200) {
                        joResponse.put(KeyUtils.Common.STATUS_CODE, statusCode);
                        joResponse.put(KeyUtils.Common.ERROR, Errors.IN_PROCESS);
                        joResponse.put(KeyUtils.Common.DATA, response.statusMessage());
                        handler.handle(joResponse);
                        return;
                    }
                    joResponse.put(KeyUtils.Common.STATUS_CODE, statusCode);
                    joResponse.put(KeyUtils.Common.ERROR, Errors.SUCCESS);
                    joResponse.put(KeyUtils.Common.DATA, response.bodyAsString());
                } else {
                    joResponse.put(KeyUtils.Common.STATUS_CODE, 1000);
                    joResponse.put(KeyUtils.Common.ERROR, Errors.IN_TIMEOUT);
                    joResponse.put(KeyUtils.Common.DATA, event.cause().getMessage());
                }
                handler.handle(joResponse);
            });
        } catch (Exception e) {
            joResponse.put(KeyUtils.Common.STATUS_CODE, 1000);
            joResponse.put(KeyUtils.Common.ERROR, Errors.SYSTEM_ERROR);
            joResponse.put(KeyUtils.Common.DATA, e.getMessage());
            handler.handle(joResponse);
        }
    }
}