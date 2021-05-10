package predict.stock.utils;

import java.util.HashMap;
import java.util.Map;

public class MSISDNMapper {
    private static Map<String, String> mapping;
    private static Map<String, String> pmapping;

    static {
        mapping = new HashMap<>();
        mapping.put("070", "0120");
        mapping.put("079", "0121");
        mapping.put("077", "0122");
        mapping.put("076", "0126");
        mapping.put("078", "0128");
        mapping.put("083", "0123");
        mapping.put("084", "0124");
        mapping.put("085", "0125");
        mapping.put("081", "0127");
        mapping.put("082", "0129");
        mapping.put("032", "0162");
        mapping.put("033", "0163");
        mapping.put("034", "0164");
        mapping.put("035", "0165");
        mapping.put("036", "0166");
        mapping.put("037", "0167");
        mapping.put("038", "0168");
        mapping.put("039", "0169");
        mapping.put("056", "0186");
        mapping.put("058", "0188");
        mapping.put("059", "0199");
    }

    static {
        pmapping = new HashMap<>();
        pmapping.put("0120", "070");
        pmapping.put("0121", "079");
        pmapping.put("0122", "077");
        pmapping.put("0126", "076");
        pmapping.put("0128", "078");
        pmapping.put("0123", "083");
        pmapping.put("0124", "084");
        pmapping.put("0125", "085");
        pmapping.put("0127", "081");
        pmapping.put("0129", "082");
        pmapping.put("0162", "032");
        pmapping.put("0163", "033");
        pmapping.put("0164", "034");
        pmapping.put("0165", "035");
        pmapping.put("0166", "036");
        pmapping.put("0167", "037");
        pmapping.put("0168", "038");
        pmapping.put("0169", "039");
        pmapping.put("0186", "056");
        pmapping.put("0188", "058");
        pmapping.put("0199", "059");
    }

    public static String toOfficialMSISDN(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        String prefix = phone.substring(0, 4);
        if (!pmapping.containsKey(prefix)) {
            return phone;
        } else {
            return phone.replaceFirst(prefix, pmapping.get(prefix));
        }

    }

    public static String toMserviceMSISDN(String phone) {
        if (phone == null || phone.length() != 10) {
            return phone;
        }
        String prefix = phone.substring(0, 3);
        if (!mapping.containsKey(prefix)) {
            return phone;
        } else {
            return phone.replaceFirst(prefix, mapping.get(prefix));
        }

    }

    public static String globalToMServiceMSISDN(String global) {
        if (!global.startsWith("84")) {
            return global;
        }
        String localPhone = global.replaceFirst("84", "0");
        String finalPhone = toMserviceMSISDN(localPhone);
        return finalPhone;
    }
}
