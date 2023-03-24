package jzip;

import java.io.IOException;
import java.io.InputStream;

public class CharDecoder {

    static byte[] GBKbyte;

    public static int decodeGbk(byte[] b, int srcoff, int length, char[] sb, int destoff) {
     //   System.out.println("destoff=" + destoff + "  srcoff=" + srcoff + " length=" + length + " b.length=" + b.length);
        if (GBKbyte == null) {
            initGBK();
        }
        int index = 0;
        int now = srcoff;
        length += srcoff;
        int c = 0;
        while (now < length) {
            c = b[now++] & 0xff;
            if ((c & 128) == 0) {
                sb[destoff++] = (char) c;
                continue;
            }
            if (length <= now) {
                now--;
                break;
            }
            index = ((c << 9) | ((b[now++] & 0xff) << 1)) - 65536;
            sb[destoff++] = (char) (((GBKbyte[index] & 0xff) << 8) | (GBKbyte[index + 1] & 0xff));
        }
        sb[sb.length - 1] = (char) destoff;
        return now;
    }

    public static int decodeUnicodeBig(byte[] b, int srcoff, int length, char[] sb, int destoff) {

        int now = srcoff;
        length += srcoff;
        if (length % 2 != 0) {
            length--;
        }
        while (now < length) {
            sb[destoff++] = (char) (((b[now++] & 0xff) << 8) | b[now++] & 0xff);
        }
        sb[sb.length - 1] = (char) destoff;
        return now;
    }

    public static int decodeUnicode(byte[] b, int srcoff, int length, char[] sb, int destoff) {

        int now = srcoff;
        length += srcoff;

        if (length % 2 != 0) {
            length--;
        }
        while (now < length) {
            sb[destoff++] = (char) ((b[now++] & 0xff) | (b[now++] & 0xff) << 8);
        }
        sb[sb.length - 1] = (char) destoff;
        return now;
    }

    public static int decodeUtf8(byte[] b, int srcoff, int length, char[] sb, int destoff) {
        int now = srcoff;
        length += srcoff;
        int c = 0;
        while (now < length) {
            // 0xxx xxxx
            c = b[now++] & 0xFF;
            if ((c & 128) == 0) {
                sb[destoff++] = (char) c;
                continue;
            }
            if (now == length || (c & 64) == 0) {
                now--;
                break;
            }
            // 110x xxxx 10xx xxxx
            if ((c & 32) == 0) {
                sb[destoff++] = (char) (((c & 0x1F) << 6) | (b[now++] & 0x3F));
                continue;
            }
            if (now == length - 1) {
                now--;
                break;
            }
            // 1110 xxxx 10xx xxxx 10xx xxxx
            sb[destoff++] = (char) (((c & 0x0F) << 12) | ((b[now++] & 0x3F) << 6) | (b[now++] & 0x3F));
        }
        sb[sb.length - 1] = (char) destoff;
        return now;
    }

    private static void initGBK() {
        try {
            InputStream in = "".getClass().getResourceAsStream("/chen/GBK");
            GBKbyte = new byte[in.available()];
            in.read(GBKbyte);
            in.close();
        } catch (IOException ex) {
        }

    }
}
