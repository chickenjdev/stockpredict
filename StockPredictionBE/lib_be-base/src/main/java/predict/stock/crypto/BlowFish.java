package predict.stock.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BlowFish {
    public static String encrypt(String to_encrypt, String strkey) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(to_encrypt.getBytes()));
        } catch (Exception e) { return null; }
    }

    public static String decrypt(String to_decrypt, String strkey) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(to_decrypt.getBytes(StandardCharsets.UTF_8)));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        System.out.println(encrypt("hello123", "23a9e537-115b-418a-ae0f-2526a2b48e82-123"));
        System.out.println(decrypt("cWSVBG/rl3L2ykns6qSBqw==", "23a9e537-115b-418a-ae0f-2526a2b48e82-123"));
    }
}
