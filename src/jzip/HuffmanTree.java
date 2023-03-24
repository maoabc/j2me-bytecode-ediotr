package jzip;

class HuffmanTree {

    short freqs[], codes[];
    byte length[];
    int bl_counts[];
    int minNumCodes, numCodes, maxLength, frelen;
    PendingBuffer ping;

    HuffmanTree(int elems, int minCodes, int maxL, PendingBuffer pen) {
        ping = pen;
        minNumCodes = minCodes;
        maxLength = maxL;
        frelen = elems;
        freqs = new short[elems];
        bl_counts = new int[maxL];

    }

    public int getEncodedLength() {
        int len = 0;
        for (int i = 0; i < frelen; i++) {
            len += freqs[i] * length[i];
        }
        return len;
    }

    public void reset() {
        for (int i = frelen - 1; i >= 0; i--) {
            freqs[i] = 0;
        }
        codes = null;
        length = null;
    }

    public void writeSymbol(int code) {
        ping.writeBits(codes[code] & 65535, length[code]);
    }

    public void setStaticCodes(short stCodes[], byte stLength[]) {
        codes = stCodes;
        length = stLength;
    }

    public void buildCodes() {
        int nextCode[] = new int[maxLength];
        int code = 0;
        codes = new short[frelen];
        for (int bits = 0; bits < maxLength; bits++) {
            nextCode[bits] = code;
            code += bl_counts[bits] << (15 - bits);
        }
        for (int i = 0; i < numCodes; i++) {
            int bits = length[i];
            if (bits > 0) {
                codes[i] = DeflaterHuffman.bitReverse(nextCode[bits - 1]);
                nextCode[bits - 1] += 1 << (16 - bits);
            }
        }
    }

    private void buildLength(int childs[], int len) {
        length = new byte[frelen];
        int numNodes = len / 2;
        int numLeafs = (numNodes + 1) / 2;
        int overflow = 0;
        for (int i = 0; i < maxLength; i++) {
            bl_counts[i] = 0;
        }
        int lengths[] = new int[numNodes];
        lengths[numNodes - 1] = 0;
        for (int i = numNodes - 1; i >= 0; i--) {
            if (childs[2 * i + 1] != -1) {
                int bitLength = lengths[i] + 1;
                if (bitLength > maxLength) {
                    bitLength = maxLength;
                    overflow++;
                }
                lengths[childs[2 * i]] = lengths[childs[2 * i + 1]] = bitLength;
            } else {
                int bitLength = lengths[i];
                bl_counts[bitLength - 1]++;
                length[childs[2 * i]] = (byte) lengths[i];
            }
        }
        if (overflow == 0) {
            return;
        }
        int incrBitLen = maxLength - 1;
        do {

            while (bl_counts[--incrBitLen] == 0);
            do {
                bl_counts[incrBitLen]--;
                bl_counts[++incrBitLen]++;
                overflow -= 1 << (maxLength - 1 - incrBitLen);
            } while (overflow > 0 && incrBitLen < maxLength - 1);
        } while (overflow > 0);
        bl_counts[maxLength - 1] += overflow;
        bl_counts[maxLength - 2] -= overflow;
        int nodePtr = 2 * numLeafs;
        int n, childPtr;
        for (int bits = maxLength; bits != 0; bits--) {
            n = bl_counts[bits - 1];

            while (n > 0) {
                childPtr = 2 * childs[nodePtr++];
                if (childs[childPtr + 1] == -1) {
                    length[childs[childPtr]] = (byte) bits;
                    n--;
                }
            }
        }
    }

