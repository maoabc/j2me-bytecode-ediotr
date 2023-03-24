package io;

import java.io.*;

public class ArrayOutputStream extends OutputStream {

    protected byte buf[];

    protected int count;

    private boolean isClosed = false;

    private void ensureOpen() {
        if (isClosed) {
            throw new RuntimeException("Writing to closed ByteArrayOutputStream");
        }
    }

    public ArrayOutputStream() {
        this(32);
    }

    public ArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                                               + size);
        }
        buf = new byte[size];
    }

    public final void write(int b) {
        ensureOpen();
        int newcount = count + 1;
        if (newcount > buf.length) {
            byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        buf[count] = (byte)b;
        count = newcount;
    }


    public final void write(byte b[], int off, int len) {
        ensureOpen();
        if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }


    public final void reset() {
        ensureOpen();
        count = 0;
    }


    public final byte toByteArray()[] {
        if (isClosed && buf.length == count) {
            return buf;
        } else {
            byte newbuf[] = new byte[count];
            System.arraycopy(buf, 0, newbuf, 0, count);
            return newbuf;
        }
    }

    public final byte[] getBufArray(){
        return buf;
    }



    public int size() {
        return count;
    }


    public String toString() {
      return new String(buf, 0, count);
    }

    public final void close() throws IOException {
        isClosed = true;
    }

}
