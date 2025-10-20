/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

class FDBigInt {
    int nWords;
    int[] data;

    FDBigInt(int n2) {
        this.nWords = 1;
        this.data = new int[1];
        this.data[0] = n2;
    }

    FDBigInt(long l2) {
        this.data = new int[2];
        this.data[0] = (int)l2;
        this.data[1] = (int)(l2 >>> 32);
        this.nWords = this.data[1] == 0 ? 1 : 2;
    }

    FDBigInt(FDBigInt fDBigInt) {
        this.nWords = fDBigInt.nWords;
        this.data = new int[this.nWords];
        System.arraycopy(fDBigInt.data, 0, this.data, 0, this.nWords);
    }

    FDBigInt(int[] nArray, int n2) {
        this.data = nArray;
        this.nWords = n2;
    }

    void lshiftMe(int n2) throws IllegalArgumentException {
        if (n2 <= 0) {
            if (n2 == 0) {
                return;
            }
            throw new IllegalArgumentException("negative shift count");
        }
        int n3 = n2 >> 5;
        int n4 = n2 & 0x1F;
        int n5 = 32 - n4;
        int[] nArray = this.data;
        int[] nArray2 = this.data;
        if (this.nWords + n3 + 1 > nArray.length) {
            nArray = new int[this.nWords + n3 + 1];
        }
        int n6 = this.nWords + n3;
        int n7 = this.nWords - 1;
        if (n4 == 0) {
            System.arraycopy(nArray2, 0, nArray, n3, this.nWords);
            n6 = n3 - 1;
        } else {
            nArray[n6--] = nArray2[n7] >>> n5;
            while (n7 >= 1) {
                nArray[n6--] = nArray2[n7] << n4 | nArray2[--n7] >>> n5;
            }
            nArray[n6--] = nArray2[n7] << n4;
        }
        while (n6 >= 0) {
            nArray[n6--] = 0;
        }
        this.data = nArray;
        this.nWords += n3 + 1;
        while (this.nWords > 1 && this.data[this.nWords - 1] == 0) {
            --this.nWords;
        }
    }

    int normalizeMe() throws IllegalArgumentException {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        for (n2 = this.nWords - 1; n2 >= 0 && (n5 = this.data[n2]) == 0; --n2) {
            ++n3;
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("zero value");
        }
        this.nWords -= n3;
        if ((n5 & 0xF0000000) != 0) {
            n4 = 32;
            while ((n5 & 0xF0000000) != 0) {
                n5 >>>= 1;
                --n4;
            }
        } else {
            while (n5 <= 1048575) {
                n5 <<= 8;
                n4 += 8;
            }
            while (n5 <= 0x7FFFFFF) {
                n5 <<= 1;
                ++n4;
            }
        }
        if (n4 != 0) {
            this.lshiftMe(n4);
        }
        return n4;
    }

    FDBigInt mult(int n2) {
        long l2 = n2;
        int[] nArray = new int[l2 * ((long)this.data[this.nWords - 1] & 0xFFFFFFFFL) > 0xFFFFFFFL ? this.nWords + 1 : this.nWords];
        long l3 = 0L;
        for (int i2 = 0; i2 < this.nWords; ++i2) {
            nArray[i2] = (int)(l3 += l2 * ((long)this.data[i2] & 0xFFFFFFFFL));
            l3 >>>= 32;
        }
        if (l3 == 0L) {
            return new FDBigInt(nArray, this.nWords);
        }
        nArray[this.nWords] = (int)l3;
        return new FDBigInt(nArray, this.nWords + 1);
    }

    FDBigInt mult(FDBigInt fDBigInt) {
        int n2;
        int[] nArray = new int[this.nWords + fDBigInt.nWords];
        for (n2 = 0; n2 < this.nWords; ++n2) {
            long l2 = (long)this.data[n2] & 0xFFFFFFFFL;
            long l3 = 0L;
            for (int i2 = 0; i2 < fDBigInt.nWords; ++i2) {
                nArray[n2 + i2] = (int)(l3 += ((long)nArray[n2 + i2] & 0xFFFFFFFFL) + l2 * ((long)fDBigInt.data[i2] & 0xFFFFFFFFL));
                l3 >>>= 32;
            }
            nArray[n2 + i2] = (int)l3;
        }
        for (n2 = nArray.length - 1; n2 > 0 && nArray[n2] == 0; --n2) {
        }
        return new FDBigInt(nArray, n2 + 1);
    }

