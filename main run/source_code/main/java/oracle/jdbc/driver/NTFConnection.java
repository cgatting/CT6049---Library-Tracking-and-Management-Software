/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import javax.net.ssl.SSLEngine;
import oracle.jdbc.aq.AQNotificationEvent;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.driver.NTFAQEvent;
import oracle.jdbc.driver.NTFDCNEvent;
import oracle.jdbc.driver.NTFListener;
import oracle.jdbc.driver.NTFManager;
import oracle.jdbc.driver.NTFRegistration;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.net.nt.CustomSSLSocketFactory;
import oracle.net.nt.SSLSocketChannel;
import oracle.net.nt.TcpsConfigure;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHANGE_NOTIFICATION})
class NTFConnection
extends Thread {
    private static final int NS_HEADER_SIZE = 10;
    private SocketChannel channel = null;
    private ByteBuffer inBuffer = null;
    private ByteBuffer outBuffer = null;
    private int currentNSPacketLength;
    private int currentNSPacketType;
    private ByteBuffer currentNSPacketDataBuffer;
    private boolean needsToBeClosed = false;
    private NTFManager ntfManager;
    private NTFListener ntfListener;
    private Selector selector = null;
    private Iterator<SelectionKey> iterator = null;
    int remotePort;
    String remoteAddress;
    String remoteName;
    int localPort;
    String localAddress;
    String localName;
    String connectionDescription;
    CharacterSet charset = null;
    boolean useSSL = false;
    static final int NSPTCN = 1;
    static final int NSPTAC = 2;
    static final int NSPTAK = 3;
    static final int NSPTRF = 4;
    static final int NSPTRD = 5;
    static final int NSPTDA = 6;
    static final int NSPTNL = 7;
    static final int NSPTAB = 9;
    static final int NSPTRS = 11;
    static final int NSPTMK = 12;
    static final int NSPTAT = 13;
    static final int NSPTCNL = 14;
    static final int NSPTHI = 19;
    static final short KPDNFY_TIMEOUT = 1;
    static final short KPDNFY_GROUPING = 2;

    NTFConnection(NTFManager nTFManager, SocketChannel socketChannel, NTFListener nTFListener) throws IOException {
        this.ntfManager = nTFManager;
        this.ntfListener = nTFListener;
        boolean bl = this.useSSL = this.ntfListener.socketOptions != null;
        if (this.useSSL) {
            this.prepareSSL(socketChannel);
        } else {
            this.channel = socketChannel;
        }
        this.channel.configureBlocking(false);
        this.channel.socket().setKeepAlive(true);
        this.inBuffer = ByteBuffer.allocate(4096);
        this.outBuffer = ByteBuffer.allocate(2048);
        Socket socket = this.channel.socket();
        InetAddress inetAddress = socket.getInetAddress();
        InetAddress inetAddress2 = socket.getLocalAddress();
        this.remotePort = socket.getPort();
        this.localPort = socket.getLocalPort();
        this.remoteAddress = inetAddress.getHostAddress();
        this.remoteName = inetAddress.getHostName();
        this.localAddress = inetAddress2.getHostAddress();
        this.localName = inetAddress2.getHostName();
        this.connectionDescription = "local=" + this.localName + "/" + this.localAddress + ":" + this.localPort + ", remote=" + this.remoteName + "/" + this.remoteAddress + ":" + this.remotePort;
    }

    private void prepareSSL(SocketChannel socketChannel) throws IOException {
        SSLEngine sSLEngine = CustomSSLSocketFactory.getSSLSocketEngine(null, 0, this.ntfListener.socketOptions);
        TcpsConfigure.configureVersion(null, sSLEngine, (String)this.ntfListener.socketOptions.get(6), true);
        TcpsConfigure.configureCipherSuites(null, sSLEngine, (String)this.ntfListener.socketOptions.get(7), true);
        sSLEngine.setUseClientMode(false);
        sSLEngine.setNeedClientAuth(false);
        this.channel = new SSLSocketChannel(socketChannel, sSLEngine);
    }

    @Override
    public void run() {
        try {
            this.selector = Selector.open();
            if (this.useSSL) {
                ((SSLSocketChannel)this.channel).getUnderlyingChannel().register(this.selector, 1);
            } else {
                this.channel.register(this.selector, 1);
            }
            int n2 = 0;
            this.inBuffer.limit(0);
            while (!this.needsToBeClosed) {
                if (!this.inBuffer.hasRemaining()) {
                    while ((n2 = this.readFromNetwork()) == 0) {
                    }
                }
                this.unmarshalOneNSPacket();
            }
        }
        catch (IOException iOException) {
        }
        catch (InterruptedException interruptedException) {
        }
        finally {
            try {
                if (!this.needsToBeClosed) {
                    this.ntfListener.releaseConnection(this);
                }
                if (this.selector != null) {
                    this.selector.close();
                }
                this.channel.close();
            }
            catch (IOException iOException) {
            }
        }
    }

    private int readFromNetwork() throws IOException, InterruptedException {
        int n2;
        this.inBuffer.compact();
        boolean bl = true;
        if (this.useSSL) {
            boolean bl2 = bl = !((SSLSocketChannel)this.channel).hasRemaining();
        }
        while (bl) {
            while (true) {
                if (this.iterator != null) {
                    if (this.iterator.hasNext()) break;
                }
                this.selector.select();
                if (this.needsToBeClosed) {
                    throw new InterruptedException();
                }
                this.iterator = this.selector.selectedKeys().iterator();
            }
            SelectionKey selectionKey = this.iterator.next();
            if (!selectionKey.isReadable()) continue;
            break;
        }
        if ((n2 = this.channel.read(this.inBuffer)) < 0) {
            throw new EOFException();
        }
        if (n2 > 0) {
            this.inBuffer.flip();
        }
        if (bl) {
            this.iterator.remove();
        }
        return n2;
    }

    private void getNextNSPacket() throws IOException, InterruptedException {
        int n2;
        while (!this.inBuffer.hasRemaining() || this.inBuffer.remaining() < 10) {
            n2 = this.readFromNetwork();
        }
        this.currentNSPacketLength = this.inBuffer.getShort();
        if (this.currentNSPacketLength <= 0) {
            throw new IOException("Invalid NS packet length.");
        }
        this.inBuffer.position(this.inBuffer.position() + 2);
        this.currentNSPacketType = this.inBuffer.get();
        this.validatePacketType();
        this.inBuffer.position(this.inBuffer.position() + 5);
        while (this.inBuffer.remaining() < this.currentNSPacketLength - 10) {
            n2 = this.readFromNetwork();
        }
        int n3 = this.inBuffer.limit();
        int n4 = this.inBuffer.position() + this.currentNSPacketLength - 10;
        this.inBuffer.limit(n4);
        this.currentNSPacketDataBuffer = this.inBuffer.slice();
        this.inBuffer.limit(n3);
        this.inBuffer.position(n4);
    }

    private void unmarshalOneNSPacket() throws IOException, InterruptedException {
        block5: {
            this.getNextNSPacket();
            if (!this.currentNSPacketDataBuffer.hasRemaining()) break block5;
            switch (this.currentNSPacketType) {
                case 1: {
                    byte[] byArray = new byte[]{0, 24, 0, 0, 2, 0, 0, 0, 1, 52, 0, 0, 8, 0, 127, -1, 1, 0, 0, 0, 0, 24, 65, 1};
                    this.outBuffer.clear();
                    this.outBuffer.put(byArray);
                    this.outBuffer.limit(24);
                    this.outBuffer.rewind();
                    this.channel.write(this.outBuffer);
                    break;
                }
                case 6: {
                    if (this.currentNSPacketDataBuffer.get(0) == -34 && this.currentNSPacketDataBuffer.get(1) == -83) {
                        byte[] byArray = new byte[]{0, 127, 0, 0, 6, 0, 0, 0, 0, 0, -34, -83, -66, -17, 0, 117, 10, 32, 1, 0, 0, 4, 0, 0, 4, 0, 3, 0, 0, 0, 0, 0, 4, 0, 5, 10, 32, 1, 0, 0, 2, 0, 6, 0, 31, 0, 14, 0, 1, -34, -83, -66, -17, 0, 3, 0, 0, 0, 2, 0, 4, 0, 1, 0, 1, 0, 2, 0, 0, 0, 0, 0, 4, 0, 5, 10, 32, 1, 0, 0, 2, 0, 6, -5, -1, 0, 2, 0, 2, 0, 0, 49, 106, 0, 4, 0, 5, 10, 32, 1, 0, 0, 1, 0, 2, 0, 0, 3, 0, 2, 0, 0, 0, 0, 0, 4, 0, 5, 10, 32, 1, 0, 0, 1, 0, 2, 0};
                        this.outBuffer.clear();
                        this.outBuffer.put(byArray);
                        this.outBuffer.limit(byArray.length);
                        this.outBuffer.rewind();
                        this.channel.write(this.outBuffer);
                        break;
                    }
                    this.unmarshalNSDataPacket();
                }
            }
        }
    }

    private void unmarshalNSDataPacket() throws IOException, InterruptedException {
        block19: {
            int n2;
            NTFRegistration[] nTFRegistrationArray;
            NTFAQEvent nTFAQEvent;
            int n3;
            block20: {
                short s2;
                short s3 = this.readShort();
                int n4 = this.readInt();
                byte by = this.readByte();
                int n5 = this.readInt();
                short s4 = this.readShort();
                if (this.charset == null || this.charset.getOracleId() != s4) {
                    this.charset = CharacterSet.make(s4);
                }
                byte by2 = this.readByte();
                int n6 = this.readInt();
                short s5 = this.readShort();
                byte by3 = this.readByte();
                int n7 = this.readInt();
                short s6 = this.readShort();
                int n8 = (n4 - 21) / 9;
                int[] nArray = new int[n8];
                for (int i2 = 0; i2 < n8; ++i2) {
                    byte by4 = this.readByte();
                    n3 = this.readInt();
                    byte[] byArray = new byte[n3];
                    this.readBuffer(byArray, 0, n3);
                    for (int i3 = 0; i3 < n3; ++i3) {
                        if (i3 >= 4) continue;
                        int n9 = i2;
                        nArray[n9] = nArray[n9] | (byArray[i3] & 0xFF) << 8 * (n3 - i3 - 1);
                    }
                }
                NTFDCNEvent nTFDCNEvent = null;
                nTFAQEvent = null;
                n3 = 0;
                short s7 = 0;
                nTFRegistrationArray = null;
                if (s3 >= 2) {
                    s2 = this.readShort();
                    nTFRegistrationArray = new NTFRegistration[nArray.length];
                    for (n2 = 0; n2 < nArray.length; ++n2) {
                        nTFRegistrationArray[n2] = this.ntfManager.getRegistration(nArray[n2]);
                        if (nTFRegistrationArray[n2] == null) continue;
                        n3 = nTFRegistrationArray[n2].getNamespace();
                        s7 = nTFRegistrationArray[n2].getDatabaseVersion();
                    }
                    if (n3 == 2) {
                        nTFDCNEvent = new NTFDCNEvent(this, s7);
                    } else if (n3 == 1) {
                        nTFAQEvent = new NTFAQEvent(this, s7);
                    } else if (n3 == 0) {
                    }
                }
                s2 = 0;
                if (s3 >= 3) {
                    n2 = this.readShort();
                    int n10 = this.readInt();
                    byte by5 = this.readByte();
                    int n11 = this.readInt();
                    s2 = this.readShort();
                    if (n3 == 2 && nTFDCNEvent != null) {
                        nTFDCNEvent.setAdditionalEventType(DatabaseChangeEvent.AdditionalEventType.getEventType(s2));
                        if (s2 == 1) {
                            nTFDCNEvent.setEventType(DatabaseChangeEvent.EventType.DEREG);
                        }
                    } else if (n3 == 1 && nTFAQEvent != null) {
                        nTFAQEvent.setAdditionalEventType(AQNotificationEvent.AdditionalEventType.getEventType(s2));
                        if (s2 == 1) {
                            nTFAQEvent.setEventType(AQNotificationEvent.EventType.DEREG);
                        }
                    }
                }
                if (s3 > 3) {
                }
                if (nTFRegistrationArray == null) break block19;
                if (n3 != 2) break block20;
                for (n2 = 0; n2 < nTFRegistrationArray.length; ++n2) {
                    if (nTFRegistrationArray[n2] == null || nTFDCNEvent == null) continue;
                    nTFRegistrationArray[n2].notify(nTFDCNEvent);
                }
                break block19;
            }
            if (n3 != 1) break block19;
            for (n2 = 0; n2 < nTFRegistrationArray.length; ++n2) {
                if (nTFRegistrationArray[n2] == null || nTFAQEvent == null) continue;
                nTFRegistrationArray[n2].notify(nTFAQEvent);
            }
        }
    }

    void closeThisConnection() {
        this.needsToBeClosed = true;
    }

    byte readByte() throws IOException, InterruptedException {
        byte by = 0;
        if (this.currentNSPacketDataBuffer.hasRemaining()) {
            by = this.currentNSPacketDataBuffer.get();
        } else {
            this.getNextNSPacket();
            by = this.currentNSPacketDataBuffer.get();
        }
        return by;
    }

    short readShort() throws IOException, InterruptedException {
        short s2 = 0;
        if (this.currentNSPacketDataBuffer.remaining() >= 2) {
            s2 = this.currentNSPacketDataBuffer.getShort();
        } else {
            int n2 = this.readByte() & 0xFF;
            int n3 = this.readByte() & 0xFF;
            s2 = (short)(n2 << 8 | n3);
        }
        return s2;
    }

    int readInt() throws IOException, InterruptedException {
        int n2 = 0;
        if (this.currentNSPacketDataBuffer.remaining() >= 4) {
            n2 = this.currentNSPacketDataBuffer.getInt();
        } else {
            int n3 = this.readByte() & 0xFF;
            int n4 = this.readByte() & 0xFF;
            int n5 = this.readByte() & 0xFF;
            int n6 = this.readByte() & 0xFF;
            n2 = n3 << 24 | n4 << 16 | n5 << 8 | n6;
        }
        return n2;
    }

    long readLong() throws IOException, InterruptedException {
        long l2 = 0L;
        if (this.currentNSPacketDataBuffer.remaining() >= 8) {
            l2 = this.currentNSPacketDataBuffer.getLong();
        } else {
            long l3 = this.readByte() & 0xFF;
            long l4 = this.readByte() & 0xFF;
            long l5 = this.readByte() & 0xFF;
            long l6 = this.readByte() & 0xFF;
            long l7 = this.readByte() & 0xFF;
            long l8 = this.readByte() & 0xFF;
            long l9 = this.readByte() & 0xFF;
            long l10 = this.readByte() & 0xFF;
            l2 = l3 << 56 | l4 << 48 | l5 << 40 | l6 << 32 | l7 << 24 | l8 << 16 | l9 << 8 | l10;
        }
        return l2;
    }

    void readBuffer(byte[] byArray, int n2, int n3) throws IOException, InterruptedException {
        if (this.currentNSPacketDataBuffer.remaining() >= n3) {
            this.currentNSPacketDataBuffer.get(byArray, n2, n3);
        } else {
            boolean bl = false;
            int n4 = 0;
            int n5 = 0;
            int n6 = this.currentNSPacketDataBuffer.remaining();
            this.currentNSPacketDataBuffer.get(byArray, n2, n6);
            n2 += n6;
            n4 += n6;
            while (!bl) {
                this.getNextNSPacket();
                n6 = this.currentNSPacketDataBuffer.remaining();
                n5 = Math.min(n6, n3 - n4);
                this.currentNSPacketDataBuffer.get(byArray, n2, n5);
                n2 += n5;
                if ((n4 += n5) != n3) continue;
                bl = true;
            }
        }
    }

    private String packetToString(ByteBuffer byteBuffer) throws IOException {
        int n2 = 0;
        char[] cArray = new char[8];
        StringBuffer stringBuffer = new StringBuffer();
        int n3 = byteBuffer.position();
        while (byteBuffer.hasRemaining()) {
            byte by = byteBuffer.get();
            String string = Integer.toHexString(by & 0xFF);
            if ((string = string.toUpperCase()).length() == 1) {
                string = "0" + string;
            }
            stringBuffer.append(string);
            stringBuffer.append(' ');
            cArray[n2] = by > 32 && by < 127 ? (int)by : 46;
            if (++n2 != 8) continue;
            stringBuffer.append('|');
            stringBuffer.append(cArray);
            stringBuffer.append('|');
            stringBuffer.append('\n');
            n2 = 0;
        }
        if (n2 != 0) {
            int n4;
            int n5 = 8 - n2;
            for (n4 = 0; n4 < n5 * 3; ++n4) {
                stringBuffer.append(' ');
            }
            stringBuffer.append('|');
            stringBuffer.append(cArray, 0, n2);
            for (n4 = 0; n4 < n5; ++n4) {
                stringBuffer.append(' ');
            }
            stringBuffer.append('|');
            stringBuffer.append('\n');
        }
        stringBuffer.append("\nEnd of Packet\n\n");
        byteBuffer.position(n3);
        return stringBuffer.toString();
    }

    private void validatePacketType() throws IOException {
        if (this.currentNSPacketType < 1 || this.currentNSPacketType > 19) {
            throw new IOException("Invalid NS packet type.");
        }
    }
}

