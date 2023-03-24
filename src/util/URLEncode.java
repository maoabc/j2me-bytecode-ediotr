package util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


public class URLEncode {
    private static boolean[] dontNeedEncoding;
    static {
        dontNeedEncoding = new boolean[256];
        for (int i = 0; i < 256; i++) {
            boolean b = ((i >= '0') && (i <= '9')) || ((i >= 'A') && (i <= 'Z')) || ((i >= 'a') && (i <= 'z'));
            dontNeedEncoding[i] = b;
        }
        dontNeedEncoding[' '] = true;
        dontNeedEncoding['-'] = true;
        dontNeedEncoding['_'] = true;
        dontNeedEncoding['.'] = true;dontNeedEncoding['*'] = true;
    }
    public static String encode(String s) {
        boolean wroteUnencodedChar = false;
        StringBuffer writer = new StringBuffer();
        StringBuffer out = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < 256) && dontNeedEncoding[c]) {
                if (c == ' ') {
                    c = '+';
                }
                out.append((char) c);
                wroteUnencodedChar = true;
            } else {
                try {
                    if (wroteUnencodedChar) {
                        writer = new StringBuffer();
                        wroteUnencodedChar = false;
                    }
                    writer.append(c);
                    if (c >= 0xD800 && c <= 0xDBFF) {
                        if ((i + 1) < s.length()) {
                            int d = (int) (s.charAt(i + 1));
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                writer.append(d);
                                i++;
                            }
                        }
                    }
                } catch (Exception e) {
                    writer = new StringBuffer();
                    continue;
                }
                String str = writer.toString();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                try {
                    dos.writeUTF(str);
                    dos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] temp = baos.toByteArray();
                byte[] ba = new byte[temp.length - 2];
                for (int ix = 0; ix < ba.length; ix++) {
                    ba[ix] = temp[ix + 2];
                }
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = forDigit((ba[j] >> 4) & 0xF, 16);
                    out.append(ch);
                    ch = forDigit(ba[j] & 0xF, 16);
                    out.append(ch);
                }
                writer = new StringBuffer();
                try {
                    dos.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return out.toString();
    }
    private static char forDigit(int digit, int radix) {
        if ((digit >= radix) || (digit < 0)) {
            return '0';
        }
        if (digit < 10) {
            return (char) ('0' + digit);
        }
        return (char) ('A' + digit - 10);
    }
}