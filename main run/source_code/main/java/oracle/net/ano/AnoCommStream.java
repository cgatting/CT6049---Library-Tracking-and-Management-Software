/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import oracle.net.ano.AnoComm;
import oracle.net.ns.NetException;
import oracle.net.ns.SessionAtts;

class AnoCommStream
extends AnoComm {
    private OutputStream out;
    private InputStream in;

    public AnoCommStream(SessionAtts sessionAtts) {
        this.out = sessionAtts.getOutputStream();
        this.in = sessionAtts.getInputStream();
    }

    @Override
    protected final void flush() {
        this.out.flush();
    }

    @Override
    protected final void a(short s2) {
        this.a(1, 2);
        this.out.write(0xFF & s2);
    }

    @Override
    protected final void a(String string) {
        this.a(string.length(), 0);
        this.out.write(string.getBytes());
    }

    @Override
    protected final void d(byte[] byArray) {
        this.a(byArray.length, 1);
        this.out.write(byArray);
    }

    @Override
    protected final void b(short s2) {
        this.out.write(0xFF & s2);
    }

    @Override
    protected final void c(int n2) {
        byte[] byArray = new byte[2];
        n2 = AnoCommStream.a((short)(0xFFFF & n2), byArray);
        this.out.write(byArray, 0, n2);
    }

    @Override
    protected final void b(long l2) {
        byte[] byArray = new byte[4];
        byte by = AnoCommStream.a((int)(0xFFFFFFFFFFFFFFFFL & l2), byArray);
        this.out.write(byArray, 0, by);
    }

    @Override
    protected final short o() {
        short s2;
        try {
            s2 = (short)this.in.read();
            if (s2 < 0) {
                throw new NetException(0);
            }
        }
        catch (NetException netException) {
            throw new IOException(netException.getMessage());
        }
        return s2;
    }

    @Override
    protected final byte[] e(int n2) {
        byte[] byArray = new byte[n2];
        try {
            if (this.in.read(byArray) < 0) {
                throw new NetException(0);
            }
        }
        catch (NetException netException) {
            throw new IOException(netException.getMessage());
        }
        return byArray;
    }

    @Override
    protected final long e(byte[] byArray) {
        long l2 = 0L;
        try {
            if (this.in.read(byArray) < 0) {
                throw new NetException(0);
            }
        }
        catch (NetException netException) {
            throw new IOException(netException.getMessage());
        }
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            l2 |= (long)((byArray[i2] & 0xFF) << 8 * (byArray.length - 1 - i2));
        }
        return l2;
    }
}

