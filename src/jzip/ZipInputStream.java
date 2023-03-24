package jzip;

import java.io.InputStream;
import java.io.IOException;

public class ZipInputStream extends InflaterInputStream {

    private CRC32 crc = new CRC32();
    private ZipEntry entry = null;
    private int csize, size, method, flags, avail;

    private boolean entryAtEOF;

    public ZipInputStream(InputStream in) {
        super(in, new Inflater(true), 10240);
    }

    private void fillBuf() throws IOException {
        avail = len = in.read(buf, 0, buf.length);
    }

    private int readBuf(byte out[], int offset, int length) throws IOException {
        if (avail <= 0) {
            fillBuf();
            if (avail <= 0) {
                return -1;
            }
        }
        if (length > avail) {
            length = avail;
        }
        System.arraycopy(buf, len - avail, out, offset, length);
        avail -= length;
        return length;
    }


    private void readFully(byte out[], int len) throws IOException {
        int off = 0, count = 0;
        while (len > 0) {
            off += (count = readBuf(out, off, len));
            len -= count;
        }
    }

    private int readLeByte() throws IOException {
        if (avail <= 0) {
            fillBuf();
            if (avail <= 0) {
                throw new IOException("EOF in header");
            }
        }
        return buf[len - avail--] & 0xff;
    }

    private int readLeShort() throws IOException {
        return readLeByte() | (readLeByte() << 8);
    }

    private int readLeInt() throws IOException {
        return readLeShort() | (readLeShort() << 16);
    }

    public ZipEntry getNextEntry() throws IOException {
        if (entry != null) {
            closeEntry();
        }

        int header = readLeInt();
        if (header == ('P' | ('K' << 8) | (1 << 16) | (2 << 24))) {
            close();
            return null;
        }
        if (header != ('P' | ('K' << 8) | (3 << 16) | (4 << 24))) {
            throw new IOException("Wrong Local header signature: " + Integer.toHexString(header));
        }
        /* skip version */
        readLeShort();//6
        flags = readLeShort();
        method = readLeShort();//10
        int dostime = readLeInt();
        int crcc = readLeInt();//18
        csize = readLeInt();
        size = readLeInt();//26
        int nameLen = readLeShort();
        int extraLen = readLeShort();//30
        byte buffer[] = new byte[nameLen];
        readFully(buffer, nameLen);
        entry = new ZipEntry(null);
        entry.setname(buffer);
        entryAtEOF = false;
        entry.setMethod(method);
        entry.flag = flags;
        if ((flags & 8) == 0) {
            entry.crc = crcc;
            entry.size = size;
            entry.compressedSize = csize;
        }
        entry.dostime = dostime;
        if (extraLen > 0) {
            byte extra[] = new byte[extraLen];
            readFully(extra, extraLen);
            entry.setExtra(extra);
        }

        if (method == 8 && avail > 0) {
            System.arraycopy(buf, len - avail, buf, 0, avail);
            len = avail;
            avail = 0;
            inf.setInput(buf, 0, len);
        }
        return entry;
    }

    private void readDataDescr() throws IOException {
        if (readLeInt() != ('P' | ('K' << 8) | (7 << 16) | (8 << 24))) {
            throw new IOException("Data descriptor signature not found");
        }
        entry.crc = readLeInt();
        csize = readLeInt();
        size = readLeInt();
        entry.size = size;
        entry.compressedSize = csize;
    }

    public void closeEntry() throws IOException {
        if (crc == null) {
            throw new IOException("Stream closed.");
        }
        if (entry == null) {
            return;
        }

        if (method == 8) {
            if ((flags & 8) != 0) {

                byte tmp[] = new byte[4096];
                while (read(tmp, 0, 4096) > 0) {
                }
                return;
            }
            csize -= inf.getTotalIn();
            avail = inf.getRemaining();
        }

        if (avail > csize && csize >= 0) {
            avail -= csize;
        } else {
            csize -= avail;
            avail = 0;
            while (csize > 0) {
                long skipped = in.skip(csize & 0xffffffffL);
                if (skipped <= 0) {
                    throw new IOException("zip archive ends early.");
                }
                csize -= skipped;
            }
        }

        size = 0;
        crc.reset();
        if (method == 8) {
            inf.reset();
        }
        entry = null;
        entryAtEOF = true;
    }

    public int available() {
        return entryAtEOF ? 0 : 1;
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (crc == null) {
            throw new IOException("Stream closed.");
        }
        if (entry == null) {
            return -1;
        }
        boolean finished = false;
        switch (method) {
            case 8:
                len = super.read(b, off, len);
                if (len < 0) {
                    if (!inf.finished()) {
                        throw new IOException("bad achive");
                    }
                    avail = inf.getRemaining();
                    if ((flags & 8) != 0) {
                        readDataDescr();
                    }

                    if (inf.getTotalIn() != csize || inf.getTotalOut() != size) {
                        throw new IOException("size mismatch: ");
                    }
                    inf.reset();
                    finished = true;
                }
                break;

            case 0:

                if (len > csize && csize >= 0) {
                    len = csize;
                }

                len = readBuf(b, off, len);
                if (len > 0) {
                    csize -= len;
                    size -= len;
                }
                if (csize == 0) {
                    finished = true;
                } else if (len < 0) {
                    throw new IOException("EOF in stored block");
                }
                break;
        }

        if (len > 0) {
            crc.update(b, off, len);
        }

        if (finished) {
            if ((int) crc.getValue() != entry.crc) {
                throw new IOException("CRC mismatch");
            }
            crc.reset();
            entry = null;
            entryAtEOF = true;
        }
        return len;
    }

    public void close() throws IOException {
        super.close();
        crc = null;
        entry = null;
        entryAtEOF = true;
    }
}
