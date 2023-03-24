package jzip;

class DeflaterEngine {

    final static int GOOD_LENGTH[] = {0, 4, 4, 4, 4, 8, 8, 8, 32, 32};
    final static int MAX_LAZY[] = {0, 4, 5, 6, 4, 16, 16, 32, 128, 258};
    final static int NICE_LENGTH[] = {0, 8, 16, 32, 16, 32, 128, 128, 258, 258};
    final static int MAX_CHAIN[] = {0, 4, 8, 32, 16, 32, 128, 256, 1024, 4096};
    final static int COMPR_FUNC[] = {0, 1, 1, 1, 1, 2, 2, 2, 2, 2};
    private byte window[], inputBuf[];
    private short head[], prev[];
    private int ins_h, matchStart, matchLen;
    private boolean prevAvailable;
    private int blockStart, strstart, lookahead;
    private int strategy, max_chain, max_lazy, niceLength, goodLength;
    private int comprFunc, inputOff, inputEnd;
    private PendingBuffer pending;
    private DeflaterHuffman huffman;
    private Adler32 adler;

    DeflaterEngine(PendingBuffer pen) {
        pending = pen;
        huffman = new DeflaterHuffman(pending);
        adler = new Adler32();
        window = new byte[65536];
        head = new short[32768];
        prev = new short[32768];
        blockStart = strstart = 1;
    }

    public void reset() {
        blockStart = strstart = 1;
        lookahead = 0;
        prevAvailable = false;
        matchLen = 2;
        for (int i = 32767; i >= 0; i--) {
            head[i] = 0;
            prev[i] = 0;
        }
        huffman.reset();
        adler.reset();
    }

    public void resetAdler() {
        adler.reset();
    }

    public int getAdler() {
        return adler.checksum;
    }

    public void setStrategy(int strat) {
        strategy = strat;
    }

    public void setLevel(int lvl) {
        goodLength = GOOD_LENGTH[lvl];
        max_lazy = MAX_LAZY[lvl];
        niceLength = NICE_LENGTH[lvl];
        max_chain = MAX_CHAIN[lvl];

        if (COMPR_FUNC[lvl] != comprFunc) {

            switch (comprFunc) {
                case 0:
                    if (strstart > blockStart) {
                        huffman.flushStoredBlock(window, blockStart,
                                strstart - blockStart, false);
                        blockStart = strstart;
                    }
                    updateHash();
                    break;
                case 1:
                    if (strstart > blockStart) {
                        huffman.flushBlock(window, blockStart, strstart - blockStart, false);
                        blockStart = strstart;
                    }
                    break;
                case 2:
                    if (prevAvailable) {
                        huffman.tallyLit(window[strstart - 1] & 0xff);
                    }
                    if (strstart > blockStart) {
                        huffman.flushBlock(window, blockStart, strstart - blockStart, false);
                        blockStart = strstart;
                    }
                    prevAvailable = false;
                    matchLen = 2;
                    break;
            }
            comprFunc = COMPR_FUNC[lvl];
        }
    }

    private void updateHash() {

        ins_h = (window[strstart] << 5) ^ window[strstart + 1];
       
    }

    private int insertString() {
        short match;
        int hash = ((ins_h << 5) ^ window[strstart + (3 - 1)]) & 32767;
        prev[strstart & 32767] = match = head[hash];
        head[hash] = (short) strstart;
        ins_h = hash;
      
        return match & 0xffff;
    }

    private void slideWindow() {
        System.arraycopy(window, 32768, window, 0, 32768);
        matchStart -= 32768;
        strstart -= 32768;
        blockStart -= 32768;
        int m;
        for (int i = 0; i < 32768; i++) {
            m = head[i] & 0xffff;
            head[i] = m >= 32768 ? (short) (m - 32768) : 0;
            m = prev[i] & 0xffff;
            prev[i] = m >= 32768 ? (short) (m - 32768) : 0;
        }
    }

    private void fillWindow() {
        if (strstart >= 65274) {
            slideWindow();
        }
        int more;
        while (lookahead < 262 && inputOff < inputEnd) {
            more = Math.min(65536 - lookahead - strstart, inputEnd - inputOff);
            System.arraycopy(inputBuf, inputOff, window, strstart + lookahead, more);
            adler.update(inputBuf, inputOff, more);
            inputOff += more;
            lookahead += more;
         
        }
        if (lookahead > 2) {
            updateHash();
        }
    }

    private boolean findLongestMatch(int curMatch) {
        int chainLength = max_chain;
        int nicelen = niceLength;
        short preve[] = prev;
        int scan = strstart;
        int match;
        int best_end = strstart + matchLen;
        int best_len = Math.max(matchLen, 2);
        int limit = Math.max(strstart - 32506, 0);
        int strend = scan + 257;
        byte scan_end1 = window[best_end - 1];
        byte scan_end = window[best_end];
        if (best_len >= goodLength) {
            chainLength >>= 2;
        }

        if (nicelen > lookahead) {
            nicelen = lookahead;
        }
        do {

            if (window[curMatch + best_len] != scan_end || window[curMatch + best_len - 1] != scan_end1 || window[curMatch] != window[scan] || window[curMatch + 1] != window[scan + 1]) {
                continue;
            }

            match = curMatch + 2;
            scan += 2;

            while (window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && window[++scan] == window[++match] && scan < strend);

            if (scan > best_end) {
                matchStart = curMatch;
                best_end = scan;
                best_len = scan - strstart;
                if (best_len >= nicelen) {
                    break;
                }

                scan_end1 = window[best_end - 1];
                scan_end = window[best_end];
            }
            scan = strstart;
        } while ((curMatch = (preve[curMatch & 32767] & 0xffff)) > limit && --chainLength != 0);

        matchLen = Math.min(best_len, lookahead);
        return matchLen >= 3;
    }

