package cor.chrissy.community.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wx128
 * @createAt 2025/1/2
 */
public class Md5Util {
    private Md5Util() {
    }

    public static String encode(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return encode(bytes);
    }

    public static String encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length);
    }

    public static String encode(byte[] data, int offset, int len) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var5) {
            throw new RuntimeException(var5);
        }

        md.update(data, offset, len);
        byte[] secretBytes = md.digest();
        return getFormattedText(secretBytes);
    }

    private static String getFormattedText(byte[] src) {
        if (src != null && src.length != 0) {
            StringBuilder stringBuilder = new StringBuilder(32);

            for (byte b : src) {
                int v = b & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }
}
