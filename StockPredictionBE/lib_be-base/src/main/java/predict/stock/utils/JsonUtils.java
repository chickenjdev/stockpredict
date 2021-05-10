package predict.stock.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Strings;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonUtils {
    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static ObjectMapper mapper;
    private static ObjectMapper mapperThin;
    private static ObjectMapper oracleMapper;
    private static ObjectMapper oracleNoTimezoneMapper;
    private static Matcher matcher;
    private static Pattern p;

    static {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);

        mapperThin = new ObjectMapper(mapper.getFactory());
        mapperThin.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapperThin.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperThin.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        oracleMapper = new ObjectMapper();
        oracleMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        oracleMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        oracleMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        oracleMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        oracleMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        SimpleModule oracleModule = new SimpleModule("PolymorphicTestObjectDeserializer", new Version(1, 0, 0, null, "com.sportslabs.amp", "spring-social-bootstrap"));

        oracleMapper.registerModule(oracleModule);

        oracleNoTimezoneMapper = oracleMapper.copy();
        oracleNoTimezoneMapper.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        oracleNoTimezoneMapper.registerModule(oracleModule);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static <T> T jsonToObj(String object, Class<T> type) {
        return jsonToObj(object, type, false);
    }

    public static <T> T jsonToObj(JsonObject object, Class<T> type) {
        return jsonToObj(object, type, false);
    }


    public static <T> T jsonToObj(String object, Class<T> type, boolean isDisableStackTrace) {
        try {
            return mapper.readValue(object, type);
        } catch (Exception e) {
            if (isDisableStackTrace) {
                log.error("error jsonToObj: " + e.getMessage());
            } else {
                log.error("error jsonToObj:" + object, e);
            }
            return null;
        }
    }

    public static <T> T jsonToObj(JsonObject object, Class<T> type, boolean isDisableStackTrace) {
        try {
            return mapper.readValue(object.encode(), type);
        } catch (Exception e) {
            if (isDisableStackTrace) {
                log.error("error jsonToObj: " + e.getMessage());
            } else {
                log.error("error jsonToObj:" + object, e);
            }
            return null;
        }
    }

    /**
     * Convert Json To Obj for Oracle
     *
     * @param object
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T jsonToObjForOracle(JsonObject object, Class<T> type) {
        try {
            return oracleMapper.readValue(object.encode(), type);
        } catch (Exception e) {
            log.error("error jsonToObjForOracle:" + object, e);
            return null;
        }
    }

    /**
     * Convert Json String To Obj for Oracle
     *
     * @param objectString
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T jsonToObjForOracle(String objectString, Class<T> type) {
        try {
            return oracleMapper.readValue(objectString, type);
        } catch (Exception e) {
            log.error("error jsonToObjForOracle:" + objectString, e);
            return null;
        }
    }

    /**
     * Convert Json To Obj List for Oracle
     *
     * @param objectList
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToObjListForOracle(List<JsonObject> objectList, Class<T> type) {
        List<T> result = new ArrayList<>();

        if (objectList == null || objectList.size() <= 0) return result;

        try {
            for (JsonObject object : objectList) {
                result.add(oracleMapper.readValue(object.encode(), type));
            }
        } catch (Exception e) {
            log.error("error jsonToObjListForOracle:" + objectList, e);
            return null;
        }

        return result;
    }

    public static <T> List<T> jsonToObjListForOracleRemove_(List<JsonObject> objectList, Class<T> type) {
        List<T> result = new ArrayList<>();

        if (objectList == null || objectList.size() <= 0) return result;

        try {
            for (JsonObject object : objectList) {
                result.add(oracleMapper.readValue(object.encode().replaceAll("_", ""), type));
            }
        } catch (Exception e) {
            log.error("error jsonToObjListForOracleRemove_:" + objectList, e);
            return null;
        }

        return result;
    }

    public static String convertToString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            log.error("error objToString: " + e.getMessage(), e);
            return "";
        }
    }

    public static String objToString(Object o) {
        try {
            JsonObject j = new JsonObject(mapper.writeValueAsString(o));
            try {
                j.remove("fullExtra");
            } catch (Exception e) {
            }
            return j.encode();
        } catch (Exception e) {
            log.error("error objToString: " + e.getMessage(), e);
            return null;
        }
    }

    public static String convertObjToString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("error convertObjToString: " + e.getMessage(), e);
            return "";
        }
    }

    public static JsonObject objToJsonObj(Object o) {
        try {
            return new JsonObject(mapper.writeValueAsString(o));
        } catch (Exception e) {
            log.error("error objToJsonObj: " + e.getMessage(), e);
            return null;
        }
    }

    public static Map<String, Object> objToMap(Object obj) {
        JsonObject jo = JsonUtils.objToJsonObj(obj);
        return jo != null ? jo.getMap() : null;
    }

    public static <T> T mapToObj(Map<String, String> map, Class<T> type) {
        return mapper.convertValue(map, type);
    }

    public static boolean isJSON(String source) {
        try {
            new JsonObject(source);
            return true;
        } catch (Exception e) {
            log.warn("not a valid json:" + source);
            return false;
        }
    }

    public static final Optional<JsonObject> getJSON(String source) {
        try {
            JsonObject json = new JsonObject(source);
            return Optional.of(json);
        } catch (Exception e) {
            log.warn("not a valid json:" + source);
            return Optional.empty();
        }
    }

    public static boolean isJSONArray(String source) {
        try {
            new JsonArray(source);
            return true;
        } catch (Exception e) {
            log.warn("not a valid json array:" + source);
            return false;
        }
    }

    public static final Optional<JsonArray> getJSONArray(String source) {
        try {
            JsonArray array = new JsonArray(source);
            return Optional.of(array);
        } catch (Exception e) {
            log.warn("not a valid json array:" + source);
            return Optional.empty();
        }
    }

    public static boolean isValidJsonObjectAndData(String data) {
        boolean isValid = isJSON(data);
        if (isValid) {
            return !new JsonObject(data).isEmpty();
        }
        return isValid;
    }

    public static boolean isValidJsonArrayAndData(String data) {
        boolean isValid = Utils.isJSONArray(data);
        if (isValid) {
            return !new JsonArray(data).isEmpty();
        }
        return isValid;
    }


    private static void maskCardNumber(JsonObject result) {
        if (result.containsKey("cardInfo")) {
            JsonObject cardInfo = result.getJsonObject("cardInfo");
            if (cardInfo.containsKey("cardNumber")) {
                cardInfo.put("cardNumber", "****************");
                result.put("cardInfo", cardInfo);
            }
        }
    }

    public static JsonObject objToJsonObjUpper(Object o) {
        try {
            return new JsonObject(oracleMapper.writeValueAsString(o));
        } catch (Exception e) {
            log.error("error objToJsonObj: " + e.getMessage(), e);
            return null;
        }
    }

    public static Map<String, Object> objToMapUpper(Object obj) {
        JsonObject jo = JsonUtils.objToJsonObjUpper(obj);
        return jo != null ? jo.getMap() : null;
    }


    public static List<String> joArrayToListString(JsonArray jsonArray){
        return jsonArray.stream().map(Object::toString).collect(Collectors.toList());
    }

    public static <T> List<T> jaToObj(String object, TypeReference<List<T>> type) {
        try {
            return mapper.readValue(object, type);
        } catch (Exception e) {
            log.error("error jaToObj:" + object, e);
            return null;
        }
    }

    public static JsonObject getSimpleJson(JsonObject complexJsonObject) {
        JsonObject joSimple = new JsonObject();

        parseComplexJsonObjectToSimpleJsonObject(joSimple, complexJsonObject);

        return joSimple;
    }

    public static JsonObject getSimpleJson(Object complexObject) {
        JsonObject joSimple = new JsonObject();

        parseComplexJsonObjectToSimpleJsonObject(joSimple, JsonUtils.objToJsonObj(complexObject));

        return joSimple;
    }

    private static void parseComplexJsonObjectToSimpleJsonObject(JsonObject joSimple, JsonObject complexJsonObject) {
        if (complexJsonObject == null) {
            return;
        }
        String prefixKey = getPrefixKey(complexJsonObject);
        Set<String> setKeys = complexJsonObject.fieldNames().stream().map(item -> prefixKey + item).collect(Collectors.toSet());

        for (String key : setKeys) {
            Object value = complexJsonObject.getValue(key);
            if (value != null && value.toString().startsWith("{") && value.toString().endsWith("}")) {
                parseComplexJsonObjectToSimpleJsonObject(joSimple, new JsonObject(value.toString().trim()));
            } else {
                joSimple.put(key, value);
            }
        }
    }

    private static String getPrefixKey(JsonObject complexJsonObject) {
        String[] arrPathClass = complexJsonObject.getString("_class", "").split(Pattern.quote("."));
        complexJsonObject.remove("_class");
        return Strings.isNullOrEmpty(arrPathClass[arrPathClass.length - 1]) ?
                arrPathClass[arrPathClass.length - 1] : arrPathClass[arrPathClass.length - 1] + ".";
    }

    public static JsonObject parseJson(Object obj, JsonObject def) {
        if (obj == null) {
            return def;
        }
        try {
            return new JsonObject(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    // Database date for which doesnt need to be converted to GMT+7
    public static <T> List<T> jsonToObjListForOracleGMT0Timezone(List<JsonObject> objectList, Class<T> type) {
        List<T> result = new ArrayList<>();
        if (objectList == null) {
            return result;
        }

        try {
            for (JsonObject object : objectList) {
                result.add(oracleNoTimezoneMapper.readValue(object.encode(), type));
            }
        } catch (Exception e) {
            log.error("error jsonToObjListForOracleGMT0Timezone:" + objectList, e);
            return null;
        }

        return result;
    }
}
