package jzip;

public class StreamManipulator {

    private byte window[];
    private int window_start = 0;
    private int window_end = 0;
    private int buffer = 0;
    private int bits_in_buffer = 0;

    public final int peekBits(int n) {
        if (bits_in_buffer < n) {
            if (window_start == window_end) {
                return -1;
            }
            buffer |= (window[window_start++] & 0xff | (window[window_start++] & 0xff) << 8) << bits_in_buffer;
            bits_in_buffer += 16;
        }
        return buffer & ((1 << n) - 1);
    }

    public final void dropBits(int n) {
        //原: buffer>>>=n;
        buffer >>>= n;
        bits_in_buffer -= n;
    }

    public final int getBits(int n) {
        int bits;
        if ((bits = peekBits(n)) >= 0) {
            dropBits(n);
        }
        return bits;
    }

    public final int getAvailableBits() {
        return bits_in_buffer;
    }

    public final int getAvailableBytes() {
        return window_end - window_start + (bits_in_buffer >> 3);
    }

    public void skipToByteBoundary() {
        buffer >>= (bits_in_buffer & 7);
        bits_in_buffer &= ~7;
    }

    public final boolean needsInput() {
        return window_start == window_end;
    }

    public int copyBytes(byte output[], int offset, int length) {
        int count = 0;
        while (bits_in_buffer > 0 && length > 0) {
            output[offset++] = (byte) buffer;
            //原: buffer >>>= 8;
            buffer >>>= 8;
            bits_in_buffer -= 8;
            length--;
            count++;
        }
        if (length == 0) {
            return count;
        }
        length = Math.min(length, window_end - window_start);
        System.arraycopy(window, window_start, output, offset, length);
        window_start += length;

        if (((window_start - window_end) & 1) != 0) {
            buffer = (window[window_start++] & 0xff);
            bits_in_buffer = 8;
        }
        return count + length;
    }

    public void reset() {
        window_start = window_end = buffer = bits_in_buffer = 0;
    }

    public void setInput(byte buf[], int off, int len) {
        window_end = off + len;
        if ((len & 1) != 0) {
            buffer |= (buf[off++] & 0xff) << bits_in_buffer;
            bits_in_buffer += 8;
        }
        window = buf;
        window_start = off;
    }
}