    FDBigInt add(FDBigInt fDBigInt) {
        int n2;
        int n3;
        int[] nArray;
        int n4;
        int[] nArray2;
        long l2 = 0L;
        if (this.nWords >= fDBigInt.nWords) {
            nArray2 = this.data;
            n4 = this.nWords;
            nArray = fDBigInt.data;
            n3 = fDBigInt.nWords;
        } else {
            nArray2 = fDBigInt.data;
            n4 = fDBigInt.nWords;
            nArray = this.data;
            n3 = this.nWords;
        }
        int[] nArray3 = new int[n4];
        for (n2 = 0; n2 < n4; ++n2) {
            l2 += (long)nArray2[n2] & 0xFFFFFFFFL;
            if (n2 < n3) {
                l2 += (long)nArray[n2] & 0xFFFFFFFFL;
            }
            nArray3[n2] = (int)l2;
            l2 >>= 32;
        }
        if (l2 != 0L) {
            int[] nArray4 = new int[nArray3.length + 1];
            System.arraycopy(nArray3, 0, nArray4, 0, nArray3.length);
            nArray4[n2++] = (int)l2;
            return new FDBigInt(nArray4, n2);
        }
        return new FDBigInt(nArray3, n2);
    }

    int cmp(FDBigInt fDBigInt) {
        int n2;
        int n3;
        if (this.nWords > fDBigInt.nWords) {
            n3 = fDBigInt.nWords - 1;
            for (n2 = this.nWords - 1; n2 > n3; --n2) {
                if (this.data[n2] == 0) continue;
                return 1;
            }
        } else if (this.nWords < fDBigInt.nWords) {
            n3 = this.nWords - 1;
            for (n2 = fDBigInt.nWords - 1; n2 > n3; --n2) {
                if (fDBigInt.data[n2] == 0) continue;
                return -1;
            }
        } else {
            n2 = this.nWords - 1;
        }
        while (n2 > 0 && this.data[n2] == fDBigInt.data[n2]) {
            --n2;
        }
        n3 = this.data[n2];
        int n4 = fDBigInt.data[n2];
        if (n3 < 0) {
            if (n4 < 0) {
                return n3 - n4;
            }
            return 1;
        }
        if (n4 < 0) {
            return -1;
        }
        return n3 - n4;
    }

    int quoRemIteration(FDBigInt fDBigInt) throws IllegalArgumentException {
        int n2;
        if (this.nWords != fDBigInt.nWords) {
            throw new IllegalArgumentException("disparate values");
        }
        int n3 = this.nWords - 1;
        long l2 = ((long)this.data[n3] & 0xFFFFFFFFL) / (long)fDBigInt.data[n3];
        long l3 = 0L;
        for (int i2 = 0; i2 <= n3; ++i2) {
            this.data[i2] = (int)(l3 += ((long)this.data[i2] & 0xFFFFFFFFL) - l2 * ((long)fDBigInt.data[i2] & 0xFFFFFFFFL));
            l3 >>= 32;
        }
        if (l3 != 0L) {
            long l4 = 0L;
            while (l4 == 0L) {
                l4 = 0L;
                for (n2 = 0; n2 <= n3; ++n2) {
                    this.data[n2] = (int)(l4 += ((long)this.data[n2] & 0xFFFFFFFFL) + ((long)fDBigInt.data[n2] & 0xFFFFFFFFL));
                    l4 >>= 32;
                }
                if (l4 != 0L && l4 != 1L) {
                    throw new RuntimeException("Assertion botch: " + l4 + " carry out of division correction");
                }
                --l2;
            }
        }
        long l5 = 0L;
        for (n2 = 0; n2 <= n3; ++n2) {
            this.data[n2] = (int)(l5 += 10L * ((long)this.data[n2] & 0xFFFFFFFFL));
            l5 >>= 32;
        }
        if (l5 != 0L) {
            throw new RuntimeException("Assertion botch: carry out of *10");
        }
        return (int)l2;
    }
}

