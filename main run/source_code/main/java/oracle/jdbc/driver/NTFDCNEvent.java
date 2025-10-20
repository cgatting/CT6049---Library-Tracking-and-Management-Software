/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.NTFConnection;
import oracle.jdbc.driver.NTFDCNQueryChanges;
import oracle.jdbc.driver.NTFDCNTableChanges;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFDCNEvent
extends DatabaseChangeEvent {
    private static final long serialVersionUID = -1176415051550965782L;
    private int notifVersion = 0;
    private int notifRegid = 0;
    private DatabaseChangeEvent.EventType eventType;
    private DatabaseChangeEvent.AdditionalEventType additionalEventType = DatabaseChangeEvent.AdditionalEventType.NONE;
    private String databaseName = null;
    private byte[] notifXid = new byte[8];
    private int notifScn1 = 0;
    private int notifScn2 = 0;
    private int numberOfTables = 0;
    private NTFDCNTableChanges[] tcdesc = null;
    private int numberOfQueries = 0;
    private NTFDCNQueryChanges[] qdesc = null;
    private long registrationId;
    private NTFConnection conn;
    private int csid;
    private boolean isReady = false;
    private ByteBuffer dataBuffer;
    private boolean isDeregistrationEvent = false;
    private short databaseVersion;
    private boolean isClientInitiatedConnection = false;

    NTFDCNEvent(long l2, byte[] byArray, short s2, int n2) {
        super(new Object());
        this.dataBuffer = ByteBuffer.wrap(byArray);
        this.databaseVersion = s2;
        this.isClientInitiatedConnection = true;
        this.registrationId = l2;
        this.csid = n2;
    }

    NTFDCNEvent(NTFConnection nTFConnection, short s2) throws IOException, InterruptedException {
        super(nTFConnection);
        this.conn = nTFConnection;
        this.csid = this.conn.charset.getOracleId();
        int n2 = this.conn.readInt();
        byte[] byArray = new byte[n2];
        this.conn.readBuffer(byArray, 0, n2);
        this.dataBuffer = ByteBuffer.wrap(byArray);
        this.databaseVersion = s2;
    }

    private void initEvent() {
        short s2;
        if (!this.isClientInitiatedConnection) {
            s2 = this.dataBuffer.get();
            int n2 = this.dataBuffer.getInt();
            byte[] byArray = new byte[n2];
            this.dataBuffer.get(byArray, 0, n2);
            String string = null;
            try {
                string = new String(byArray, "UTF-8");
                string = string.replaceFirst("CHNF", "");
                this.registrationId = Long.parseLong(string);
            }
            catch (Exception exception) {
            }
            byte by = this.dataBuffer.get();
            int n3 = this.dataBuffer.getInt();
            byte[] byArray2 = new byte[n3];
            this.dataBuffer.get(byArray2, 0, n3);
            byte by2 = this.dataBuffer.get();
            int n4 = this.dataBuffer.getInt();
        }
        if (this.dataBuffer.hasRemaining()) {
            this.notifVersion = this.dataBuffer.getShort();
            this.notifRegid = this.dataBuffer.getInt();
            this.eventType = DatabaseChangeEvent.EventType.getEventType(this.dataBuffer.getInt());
            s2 = this.dataBuffer.getShort();
            byte[] byArray = new byte[s2];
            this.dataBuffer.get(byArray, 0, s2);
            try {
                this.databaseName = new String(byArray, "UTF-8");
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.dataBuffer.get(this.notifXid);
            this.notifScn1 = this.dataBuffer.getInt();
            this.notifScn2 = this.dataBuffer.getShort();
            if (this.eventType == DatabaseChangeEvent.EventType.OBJCHANGE) {
                this.numberOfTables = this.dataBuffer.getShort();
                this.tcdesc = new NTFDCNTableChanges[this.numberOfTables];
                for (int i2 = 0; i2 < this.tcdesc.length; ++i2) {
                    this.tcdesc[i2] = new NTFDCNTableChanges(this.dataBuffer, this.csid);
                }
            } else if (this.eventType == DatabaseChangeEvent.EventType.QUERYCHANGE) {
                this.numberOfQueries = this.dataBuffer.getShort();
                this.qdesc = new NTFDCNQueryChanges[this.numberOfQueries];
                for (int i3 = 0; i3 < this.numberOfQueries; ++i3) {
                    this.qdesc[i3] = new NTFDCNQueryChanges(this.dataBuffer, this.csid);
                }
            }
        }
        this.isReady = true;
    }

    @Override
    public String getDatabaseName() {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.databaseName;
    }

    @Override
    public TableChangeDescription[] getTableChangeDescription() {
        if (!this.isReady) {
            this.initEvent();
        }
        if (this.eventType == DatabaseChangeEvent.EventType.OBJCHANGE) {
            return this.tcdesc;
        }
        return null;
    }

    @Override
    public QueryChangeDescription[] getQueryChangeDescription() {
        if (!this.isReady) {
            this.initEvent();
        }
        if (this.eventType == DatabaseChangeEvent.EventType.QUERYCHANGE) {
            return this.qdesc;
        }
        return null;
    }

    @Override
    public byte[] getTransactionId() {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.notifXid;
    }

    @Override
    public String getTransactionId(boolean bl) {
        long l2;
        int n2;
        int n3;
        if (!this.isReady) {
            this.initEvent();
        }
        if (!bl) {
            n3 = (this.notifXid[0] & 0xFF) << 8 | this.notifXid[1] & 0xFF;
            n2 = (this.notifXid[2] & 0xFF) << 8 | this.notifXid[3] & 0xFF;
            l2 = ((this.notifXid[4] & 0xFF) << 24 | (this.notifXid[5] & 0xFF) << 16 | (this.notifXid[6] & 0xFF) << 8 | this.notifXid[7] & 0xFF) & 0xFFFFFFFF;
        } else {
            n3 = (this.notifXid[1] & 0xFF) << 8 | this.notifXid[0] & 0xFF;
            n2 = (this.notifXid[3] & 0xFF) << 8 | this.notifXid[2] & 0xFF;
            l2 = ((this.notifXid[7] & 0xFF) << 24 | (this.notifXid[6] & 0xFF) << 16 | (this.notifXid[5] & 0xFF) << 8 | this.notifXid[4] & 0xFF) & 0xFFFFFFFF;
        }
        String string = "" + n3 + "." + n2 + "." + l2;
        return string;
    }

    void setEventType(DatabaseChangeEvent.EventType eventType) throws IOException {
        if (!this.isReady) {
            this.initEvent();
        }
        this.eventType = eventType;
        if (this.eventType == DatabaseChangeEvent.EventType.DEREG) {
            this.isDeregistrationEvent = true;
        }
    }

    void setAdditionalEventType(DatabaseChangeEvent.AdditionalEventType additionalEventType) {
        this.additionalEventType = additionalEventType;
    }

    @Override
    public DatabaseChangeEvent.EventType getEventType() {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.eventType;
    }

    @Override
    public DatabaseChangeEvent.AdditionalEventType getAdditionalEventType() {
        return this.additionalEventType;
    }

    boolean isDeregistrationEvent() {
        return this.isDeregistrationEvent;
    }

    @Override
    public String getConnectionInformation() {
        if (this.conn == null) {
            return null;
        }
        return this.conn.connectionDescription;
    }

    @Override
    public int getRegistrationId() {
        if (!this.isReady) {
            this.initEvent();
        }
        return (int)this.registrationId;
    }

    @Override
    public long getRegId() {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.registrationId;
    }

    @Override
    @DisableTrace
    public String toString() {
        QueryChangeDescription[] queryChangeDescriptionArray;
        TableChangeDescription[] tableChangeDescriptionArray;
        if (!this.isReady) {
            this.initEvent();
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (this.conn != null) {
            stringBuffer.append("Connection information  : " + this.conn.connectionDescription + "\n");
        }
        stringBuffer.append("Registration ID         : " + this.registrationId + "\n");
        stringBuffer.append("Notification version    : " + this.notifVersion + "\n");
        stringBuffer.append("Event type              : " + (Object)((Object)this.eventType) + "\n");
        if (this.additionalEventType != DatabaseChangeEvent.AdditionalEventType.NONE) {
            stringBuffer.append("Additional event type   : " + (Object)((Object)this.additionalEventType) + "\n");
        }
        if (this.databaseName != null) {
            stringBuffer.append("Database name           : " + this.databaseName + "\n");
        }
        if ((tableChangeDescriptionArray = this.getTableChangeDescription()) != null) {
            stringBuffer.append("Table Change Description (length=" + this.numberOfTables + ")\n");
            for (int i2 = 0; i2 < tableChangeDescriptionArray.length; ++i2) {
                stringBuffer.append(tableChangeDescriptionArray[i2].toString());
            }
        }
        if ((queryChangeDescriptionArray = this.getQueryChangeDescription()) != null) {
            stringBuffer.append("Query Change Description (length=" + this.numberOfQueries + ")\n");
            for (int i3 = 0; i3 < queryChangeDescriptionArray.length; ++i3) {
                stringBuffer.append(queryChangeDescriptionArray[i3].toString());
            }
        }
        return stringBuffer.toString();
    }

    @Log
    protected void debug(Logger logger, Level level, Executable executable, String string) {
        ClioSupport.log(logger, level, this.getClass(), executable, string);
    }
}

