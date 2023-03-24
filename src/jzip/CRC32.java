package jzip;

public class CRC32{

    private int crc = 0;
    private static int[] crc_table;

    private static int[] make_crc_table() {
        int[] ctab = new int[256];
        for (int n = 0; n < 256; n++) {
            int c = n;
            for (int k = 8; --k >= 0;) {
                if ((c & 1) != 0) {
                    c = 0xedb88320 ^ (c >>> 1);
                } else {
                    c = c >>> 1;
                }
            }
            ctab[n] = c;
        }
        return ctab;
    }

    public CRC32() {
        if (crc_table == null) {
            crc_table = make_crc_table();
        }
    }


    public long getValue() {
        return (long) crc & 0xffffffffL;
    }

    public void reset() {
        crc = 0;
    }

    public void update(byte buf[], int off, int len) {
        int c = ~crc;
        while (--len >= 0) {
            c = crc_table[(c ^ buf[off++]) & 0xff] ^ (c >>> 8);
        }
        crc = ~c;
    }
}