    public void buildTree() {
        int numSymbols = frelen;
        int heap[] = new int[numSymbols];
        int heapLen = 0, maxCode = 0;
        int freq;
        for (int n = 0; n < numSymbols; n++) {
            freq = freqs[n];
            if (freq != 0) {
                int pos = heapLen++;
                int ppos;
                while (pos > 0 && freqs[heap[ppos = (pos - 1) / 2]] > freq) {
                    heap[pos] = heap[ppos];
                    pos = ppos;
                }
                heap[pos] = n;
                maxCode = n;
            }
        }
        while (heapLen < 2) {
            heap[heapLen++] = maxCode < 2 ? ++maxCode : 0;
        }
        numCodes = Math.max(maxCode + 1, minNumCodes);
        int childlen = 4 * heapLen - 2;
        int childs[] = new int[childlen];
        int values[] = new int[2 * heapLen - 1];
        int numNodes = heapLen;
        for (int i = 0; i < heapLen; i++) {
            int node = heap[i];
            childs[2 * i] = node;
            childs[2 * i + 1] = -1;
            values[i] = freqs[node] << 8;
            heap[i] = i;
        }
        do {
            int first = heap[0];
            int last = heap[--heapLen];
            int ppos = 0;
            int path = 1;
            while (path < heapLen) {
                if (path + 1 < heapLen && values[heap[path]] > values[heap[path + 1]]) {
                    path++;
                }
                heap[ppos] = heap[path];
                ppos = path;
                path = path * 2 + 1;
            }
            int lastVal = values[last];
            while ((path = ppos) > 0 && values[heap[ppos = (path - 1) / 2]] > lastVal) {
                heap[path] = heap[ppos];
            }
            heap[path] = last;
            int second = heap[0];

            last = numNodes++;
            childs[2 * last] = first;
            childs[2 * last + 1] = second;
            int mindepth = Math.min(values[first] & 255, values[second] & 255);
            values[last] = lastVal = values[first] + values[second] - mindepth + 1;
            ppos = 0;
            path = 1;
            while (path < heapLen) {
                if (path + 1 < heapLen && values[heap[path]] > values[heap[path + 1]]) {
                    path++;
                }
                heap[ppos] = heap[path];
                ppos = path;
                path = ppos * 2 + 1;
            }

            while ((path = ppos) > 0 && values[heap[ppos = (path - 1) / 2]] > lastVal) {
                heap[path] = heap[ppos];
            }
            heap[path] = last;
        } while (heapLen > 1);

        buildLength(childs, childlen);
    }

    public void calcBLFreq(HuffmanTree blTree) {
        int max_count, count;
        int curlen = -1;
        int i = 0;
        while (i < numCodes) {
            count = 1;
            int nextlen = length[i];
            if (nextlen == 0) {
                max_count = 138;
            } else {
                max_count = 6;
                if (curlen != nextlen) {
                    blTree.freqs[nextlen]++;
                    count = 0;
                }
            }
            curlen = nextlen;
            i++;
            while (i < numCodes && curlen == length[i]) {
                i++;
                if (++count >= max_count) {
                    break;
                }
            }
            if (count < 3) {
                blTree.freqs[curlen] += count;
            } else if (curlen != 0) {
                blTree.freqs[16]++;
            } else if (count <= 10) {
                blTree.freqs[17]++;
            } else {
                blTree.freqs[18]++;
            }
        }
    }

    public void writeTree(HuffmanTree blTree) {
        int max_count, count;
        int curlen = -1;
        int i = 0;
        while (i < numCodes) {
            count = 1;
            int nextlen = length[i];
            if (nextlen == 0) {
                max_count = 138;
            } else {
                max_count = 6;
                if (curlen != nextlen) {
                    blTree.writeSymbol(nextlen);
                    count = 0;
                }
            }
            curlen = nextlen;
            i++;
            while (i < numCodes && curlen == length[i]) {
                i++;
                if (++count >= max_count) {
                    break;
                }
            }
            if (count < 3) {
                while (count-- > 0) {
                    blTree.writeSymbol(curlen);
                }
            } else if (curlen != 0) {
                blTree.writeSymbol(16);
                ping.writeBits(count - 3, 2);
            } else if (count <= 10) {
                blTree.writeSymbol(17);
                ping.writeBits(count - 3, 3);
            } else {
                blTree.writeSymbol(18);
                ping.writeBits(count - 11, 7);
            }

        }
    }
}
