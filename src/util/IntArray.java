package util;

public class IntArray {

    protected int buf[];

    protected int count;


    public IntArray(int size) {
        buf = new int[size];
    }

    public IntArray() {
        this(32);
    }

    public final void add(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            int newbuf[] = new int[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        buf[count] = b;
        count = newcount;
    }

    public final int[] toIntArray() {
        if ( buf.length == count) {
            return buf;
        } else {
            int newbuf[] = new int[count];
            System.arraycopy(buf, 0, newbuf, 0, count);
            return newbuf;
        }
    }

}
