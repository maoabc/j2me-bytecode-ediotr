package jzip;

import java.io.InputStream;
import java.io.IOException;

public class InflaterInputStream extends InputStream{

    protected Inflater inf;
    protected InputStream in;
    protected byte buf[];
    protected int len, bufsize;

    public InflaterInputStream(InputStream input, Inflater inflate, int size) {
        in = input;
        len = 0;
        inf = inflate;
        buf = new byte[size];
        bufsize = size;
    }

    public int available() {
        return inf.finished() ? 0 : 1;
    }

    public synchronized void close() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }

    }

    protected void fill() throws IOException {
        len = in.read(buf, 0, bufsize);
        inf.setInput(buf, 0, len);
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int count;
        while (true) {
            count = inf.inflate(b, off, len);
            if (count > 0) {
                return count;
            }
            if (inf.needsDictionary() | inf.finished()) {
                return -1;
            } else if (inf.needsInput()) {
                fill();
            }
        }
    }

    public int read() {
        return 0;
    }
}
