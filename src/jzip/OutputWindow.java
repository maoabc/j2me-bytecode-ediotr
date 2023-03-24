package jzip;

class OutputWindow {

    private byte window[] = new byte[32768];
    private int window_end = 0;
    private int window_filled = 0;

    public void write(int abyte) {
        window_filled++;
        window[window_end++] = (byte) abyte;
        window_end &= 32767;
    }

    private final void slowRepeat(int rep_start, int len, int dist) {
        while (len-- > 0) {
            window[window_end++] = window[rep_start++];
            window_end &= 32767;
            rep_start &= 32767;
        }
    }

    public void repeat(int len, int dist) {
        window_filled += len;
        int rep_start = (window_end - dist) & 32767;
        int border = 32768 - len;
        if (rep_start <= border && window_end < border) {
            if (len <= dist) {
                System.arraycopy(window, rep_start, window, window_end, len);
                window_end += len;
            } else {
                while (len-- > 0) {
                    window[window_end++] = window[rep_start++];
                }
            }
        } else {
            slowRepeat(rep_start, len, dist);
        }
    }

    public int copyStored(StreamManipulator input, int len) {
        len = Math.min(Math.min(len, 32768 - window_filled), input.getAvailableBytes());
        int copied;
        int tailLen = 32768 - window_end;
        if (len > tailLen) {
            copied = input.copyBytes(window, window_end, tailLen);
            if (copied == tailLen) {
                copied += input.copyBytes(window, 0, len - tailLen);
            }
        } else {
            copied = input.copyBytes(window, window_end, len);
        }

        window_end = (window_end + copied) & 32767;
        window_filled += copied;
        return copied;
    }

    public void copyDict(byte dict[], int offset, int len) {

        if (len > 32768) {
            offset += len - 32768;
            len = 32768;
        }
        System.arraycopy(dict, offset, window, 0, len);
        window_end = len & 32767;
    }

    public int getFreeSpace() {
        return 32768 - window_filled;
    }

    public int getAvailable() {
        return window_filled;
    }

    public int copyOutput(byte output[], int offset, int len) {
        int copy_end = window_end;
        if (len > window_filled) {
            len = window_filled;
        } else {
            copy_end = (window_end - window_filled + len) & 32767;
        }

        int copied = len;
        int tailLen = len - copy_end;

        if (tailLen > 0) {
            System.arraycopy(window, 32768 - tailLen, output, offset, tailLen);
            offset += tailLen;
            len = copy_end;
        }
        System.arraycopy(window, copy_end - len, output, offset, len);
        window_filled -= copied;
        return copied;
    }

    public void reset() {
        window_filled = window_end = 0;
    }
}



