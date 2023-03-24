package jzip;

class InflaterDynHeader {

    private static final int[] BL_ORDER = {16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15};
    private byte blLens[], litdistLens[];
    private InflaterHuffmanTree blTree;
    private int mode, lnum, dnum, blnum, num, repSymbol, ptr;
    private byte lastLen;

    public boolean decode(StreamManipulator input) {

        while (true) {
            switch (mode) {
                case 0:
                    lnum = input.peekBits(5);
                    if (lnum < 0) {
                        return false;
                    }
                    lnum += 257;
                    input.dropBits(5);

                    mode = 1;

                case 1:
                    dnum = input.peekBits(5);
                    if (dnum < 0) {
                        return false;
                    }
                    dnum++;
                    input.dropBits(5);

                    num = lnum + dnum;
                    litdistLens = new byte[num];
                    mode = 2;

                case 2:
                    blnum = input.peekBits(4);
                    if (blnum < 0) {
                        return false;
                    }
                    blnum += 4;
                    input.dropBits(4);
                    blLens = new byte[19];
                    ptr = 0;

                    mode = 3;

                case 3:
                    while (ptr < blnum) {
                        int len = input.peekBits(3);
                        if (len < 0) {
                            return false;
                        }
                        input.dropBits(3);

                        blLens[BL_ORDER[ptr]] = (byte) len;
                        ptr++;
                    }
                    blTree = new InflaterHuffmanTree(blLens, 19);
                    blLens = null;
                    ptr = 0;
                    mode = 4;

                case 4: {
                    int symbol;
                    while (((symbol = blTree.getSymbol(input)) & ~15) == 0) {

                        litdistLens[ptr++] = lastLen = (byte) symbol;

                        if (ptr == num) {

                            return true;
                        }
                    }
                    if (symbol < 0) {
                        return false;
                    }

                    if (symbol >= 17) {
                        lastLen = 0;
                    }
                    repSymbol = symbol - 16;
                    mode = 5;
                }


                case 5: {
                    int bits = repSymbol > 1 ? 7 : repSymbol + 2;
                    int count = input.peekBits(bits);
                    if (count < 0) {
                        return false;
                    }
                    input.dropBits(bits);
                    count += repSymbol > 1 ? 11 : 3;
                    while (count-- > 0) {
                        litdistLens[ptr++] = lastLen;
                    }

                    if (ptr == num) {
                        return true;
                    }

                }
                mode = 4;
            }
        }
    }

    public InflaterHuffmanTree buildLitLenTree() {
        byte litlenLens[] = new byte[lnum];
        System.arraycopy(litdistLens, 0, litlenLens, 0, lnum);
        return new InflaterHuffmanTree(litlenLens, lnum);
    }

    public InflaterHuffmanTree buildDistTree() {
        byte distLens[] = new byte[dnum];
        System.arraycopy(litdistLens, lnum, distLens, 0, dnum);
        return new InflaterHuffmanTree(distLens, dnum);
    }
}
