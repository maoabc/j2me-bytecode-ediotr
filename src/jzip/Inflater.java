package jzip;

public class Inflater {

    private static final int CPLENS[] = {
        3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31,
        35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258
    };
    private static final int CPLEXT[] = {
        0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2,
        3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0
    };
    private static final int CPDIST[] = {
        1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193,
        257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145,
        8193, 12289, 16385, 24577
    };
    private static final int CPDEXT[] = {
        0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6,
        7, 7, 8, 8, 9, 9, 10, 10, 11, 11,
        12, 12, 13, 13
    };
    private int mode, readAdler, neededBits, uncomprLen;
    private boolean isLastBlock;
    private int totalOut, totalIn, repLength, repDist;
    private boolean nowrap;
    private StreamManipulator input;
    private OutputWindow outputWindow;
    private InflaterDynHeader dynHeader;
    private InflaterHuffmanTree litlenTree, distTree;
    private Adler32 adler;

    public Inflater(boolean nohead) {
        nowrap = nohead;
        adler = new Adler32();
        input = new StreamManipulator();
        outputWindow = new OutputWindow();
        mode = nowrap ? 2 : 0;
    }

    public boolean finished() {
        return mode == 12 && outputWindow.getAvailable() == 0;
    }

    public int getRemaining() {
        return input.getAvailableBytes();
    }

    public int getTotalIn() {
        return totalIn - getRemaining();
    }

    public int getTotalOut() {
        return totalOut;
    }

    public int inflate(byte buf[], int off, int len) {

        if (len <= 0) {
            return 0;
        }

        int count = 0;
        int more;
        do {
            if (mode != 11) {
                more = outputWindow.copyOutput(buf, off, len);
                adler.update(buf, off, more);
                off += more;
                count += more;
                totalOut += more;
                len -= more;
                if (len == 0) {
                    return count;
                }
            }
        } while (decode() || (outputWindow.getAvailable() > 0 && mode != 11));
        return count;
    }

    public boolean needsDictionary() {
        return mode == 1 && neededBits == 0;
    }

    public boolean needsInput() {
        return input.needsInput();
    }

    public void reset() {
        mode = nowrap ? 2 : 0;
        totalIn = totalOut = 0;
        input.reset();
        outputWindow.reset();
        dynHeader = null;
        litlenTree = null;
        distTree = null;
        isLastBlock = false;
        adler.reset();
    }

    public void setInput(byte buf[], int off, int len) {
        input.setInput(buf, off, len);
        totalIn += len;
    }

    private boolean decodeHeader() {
        int header = input.peekBits(16);
        if (header < 0) {
            return false;
        }
        input.dropBits(16);
        header = ((header << 8) | (header >> 8)) & 0xffff;
        if ((header & 0x0020) == 0) {
            mode = 2;
        } else {
            mode = 1;
            neededBits = 32;
        }
        return true;
    }

    private boolean decodeDict() {
        while (neededBits > 0) {
            int dictByte = input.peekBits(8);
            if (dictByte < 0) {
                return false;
            }
            input.dropBits(8);
            readAdler = (readAdler << 8) | dictByte;
            neededBits -= 8;
        }
        return false;
    }

    private boolean decodeHuffman() {
        int free = outputWindow.getFreeSpace();
        while (free >= 258) {
            int symbol;
            switch (mode) {
                case 7:

                    while (((symbol = litlenTree.getSymbol(input)) & ~0xff) == 0) {
                        outputWindow.write(symbol);
                        if (--free < 258) {
                            return true;
                        }
                    }
                    if (symbol < 257) {
                        if (symbol < 0) {
                            return false;
                        } else {

                            distTree = null;
                            litlenTree = null;
                            mode = 2;
                            return true;
                        }
                    }
                    repLength = CPLENS[symbol - 257];
                    neededBits = CPLEXT[symbol - 257];
                case 8:
                    if (neededBits > 0) {
                        mode = 8;
                        int i = input.peekBits(neededBits);
                        if (i < 0) {
                            return false;
                        }
                        input.dropBits(neededBits);
                        repLength += i;
                    }
                    mode = 9;

                case 9:
                    symbol = distTree.getSymbol(input);
                    if (symbol < 0) {
                        return false;
                    }

                    repDist = CPDIST[symbol];
                    neededBits = CPDEXT[symbol];


                case 10:
                    if (neededBits > 0) {
                        mode = 10;
                        int i = input.peekBits(neededBits);
                        if (i < 0) {
                            return false;
                        }
                        input.dropBits(neededBits);
                        repDist += i;
                    }
                    outputWindow.repeat(repLength, repDist);
                    free -= repLength;
                    mode = 7;
                    break;

            }
        }
        return true;
    }

    private boolean decodeChksum() {
        while (neededBits > 0) {
            int chkByte = input.peekBits(8);
            if (chkByte < 0) {
                return false;
            }
            input.dropBits(8);
            readAdler = (readAdler << 8) | chkByte;
            neededBits -= 8;
        }
        mode = 12;
        return false;
    }

    private boolean decode() {
        switch (mode) {
            case 0:
                return decodeHeader();
            case 1:
                return decodeDict();
            case 11:
                return decodeChksum();

            case 2:
                if (isLastBlock) {
                    if (nowrap) {
                        mode = 12;
                        return false;
                    } else {
                        input.skipToByteBoundary();
                        neededBits = 32;
                        mode = 11;
                        return true;
                    }
                }

                int type = input.peekBits(3);

                if (type < 0) {
                    return false;
                }
                input.dropBits(3);

                if ((type & 1) != 0) {
                    isLastBlock = true;
                }
                switch (type >> 1) {
                    case 0:
                        input.skipToByteBoundary();
                        mode = 3;
                        break;
                    case 1:
                        litlenTree = InflaterHuffmanTree.defLitLenTree;
                        distTree = InflaterHuffmanTree.defDistTree;
                        mode = 7;
                        break;
                    case 2:
                        dynHeader = new InflaterDynHeader();
                        mode = 6;
                        break;

                }
                return true;

            case 3: {
                if ((uncomprLen = input.peekBits(16)) < 0) {
                    return false;
                }
                input.dropBits(16);
                mode = 4;
            }

            case 4: {
                int nlen = input.peekBits(16);
                if (nlen < 0) {
                    return false;
                }
                input.dropBits(16);

                mode = 5;
            }
            case 5: {
                int more = outputWindow.copyStored(input, uncomprLen);
                uncomprLen -= more;
                if (uncomprLen == 0) {
                    mode = 2;
                    return true;
                }
                return !input.needsInput();
            }

            case 6:
                if (!dynHeader.decode(input)) {
                    return false;
                }
                litlenTree = dynHeader.buildLitLenTree();
                distTree = dynHeader.buildDistTree();
                mode = 7;
            case 7:
            case 8:
            case 9:
            case 10:
                return decodeHuffman();
            case 12:
                return false;

        }
        return false;
    }
}
