package jzip;

import java.io.IOException;
import java.io.InputStream;

public class ContDataInputStream extends InputStream {

    private int offset;
    InputStream in;
    public byte[] bytes;
    private int bufoff, bufsize;

    public void setOffset(int off) {
        offset = off;
    }

    public ContDataInputStream(InputStream input) {
        in = input;
        offset = 0;
        bytes = new byte[64*1024];
        bufoff = 0;
        bufsize = 0;
        fillbuf();
    }

    public int getOffst() {
        return offset;
    }

    public boolean resetto(long off) throws IOException {
        if (off < offset - bufoff) {
            return false;
        }
        if (off >= offset) {
            skipto(off);
        } else {
            int back = (int) (offset - off);
            bufoff -= back;
            bufsize += back;
            offset = (int) off;
        }
        return true;
    }

    private void fillbuf() {
        if (bufsize > 0 && bufoff > 0) {
            System.arraycopy(bytes, bufoff, bytes, 0, bufsize);
        }
        bufoff = 0;
        int max = bytes.length;
        int l;
        try {
            while (bufsize < max && (l = in.read(bytes, bufsize, max - bufsize)) > 0) {
                bufsize += l;
            }
        } catch (Exception ex) {
        }
    }

    public void close() throws IOException {
        bytes = null;
        in.close();
    }

    public void readFully(byte[] bufer, int off, int len) throws IOException {
        if (bufsize > len) {
            System.arraycopy(bytes, bufoff, bufer, off, len);
            bufoff += len;
            bufsize -= len;
            offset += len;
            return;
        } else {
            if (bufsize > 0) {
                System.arraycopy(bytes, bufoff, bufer, off, bufsize);
                len -= bufsize;
                off += bufsize;
                offset += bufsize;
                bufoff = bufsize = 0;
            }
            int l;
            while (len > 0 && (l = in.read(bufer, off, len)) > 0) {
                offset += l;
                len -= l;
                off += l;
            }
            fillbuf();
        }
    }

    public long skip(long len) throws IOException {
        if (len <= 0) {
            return 0;
        }
        if (bufsize >= len) {
            bufoff += len;
            bufsize -= len;
            offset += len;
            return len;
        } else {
            int yuan = offset;
            int read = (int) len - bufsize;
            int l = 0;
            while (read > 0 && (l = in.read(bytes)) > 0) {
                read -= l;
                offset += l;
            }
            if (read < 0) {
                bufsize = -read;
                bufoff = l - bufsize;
                offset = (int) len + yuan;
                return len;
            } else {
                bufoff = bufsize = 0;
                fillbuf();
                offset = yuan + (int) len - read;
                return len - read;
            }
        }
    }

    public void skipto(long off) throws IOException {
        skip(off - offset);
    }

    public int read(byte[] addr, int off, int len) throws IOException {
        if (len <= bufsize) {
            readFully(addr, off, len);
            return len;
        } else {
            int l = bufsize;
            if (l > 0) {
                System.arraycopy(bytes, bufoff, addr, off, l);
                len -= l;
                off += l;
                bufoff = bufsize = 0;
            }
            l += in.read(addr, off, len);
            offset += l;
            fillbuf();
            return l;
        }
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int readInt() throws IOException {
        if (bufsize < 4) {
            fillbuf();
        }
        offset += 4;
        bufsize -= 4;
        return ((bytes[bufoff++] & 0xff) << 24) | ((bytes[bufoff++] & 0xff) << 16) | ((bytes[bufoff++] & 0xff) << 8) | (bytes[bufoff++] & 0xff);
    }

    public int readLshort() throws IOException {
        if (bufsize < 2) {
            fillbuf();
        }
        offset += 2;
        bufsize -= 2;
        return (bytes[bufoff++] & 0xff) | ((bytes[bufoff++] & 0xff) << 8);
    }

    public int readLint() throws IOException {
        if (bufsize < 4) {
            fillbuf();
        }
        offset += 4;
        bufsize -= 4;
        return (bytes[bufoff++] & 0xff) | ((bytes[bufoff++] & 0xff) << 8) | ((bytes[bufoff++] & 0xff) << 16) | ((bytes[bufoff++] & 0xff) << 24);
    }

    public byte readUnsignedByte() throws IOException {
        if (bufsize < 1) {
            fillbuf();
        }
        offset++;
        bufsize--;
        return (byte) (bytes[bufoff++] & 0xff);
    }

    public int read() throws IOException {
        if (bufsize < 1) {
            fillbuf();
        }
        offset++;
        bufsize--;
        return bytes[bufoff++];
    }
}
