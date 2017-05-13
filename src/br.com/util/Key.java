package util;

import org.apache.commons.codec.binary.Base64;
import service.BCrypt;

import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by MarioJ on 07/04/15.
 */
public class Key {

    private static char ALHPA[] = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static String generate(String cn, String phone) {
        return BCrypt.hashpw(getOriginalKey(cn, phone), BCrypt.gensalt());
    }

    public static String generateConfirmCode() {

        SecureRandom random = new SecureRandom();
        random.setSeed(new Date().getTime());

        String confirmCode = "";

        for (int i = 0; i < 11; i++)
            confirmCode += String.valueOf(ALHPA[random.nextInt(ALHPA.length)]);

        return confirmCode;
    }

    public static String encode(String cn, String phone) {
        return new String(Base64.encodeBase64((cn + phone).getBytes()));
    }

    public static String decode(byte[] data) {
        return new String(Base64.decodeBase64(data));
    }

    private static String getOriginalKey(String cn, String phone) {
        return (cn + phone) + new StringBuilder(cn + phone).reverse() + cn;
    }

}
