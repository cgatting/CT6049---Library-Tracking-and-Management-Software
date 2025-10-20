/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import oracle.net.ano.Ano;
import oracle.net.aso.k;
import oracle.net.aso.m;
import oracle.net.ns.NIONSDataChannel;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;

class CryptoNIONSDataChannel
extends NIONSDataChannel
implements SQLnetDef {
    private m f = null;
    private k g = null;
    private byte B = 0;
    private int C = 0;
    private Ano ano = null;

    public CryptoNIONSDataChannel(SessionAtts sessionAtts) {
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
    public void readDataFromSocketChannel() {
        super.readDataFromSocketChannel();
        this.ano = this.session.ano;
        this.C = 0;
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
            CryptoNIONSDataChannel cryptoNIONSDataChannel = this;
            int n2 = cryptoNIONSDataChannel.session.payloadDataBufferForRead.position();
            ByteOrder byteOrder = cryptoNIONSDataChannel.session.payloadDataBufferForRead.order();
            cryptoNIONSDataChannel.session.payloadDataBufferForRead.order(ByteOrder.BIG_ENDIAN);
            int n3 = cryptoNIONSDataChannel.session.payloadDataBufferForRead.limit();
            if (n3 > 0) {
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.position(n3 - 1);
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.get();
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.position(n2);
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.order(byteOrder);
                --n3;
            }
            byte[] byArray = new byte[n3];
            int n4 = cryptoNIONSDataChannel.session.payloadDataBufferForRead.limit();
            cryptoNIONSDataChannel.session.payloadDataBufferForRead.get(byArray);
            cryptoNIONSDataChannel.session.payloadDataBufferForRead.position(n2);
            cryptoNIONSDataChannel.session.payloadDataBufferForRead.limit(n4);
            if (cryptoNIONSDataChannel.f != null && n3 > 0) {
                byArray = cryptoNIONSDataChannel.f.f(byArray);
            }
            if (byArray == null) {
                throw new IOException("Bad buffer - Fail to decrypt buffer");
            }
            n3 = byArray.length;
            if (cryptoNIONSDataChannel.g != null && n3 > 0) {
                byte[] byArray2 = new byte[cryptoNIONSDataChannel.g.size()];
                System.arraycopy(byArray, n3 -= cryptoNIONSDataChannel.g.size(), byArray2, 0, cryptoNIONSDataChannel.g.size());
                byte[] byArray3 = new byte[n3];
                System.arraycopy(byArray, 0, byArray3, 0, n3);
                if (cryptoNIONSDataChannel.g.c(byArray3, byArray2)) {
                    throw new IOException("Checksum fail");
                }
                cryptoNIONSDataChannel.session.payloadDataBufferForRead = ByteBuffer.wrap(byArray3, 0, n3);
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.limit(n3);
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.order(byteOrder);
            } else {
                cryptoNIONSDataChannel.session.payloadDataBufferForRead = ByteBuffer.wrap(byArray, 0, n3);
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.limit(n3);
                cryptoNIONSDataChannel.session.payloadDataBufferForRead.order(byteOrder);
            }
            cryptoNIONSDataChannel.session.payloadDataBufferForRead.position(n2);
            return;
        }
        catch (Exception exception) {
            this.ano.checkForAnoNegotiationFailure();
            throw exception;
        }
    }

    @Override
    public void writeDataToSocketChannel() {
        try {
            if (this.B == 0) {
                this.B = this.ano.w();
            }
            CryptoNIONSDataChannel cryptoNIONSDataChannel = this;
            int n2 = cryptoNIONSDataChannel.session.payloadDataBufferForWrite.position();
            byte[] byArray = new byte[cryptoNIONSDataChannel.session.payloadDataBufferForWrite.position()];
            cryptoNIONSDataChannel.session.payloadDataBufferForWrite.limit(cryptoNIONSDataChannel.session.payloadDataBufferForWrite.position());
            cryptoNIONSDataChannel.session.payloadDataBufferForWrite.position(0);
            cryptoNIONSDataChannel.session.payloadDataBufferForWrite.get(byArray);
            cryptoNIONSDataChannel.session.payloadDataBufferForWrite.position(0);
            cryptoNIONSDataChannel.session.payloadDataBufferForWrite.limit(cryptoNIONSDataChannel.session.payloadDataBufferForWrite.capacity());
            byte[] byArray2 = null;
            if (cryptoNIONSDataChannel.g != null && (byArray2 = cryptoNIONSDataChannel.g.e(byArray, byArray.length)) != null) {
                n2 += byArray2.length;
            }
            byte[] byArray3 = new byte[n2];
            System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
            if (byArray2 != null) {
                System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
            }
            if (cryptoNIONSDataChannel.f != null) {
                byArray = cryptoNIONSDataChannel.f.g(byArray3);
                if (byArray == null) {
                    throw new IOException("Fail to encrypt buffer");
                }
                n2 = byArray.length;
                cryptoNIONSDataChannel.session.payloadDataBufferForWrite.put(byArray);
            } else if (cryptoNIONSDataChannel.g != null) {
                cryptoNIONSDataChannel.session.payloadDataBufferForWrite.put(byArray3);
            }
            if (n2 > 0) {
                cryptoNIONSDataChannel.session.payloadDataBufferForWrite.put(cryptoNIONSDataChannel.B);
            }
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            throw iOException;
        }
        super.writeDataToSocketChannel();
    }

    @Override
    public int getDataExpansionByteSize() {
        return this.C;
    }

    @Override
    protected void processMarker() {
        this.session.ano.setRenewKey(true);
    }
}

