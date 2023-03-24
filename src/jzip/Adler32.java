package jzip;

public class Adler32 {

    public int checksum;

    public Adler32() {
        checksum = 1;
    }

    public void reset() {
        checksum = 1;
    }

    public void update(byte buf[], int off, int len) {

        int s1 = checksum & 0xffff;
        int s2 = checksum >>> 16;
        int n;
        while (len > 0) {
            len -= (n = Math.min(3800, len));
            while (--n >= 0) {
                s2 += (s1 += (buf[off++] & 0xFF));
            }
            s1 %= 65521;
            s2 %= 65521;
        }
        checksum = (s2 << 16) | s1;
    }
}
