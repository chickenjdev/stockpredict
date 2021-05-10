package predict.stock.utils;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import predict.stock.base.SPData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Utils {

  public static JsonArray readJsonArrayFromFile(String filename) {
    BufferedReader br = null;
    JsonArray jsonArray = null;
    try {

      try {
        br = new BufferedReader(new FileReader(filename));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      StringBuilder sb = new StringBuilder();
      String line = null;
      try {
        line = br.readLine();
        while (line != null) {
          sb.append(line);
          sb.append("\n");
          line = br.readLine();
        }
        String fullContent = sb.toString();
        jsonArray = new JsonArray(fullContent);

      } catch (IOException e) {
        e.printStackTrace();
      }

    } finally {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return jsonArray;
  }

  public static String createQueueName(String queue, String module){
    var rand = new Random().nextInt(1000);
    return String.join("_", "ha", "be", "qu", module, queue, String.valueOf(rand));
  }

  public static String createReplyQueueName(String queue, String module){
    var rand = new Random().nextInt(1000);
    return String.join("_", "ha", "be", "rl", module, queue, String.valueOf(rand));
  }

  public static String createExchangeName(String exchange, String module){
    var rand = new Random().nextInt(1000);
    return String.join("_", "ha", "be", "ex", module, exchange, String.valueOf(rand));
  }

  public static Buffer toBuffer(SPData data) {
    return Buffer.buffer(toBytes(data));
  }

  public static byte[] toBytes(SPData data) {
    try {
      return JsonObject.mapFrom(data).encode().getBytes();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static SPData fromBytes(byte[] bytes) {
    try {
      return new JsonObject(new String(bytes, Charset.forName(StandardCharsets.UTF_8.name()))).mapTo(SPData.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean isJSONArray(String source) {
    try {
      new JsonArray(source);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isJSON(String source) {
    try {
      new JsonObject(source);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean isNullOrEmpty(Object o) {
    if (o == null) {
      return true;
    } else if (o instanceof String) {
      String s1 = o.toString();
      return "".equals(s1);
    } else return false;
  }
}
