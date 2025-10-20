/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import oracle.jdbc.driver.NTFConnection;
import oracle.jdbc.driver.NTFManager;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHANGE_NOTIFICATION})
class NTFListener
extends Thread {
    private ArrayList<NTFConnection> connections;
    private final Monitor connectionsMonitor = Monitor.newInstance();
    private int nbOfConnections = 0;
    private boolean needsToBeClosed = false;
    NTFManager dcnManager;
    ServerSocketChannel ssChannel;
    int tcpport;
    public Properties socketOptions;
    private Exception[] connectionCreationExceptionArr;

    NTFListener(NTFManager nTFManager, ServerSocketChannel serverSocketChannel, int n2, @Blind(value=PropertiesBlinder.class) Properties properties, Exception[] exceptionArray) {
        this.dcnManager = nTFManager;
        this.connections = new ArrayList(10);
        this.ssChannel = serverSocketChannel;
        this.tcpport = n2;
        this.socketOptions = properties;
        this.connectionCreationExceptionArr = exceptionArray;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            this.ssChannel.register(selector, 16);
            block13: while (true) {
                selector.select();
                if (this.needsToBeClosed) break;
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (true) {
                    block20: {
                        if (!iterator.hasNext()) continue block13;
                        SelectionKey selectionKey = iterator.next();
                        if ((selectionKey.readyOps() & 0x10) != 16) continue;
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        try {
                            NTFConnection nTFConnection = new NTFConnection(this.dcnManager, socketChannel, this);
                            try (Monitor.CloseableLock closeableLock = this.connectionsMonitor.acquireCloseableLock();){
                                this.connections.add(nTFConnection);
                            }
                            nTFConnection.start();
                            if (this.connectionCreationExceptionArr != null && this.connectionCreationExceptionArr.length > 0) {
                                this.connectionCreationExceptionArr[0] = null;
                            }
                        }
                        catch (IOException iOException) {
                            if (this.connectionCreationExceptionArr == null || this.connectionCreationExceptionArr.length <= 0) break block20;
                            this.connectionCreationExceptionArr[0] = iOException;
                        }
                    }
                    iterator.remove();
                }
                break;
            }
            selector.close();
            this.ssChannel.close();
        }
        catch (IOException iOException) {
        }
    }

    void closeThisListener() {
        try (Monitor.CloseableLock closeableLock = this.connectionsMonitor.acquireCloseableLock();){
            for (NTFConnection nTFConnection : this.connections) {
                nTFConnection.closeThisConnection();
                nTFConnection.interrupt();
            }
            this.needsToBeClosed = true;
        }
    }

    void releaseConnection(NTFConnection nTFConnection) {
        try (Monitor.CloseableLock closeableLock = this.connectionsMonitor.acquireCloseableLock();){
            this.connections.remove(nTFConnection);
        }
    }

    Exception getRegistrationException() {
        if (this.connectionCreationExceptionArr != null && this.connectionCreationExceptionArr.length > 0) {
            return this.connectionCreationExceptionArr[0];
        }
        return null;
    }
}

