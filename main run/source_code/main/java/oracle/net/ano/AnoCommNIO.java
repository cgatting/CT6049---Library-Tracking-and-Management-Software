/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import oracle.net.ano.AnoComm;
import oracle.net.ns.NIONSDataChannel;
import oracle.net.ns.SessionAtts;

class AnoCommNIO
extends AnoComm {
    private boolean m = false;
    private NIONSDataChannel n;
    private SessionAtts sAtts;

    public AnoCommNIO(SessionAtts sessionAtts) {
        this.sAtts = sessionAtts;
        this.n = sessionAtts.dataChannel;
    }

    private void p() {
        if (this.m) {
            this.flush();
        }
        if (this.sAtts.payloadDataBufferForRead.hasRemaining()) {
            return;
        }
        this.n.readDataFromSocketChannel();
    }

    private void f(int n2) {
        if (this.sAtts.payloadDataBufferForWrite.remaining() < n2) {
            if (this.m) {
                this.flush();
            }
            this.sAtts.prepareWriteBuffer();
        }
    }

    @Override
    protected final void flush() {
        if (this.m) {
            this.n.writeDataToSocketChannel();
        }
        this.m = false;
    }

    @Override
    protected final void a(short s2) {
        this.a(1, 2);
        this.f(1);
        this.sAtts.payloadDataBufferForWrite.put((byte)s2);
        this.m = true;
    }

    @Override
    protected final void a(String string) {
        this.a(string.length(), 0);
        this.f(string.length());
        this.sAtts.payloadDataBufferForWrite.put(string.getBytes());
        this.m = true;
    }

    @Override
    protected final void d(byte[] byArray) {
        this.a(byArray.length, 1);
        this.f(byArray.length);
        this.sAtts.payloadDataBufferForWrite.put(byArray);
        this.m = true;
    }

    @Override
    protected final void b(short s2) {
        this.f(1);
        this.sAtts.payloadDataBufferForWrite.put((byte)s2);
        this.m = true;
    }

    @Override
    protected final void c(int n2) {
        byte[] byArray = new byte[2];
        n2 = AnoCommNIO.a((short)(0xFFFF & n2), byArray);
        this.f(2);
        this.sAtts.payloadDataBufferForWrite.put(byArray, 0, n2);
        this.m = true;
    }

    @Override
    protected final void b(long l2) {
        byte[] byArray = new byte[4];
        byte by = AnoCommNIO.a((int)(0xFFFFFFFFFFFFFFFFL & l2), byArray);
        this.f(4);
        this.sAtts.payloadDataBufferForWrite.put(byArray, 0, by);
        this.m = true;
    }

    @Override
    protected final short o() {
        this.p();
        short s2 = (short)(this.sAtts.payloadDataBufferForRead.get() & 0xFF);
        return s2;
    }

    @Override
    protected final byte[] e(int n2) {
        byte[] byArray = new byte[n2];
        this.p();
        this.sAtts.payloadDataBufferForRead.get(byArray);
        return byArray;
    }

    @Override
    protected final long e(byte[] byArray) {
        long l2 = 0L;
        this.p();
        this.sAtts.payloadDataBufferForRead.get(byArray);
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            l2 |= (long)((byArray[i2] & 0xFF) << 8 * (byArray.length - 1 - i2));
        }
        return l2;
    }
}

