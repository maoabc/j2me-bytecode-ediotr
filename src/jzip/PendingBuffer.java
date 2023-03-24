package jzip;

class PendingBuffer {

    private byte buf[];
    private int start, end, bits, bitCount;

    public PendingBuffer(int size) {
        buf = new byte[size];
    }

    public void reset() {
        start = end = bitCount = bits = 0;
    }

    public void writeShortMSB(int s) {
        buf[end++] = (byte) (s >> 8);
        buf[end++] = (byte) s;
    }

    public boolean isFlushed() {
        return end == 0;
    }

    public void writeShort(int s) {
        buf[end++] = (byte) s;
        buf[end++] = (byte) (s >> 8);
    }

    public final void writeBlock(byte block[], int offset, int len) {
        System.arraycopy(block, offset, buf, end, len);
        end += len;
    }

    public int getBitCount() {
        return bitCount;
    }

    public void alignToByte() {
        if (bitCount > 0) {
            buf[end++] = (byte) bits;
            if (bitCount > 8) {
                buf[end++] = (byte) ((bits >> 8) & 0xff);
            }
        }
        bits = 0;
        bitCount = 0;
    }

    public void writeBits(int b, int count) {
       
        bits |= b << bitCount;
        bitCount += count;
        if (bitCount >= 16) {
            buf[end++] = (byte) bits;
            buf[end++] = (byte) (bits >> 8);
            bits = (bits >> 16) & 0xffff;
            bitCount -= 16;
        }
    }

    public int flush(byte output[], int offset, int length) {
        if (bitCount >= 8) {
            buf[end++] = (byte) bits;
            bits = (bits >> 8) & 0xffffff;
            bitCount -= 8;
        }
        if (length > end - start) {
            length = end - start;
            System.arraycopy(buf, start, output, offset, length);
            start = end = 0;
        } else {
            System.arraycopy(buf, start, output, offset, length);
            start += length;
        }
        return length;
    }
}

