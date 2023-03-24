package jzip;

class DeflaterHuffman {

    private static final byte BL_ORDER[] = {16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15};
    private final static byte bit4Reverse[] = {0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15};
    private static short staticLCodes[], staticDCodes[];
    private static byte staticLLength[], staticDLength[];
    private PendingBuffer pending;
    private HuffmanTree literalTree, distTree, blTree;
    private short d_buf[];
    private byte l_buf[];
    private int last_lit, extra_bits;

    static short bitReverse(int value) {
        return (short) (bit4Reverse[value & 0xf] << 12 | bit4Reverse[(value >> 4) & 0xf] << 8 | bit4Reverse[(value >> 8) & 0xf] << 4 | bit4Reverse[value >> 12]);
    }

    static {
        staticLCodes = new short[286];
        staticLLength = new byte[286];
        int i = 0;
        while (i < 144) {
            staticLCodes[i] = bitReverse((0x030 + i) << 8);
            staticLLength[i++] = 8;
        }
        while (i < 256) {
            staticLCodes[i] = bitReverse((0x190 - 144 + i) << 7);
            staticLLength[i++] = 9;
        }
        while (i < 280) {
            staticLCodes[i] = bitReverse((0x000 - 256 + i) << 9);
            staticLLength[i++] = 7;
        }
        while (i < 286) {
            staticLCodes[i] = bitReverse((0x0c0 - 280 + i) << 8);
            staticLLength[i++] = 8;
        }
        staticDCodes = new short[30];
        staticDLength = new byte[30];
        for (i = 0; i < 30; i++) {
            staticDCodes[i] = bitReverse(i << 11);
            staticDLength[i] = 5;
        }

    }

    public DeflaterHuffman(PendingBuffer pen) {

        pending = pen;
        literalTree = new HuffmanTree(286, 257, 15, pending);
        distTree = new HuffmanTree(30, 1, 15, pending);
        blTree = new HuffmanTree(19, 4, 7, pending);
        d_buf = new short[16384];
        l_buf = new byte[16384];

    }

    public void reset() {

        last_lit = 0;
        extra_bits = 0;
        literalTree.reset();
        distTree.reset();
        blTree.reset();
    }

    private int l_code(int len) {

        if (len == 255) {
            return 285;
        }
        int code = 257;
        while (len >= 8) {
            code += 4;
            len >>= 1;
        }
        return code + len;
    }

    private int d_code(int distance) {

        int code = 0;
        while (distance >= 4) {
            code += 2;
            distance >>= 1;
        }
        return code + distance;
    }

    public void sendAllTrees(int blTreeCodes) {

        blTree.buildCodes();
        literalTree.buildCodes();
        distTree.buildCodes();
        pending.writeBits(literalTree.numCodes - 257, 5);
        pending.writeBits(distTree.numCodes - 1, 5);
        pending.writeBits(blTreeCodes - 4, 4);
        for (int rank = 0; rank < blTreeCodes; rank++) {
            pending.writeBits(blTree.length[BL_ORDER[rank]], 3);
        }
        literalTree.writeTree(blTree);
        distTree.writeTree(blTree);

    }

    public void compressBlock() {

        for (int i = 0; i < last_lit; i++) {
            int litlen = l_buf[i] & 0xff;
            int dist = d_buf[i];
            if (dist-- != 0) {

                int lc = l_code(litlen);
                literalTree.writeSymbol(lc);

                int bits = (lc - 261) / 4;
                if (bits > 0 && bits <= 5) {
                    pending.writeBits(litlen & ((1 << bits) - 1), bits);
                }

                int dc = d_code(dist);
                distTree.writeSymbol(dc);

                bits = dc / 2 - 1;
                if (bits > 0) {
                    pending.writeBits(dist & ((1 << bits) - 1), bits);
                }
            } else {

                literalTree.writeSymbol(litlen);
            }
        }

        literalTree.writeSymbol(256);

    }

    public void flushStoredBlock(byte stored[], int stored_offset, int stored_len, boolean lastBlock) {

        pending.writeBits(lastBlock ? 1 : 0, 3);
        pending.alignToByte();
        pending.writeShort(stored_len);
        pending.writeShort(~stored_len);
        pending.writeBlock(stored, stored_offset, stored_len);
        reset();
    }

    public void flushBlock(byte stored[], int stored_offset, int stored_len, boolean lastBlock) {

        literalTree.freqs[256]++;
        literalTree.buildTree();
        distTree.buildTree();
        literalTree.calcBLFreq(blTree);
        distTree.calcBLFreq(blTree);
        blTree.buildTree();

        int blTreeCodes = 4;
        for (int i = 18; i > blTreeCodes; i--) {
            if (blTree.length[BL_ORDER[i]] > 0) {
                blTreeCodes = i + 1;
            }
        }
        int opt_len = 14 + blTreeCodes * 3 + blTree.getEncodedLength() + literalTree.getEncodedLength() + distTree.getEncodedLength() + extra_bits;
        int static_len = extra_bits;
        for (int i = 0; i < 286; i++) {
            static_len += literalTree.freqs[i] * staticLLength[i];
        }
        for (int i = 0; i < 30; i++) {
            static_len += distTree.freqs[i] * staticDLength[i];
        }
        if (opt_len >= static_len) {
            opt_len = static_len;
        }

        if (stored_offset >= 0 && stored_len + 4 < opt_len >> 3) {
            flushStoredBlock(stored, stored_offset, stored_len, lastBlock);
        } else if (opt_len == static_len) {
            pending.writeBits(2 + (lastBlock ? 1 : 0), 3);
            literalTree.setStaticCodes(staticLCodes, staticLLength);
            distTree.setStaticCodes(staticDCodes, staticDLength);
            compressBlock();
            reset();
        } else {
            pending.writeBits(4 + (lastBlock ? 1 : 0), 3);
            sendAllTrees(blTreeCodes);
            compressBlock();
            reset();
        }
    }

    public boolean isFull() {

        return last_lit == 16384;
    }

    public boolean tallyLit(int lit) {
       
        d_buf[last_lit] = 0;
        l_buf[last_lit++] = (byte) lit;
        literalTree.freqs[lit]++;
        return last_lit == 16384;
    }

    public boolean tallyDist(int dist, int len) {
      
        d_buf[last_lit] = (short) dist;
        l_buf[last_lit++] = (byte) (len - 3);

        int lc = l_code(len - 3);
        literalTree.freqs[lc]++;
        if (lc >= 265 && lc < 285) {
            extra_bits += (lc - 261) / 4;
        }

        int dc = d_code(dist - 1);
        distTree.freqs[dc]++;
        if (dc >= 4) {
            extra_bits += dc / 2 - 1;
        }
        return last_lit == 16384;
    }
}