    private boolean deflateStored(boolean flush, boolean finish) {
        if (!flush && lookahead == 0) {
            return false;
        }
        strstart += lookahead;
        lookahead = 0;
        int storedLen = strstart - blockStart;
        if ((storedLen >= 65531) || (blockStart < 32768 && storedLen >= 32506) || flush) {
            boolean lastBlock = finish;
            if (storedLen > 65531) {
                storedLen = 65531;
                lastBlock = false;
            }
            huffman.flushStoredBlock(window, blockStart, storedLen, lastBlock);
            blockStart += storedLen;
            return !lastBlock;
        }
        return true;
    }

    private boolean deflateFast(boolean flush, boolean finish) {
        if (lookahead < 262 && !flush) {
            return false;
        }

        while (lookahead >= 262 || flush) {
            if (lookahead == 0) {
                huffman.flushBlock(window, blockStart, strstart - blockStart, finish);
                blockStart = strstart;
                return false;
            }

            if (strstart > 65274) {
                slideWindow();
            }

            int hashHead;
            if (lookahead >= 3 && (hashHead = insertString()) != 0 && strategy != 2 && strstart - hashHead <= 32506 && findLongestMatch(hashHead)) {
                huffman.tallyDist(strstart - matchStart, matchLen);
                lookahead -= matchLen;
                if (matchLen <= max_lazy && lookahead >= 3) {
                    while (--matchLen > 0) {
                        strstart++;
                        insertString();
                    }
                    strstart++;
                } else {
                    strstart += matchLen;
                    if (lookahead >= 3 - 1) {
                        updateHash();
                    }
                }
                matchLen = 3 - 1;
                continue;
            } else {
                /* No match found */
                huffman.tallyLit(window[strstart] & 0xff);
                strstart++;
                lookahead--;
            }

            if (huffman.isFull()) {
                boolean lastBlock = finish && lookahead == 0;
                huffman.flushBlock(window, blockStart, strstart - blockStart,lastBlock);
                blockStart = strstart;
                return !lastBlock;
            }
        }
        return true;
    }

    private boolean deflateSlow(boolean flush, boolean finish) {

        if (lookahead < 262 && !flush) {
            return false;
        }

        while (lookahead >= 262 || flush) {
           
            if (lookahead == 0) {
                if (prevAvailable) {
                    huffman.tallyLit(window[strstart - 1] & 0xff);
                }
                prevAvailable = false;
                huffman.flushBlock(window, blockStart, strstart - blockStart, finish);
                blockStart = strstart;
                return false;
            }

            if (strstart >= 2 * 32768 - 262) {
               slideWindow();
            }

            int prevMatch = matchStart;
            int prevLen = matchLen;
            if (lookahead >= 3) {
                int hashHead = insertString();
                if (strategy != 2 && hashHead != 0 && strstart - hashHead <= 32506 && findLongestMatch(hashHead)) {

                    if (matchLen <= 5 && (strategy == 1 || (matchLen == 3 && strstart - matchStart > 4096))) {
                        matchLen = 2;
                    }
                }
            }


            if (prevLen >= 3 && matchLen <= prevLen) {

                huffman.tallyDist(strstart - 1 - prevMatch, prevLen);
                prevLen -= 2;
                do {
                    strstart++;
                    lookahead--;
                    if (lookahead >= 3) {
                        insertString();
                    }
                } while (--prevLen > 0);
                strstart++;
                lookahead--;
                prevAvailable = false;
                matchLen = 2;
            } else {
                if (prevAvailable) {
                    huffman.tallyLit(window[strstart - 1] & 0xff);
                }
                prevAvailable = true;
                strstart++;
                lookahead--;
            }

            if (huffman.isFull()) {
                int len = strstart - blockStart;
                if (prevAvailable) {
                    len--;
                }
                boolean lastBlock = (finish && lookahead == 0 && !prevAvailable);
                huffman.flushBlock(window, blockStart, len, lastBlock);
                blockStart += len;
                return !lastBlock;
            }
        }
        return true;
    }

    void setInput(byte buf[], int off, int len) {
        inputBuf = buf;
        inputOff = off;
        inputEnd = off + len;
    }

    boolean needsInput() {
        return inputEnd == inputOff;
    }

    boolean deflate(boolean flush, boolean finish) {

        boolean progress = false;
        do {
            fillWindow();
            boolean canFlush = flush && inputOff == inputEnd;
            switch (comprFunc) {

                case 0:
                    progress = deflateStored(canFlush, finish);
                    break;
                case 1:
                    progress = deflateFast(canFlush, finish);
                    break;
                case 2:
                    progress = deflateSlow(canFlush, finish);
                    break;

            }
        } while (pending.isFlushed() && progress);

        return progress;
    }
}
