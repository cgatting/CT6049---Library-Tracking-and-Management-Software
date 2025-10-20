/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.io.IOException;
import oracle.net.ano.Ano;
import oracle.net.aso.k;
import oracle.net.aso.m;
import oracle.net.ns.DataPacket;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;

public class CryptoDataPacket
extends DataPacket
implements SQLnetDef {
    private m f = null;
    private k g = null;
    private byte B = 0;
    private int C = 0;
    private Ano ano = null;

    public CryptoDataPacket(SessionAtts sessionAtts) {
        super(sessionAtts);
        this.ano = sessionAtts.ano;
        if (sessionAtts.ano.f != null) {
            this.f = sessionAtts.ano.f;
            this.C += this.f.z();
        }
        if (sessionAtts.ano.g != null) {
            this.g = sessionAtts.ano.g;
            this.C += this.g.size();
        }
        ++this.C;
    }

    @Override
    protected void createBuffer(int n2) {
        super.createBuffer(n2 + (this.sAtts.poolEnabled ? 16 : 0));
    }

    @Override
    public void receive() {
        super.receive();
        if (this.type != 6) {
            return;
        }
        this.ano = this.sAtts.ano;
        if (this.ano.f != null) {
            this.f = this.ano.f;
            this.C += this.f.z();
            if (this.ano.getRenewKey()) {
                this.f.a(null, null);
            }
        }
        if (this.ano.g != null) {
            this.g = this.ano.g;
            this.C += this.g.size();
            if (this.ano.getRenewKey()) {
                this.g.Z();
            }
        }
        ++this.C;
        this.ano.setRenewKey(false);
        try {
            CryptoDataPacket cryptoDataPacket = this;
            byte[] byArray = new byte[cryptoDataPacket.dataLen - 1];
            byte[] cfr_ignored_0 = cryptoDataPacket.buffer;
            int cfr_ignored_1 = cryptoDataPacket.length;
            --cryptoDataPacket.dataLen;
            System.arraycopy(cryptoDataPacket.buffer, cryptoDataPacket.dataOff, byArray, 0, cryptoDataPacket.dataLen);
            if (cryptoDataPacket.f != null) {
                byArray = cryptoDataPacket.f.f(byArray);
            }
            if (byArray == null) {
                throw new IOException("Bad buffer - Fail to decrypt buffer");
            }
            cryptoDataPacket.dataLen = byArray.length;
            if (cryptoDataPacket.g != null) {
                byte[] byArray2 = new byte[cryptoDataPacket.g.size()];
                cryptoDataPacket.dataLen -= cryptoDataPacket.g.size();
                System.arraycopy(byArray, cryptoDataPacket.dataLen, byArray2, 0, cryptoDataPacket.g.size());
                byte[] byArray3 = new byte[cryptoDataPacket.dataLen];
                System.arraycopy(byArray, 0, byArray3, 0, cryptoDataPacket.dataLen);
                if (cryptoDataPacket.g.c(byArray3, byArray2)) {
                    throw new IOException("Checksum fail");
                }
                System.arraycopy(byArray3, 0, cryptoDataPacket.buffer, cryptoDataPacket.dataOff, cryptoDataPacket.dataLen);
            } else {
                System.arraycopy(byArray, 0, cryptoDataPacket.buffer, cryptoDataPacket.dataOff, cryptoDataPacket.dataLen);
            }
            cryptoDataPacket.length = cryptoDataPacket.dataOff + cryptoDataPacket.dataLen;
            cryptoDataPacket.pktOffset = 10;
            return;
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            throw iOException;
        }
    }

    @Override
    protected int putDataInBuffer(byte[] byArray, int n2, int n3) {
        if ((n3 = Math.min(this.buffer.length - this.sessionIdSize - this.C - this.pktOffset, n3)) > 0) {
            System.arraycopy(byArray, n2, this.buffer, this.pktOffset, n3);
            this.pktOffset += n3;
            this.isBufferFull = this.pktOffset + this.C == this.buffer.length - this.sessionIdSize;
            this.availableBytesToSend = this.dataOff < this.pktOffset ? this.pktOffset - this.dataOff : 0;
        }
        return n3;
    }

    @Override
    protected void send(int n2) {
        try {
            if (this.B == 0) {
                this.B = this.ano.w();
            }
            CryptoDataPacket cryptoDataPacket = this;
            this.dataLen = cryptoDataPacket.availableBytesToSend;
            if (cryptoDataPacket.dataLen > 0) {
                byte[] byArray = new byte[cryptoDataPacket.availableBytesToSend];
                System.arraycopy(cryptoDataPacket.buffer, cryptoDataPacket.dataOff, byArray, 0, byArray.length);
                byte[] byArray2 = null;
                if (cryptoDataPacket.g != null && (byArray2 = cryptoDataPacket.g.e(byArray, byArray.length)) != null) {
                    cryptoDataPacket.dataLen += byArray2.length;
                }
                byte[] byArray3 = new byte[cryptoDataPacket.dataLen];
                System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
                if (byArray2 != null) {
                    System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
                }
                if (cryptoDataPacket.f != null) {
                    byArray = cryptoDataPacket.f.g(byArray3);
                    if (byArray == null) {
                        throw new IOException("Fail to encrypt buffer");
                    }
                    cryptoDataPacket.dataLen = byArray.length;
                    System.arraycopy(byArray, 0, cryptoDataPacket.buffer, cryptoDataPacket.dataOff, cryptoDataPacket.dataLen);
                } else if (cryptoDataPacket.g != null) {
                    System.arraycopy(byArray3, 0, cryptoDataPacket.buffer, cryptoDataPacket.dataOff, cryptoDataPacket.dataLen);
                }
                if (cryptoDataPacket.dataLen > 0) {
                    cryptoDataPacket.buffer[cryptoDataPacket.dataOff + cryptoDataPacket.dataLen] = cryptoDataPacket.B;
                    ++cryptoDataPacket.dataLen;
                }
            }
            cryptoDataPacket.pktOffset = 10 + cryptoDataPacket.dataLen;
            cryptoDataPacket.length = 10 + cryptoDataPacket.dataLen;
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            throw iOException;
        }
        super.send(n2);
    }
}

