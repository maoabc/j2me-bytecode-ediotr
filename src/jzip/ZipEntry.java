package jzip;

import java.util.Calendar;
import java.util.Date;

public class ZipEntry  {

    public int dostime, size, compressedSize, offset, crc;
    public short method;
    public String name;
    public int flag;

    public byte comment[] = null;
    private byte extra[] = null;
    private byte namebytes[] = null;
    public int extralen, namelen, commentlen;

    public ZipEntry(String n) {
        if (n != null) {
            try {
                name = n.replace('\\', '/');
                namebytes = name.getBytes("Utf-8");
                namelen = namebytes.length;
            } catch (Exception ex) {
            }
        }
    }
    public int getSize(){
        return size;
    }


    public void setname(byte n[]) {
        for (int i = 0; i < n.length; i++) {
            if (n[i] == 92) 
                n[i] = 47;
        }
        namelen = n.length;
        namebytes = n;
        char buf[] = new char[n.length + 1];
        if (CharDecoder.decodeUtf8(n, 0, n.length, buf, 0) != n.length) {
            CharDecoder.decodeGbk(n, 0, n.length, buf, 0);
        }
        name = new String(buf, 0, buf[n.length]);
    }

    public void setTime(long time) {
        Calendar w = Calendar.getInstance();
        if (time > 0) {
            w.setTime(new Date(time));
        }
        dostime = (w.get(1) - 1980) << 25 | (w.get(2) + 1) << 21 | (w.get(5)) << 16 | (w.get(11)) << 11 | (w.get(12)) << 5 | (w.get(13)) >> 1;
    }

    public void setMethod(int method) {
        this.method = (short) method;
    }

    public int getMethod() {
        return method;
    }

    public byte[] getNamebytes() {
        return namebytes;
    }

    public void setExtra(byte extra[]) {
        if (extra == null) {
            this.extra = null;
            extralen = 0;
            return;
        }
        this.extra = extra;
        extralen = extra.length;
    }

    public byte[] getExtra() {
        return extra;
    }

    public boolean isDirectory() {
        return name.endsWith("/");
    }

    public String toString() {
        return name;
    }
}
