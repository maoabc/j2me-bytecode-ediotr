package io;

import java.io.*;

public class BufferedOutputStream extends OutputStream {

    protected byte buf[];
    protected int count;
    OutputStream out;

    public BufferedOutputStream(OutputStream out) {
        this(out, 8192);
    }

    public BufferedOutputStream(OutputStream out, int size) {
        this.out = out;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buf = new byte[size];
    }

    private void flushBuffer() throws IOException {
        if (count > 0) {
            out.write(buf, 0, count);
            count = 0;
        }
    }

    public final void write(int b) throws IOException {
        if (count >= buf.length) {
            flushBuffer();
        }
        buf[count++] = (byte) b;
    }

    public final void write(byte b[], int off, int len) throws IOException {
        if (len >= buf.length) {
            flushBuffer();
            out.write(b, off, len);
            return;
        }
        if (len > buf.length - count) {
            flushBuffer();
        }
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    public void write(byte[] buf) throws IOException {
        write(buf, 0, buf.length);
    }

    public final void flush() throws IOException {
        flushBuffer();
        out.flush();
    }

    public void close() throws IOException {
        try {
            flush();
        } catch (IOException ignored) {
        }
        out.close();
    }

}
