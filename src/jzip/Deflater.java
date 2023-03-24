package jzip;

public class Deflater {

    private int level, state, totalOut;
    private boolean noHeader;
    private PendingBuffer pending;
    private DeflaterEngine engine;
    static int num = 0;

    public Deflater(int lvl, boolean nowrap) {
        pending = new PendingBuffer(0x10000);
        engine = new DeflaterEngine(pending);
        noHeader = nowrap;
        engine.setStrategy(0);
        setLevel(lvl);
        reset();
    }

    public void reset() {
        state = (noHeader ? 0x10 : 0);
        totalOut = 0;
        pending.reset();
        engine.reset();
    }

    public int getTotalOut() {
        return totalOut;
    }

    void flush() {
        state |= 4;
    }

    public void finish() {
        state |= 4 | 8;
    }

    public boolean finished() {
        return state == 0x1e && pending.isFlushed();
    }

    public boolean needsInput() {
        return engine.needsInput();
    }

    public void setInput(byte input[], int off, int len) {
        engine.setInput(input, off, len);
    }

    public void setLevel(int lvl) {
        if (level != lvl) {
            level = lvl;
            engine.setLevel(lvl);
        }
    }

    private void writeHead() {
        int header = 30720;
        int level_flags = (level - 1) >> 1;
        if (level_flags < 0 || level_flags > 3) {
            level_flags = 3;
        }
        header |= level_flags << 6;
        if ((state & 1) != 0) {
            header |= 0x20;
        }
        header += 31 - (header % 31);
        pending.writeShortMSB(header);
        if ((state & 1) != 0) {
            int chksum = engine.getAdler();
            engine.resetAdler();
            pending.writeShortMSB(chksum >> 16);
            pending.writeShortMSB(chksum & 0xffff);
        }
        state = 0x10 | (state & (4 | 8));
    }

    public int deflate(byte output[], int offset, int length) {
        int origLength = length;
        if (state < 16) {
            writeHead();
        }
        int count;
        while (true) {
            count = pending.flush(output, offset, length);
            offset += count;
            totalOut += count;
            length -= count;
            if (length == 0 || state == 30) {
                break;
            }

            if (!engine.deflate((state & 4) != 0, (state & 8) != 0)) {
                if (state == 16) {
                    return origLength - length;
                } else if (state == 20) {
                    if (level != 0) {
                        count = 8 + ((-pending.getBitCount()) & 7);
                        while (count > 0) {
                            pending.writeBits(2, 10);
                            count -= 10;
                        }
                    }
                    state = 16;
                } else if (state == 28) {
                    pending.alignToByte();
                    if (!noHeader) {
                        count = engine.getAdler();
                        pending.writeShortMSB(count >> 16);
                        pending.writeShortMSB(count & 0xffff);
                    }
                    state = 30;
                }
            }
        }
        return origLength - length;
    }
}
