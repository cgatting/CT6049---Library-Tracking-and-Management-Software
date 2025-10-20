/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.xdb.XMLType
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.driver.AQMessagePropertiesI;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.OracleStruct;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.replay.driver.TxnReplayableStruct;
import oracle.sql.ANYDATA;
import oracle.sql.OPAQUE;
import oracle.sql.OpaqueDescriptor;
import oracle.sql.RAW;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TypeDescriptor;
import oracle.sql.json.OracleJsonDatum;
import oracle.xdb.XMLType;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class AQMessageI
implements AQMessage {
    private byte[] id = null;
    private AQMessagePropertiesI properties = null;
    private byte[] toid = null;
    private int version = 1;
    private byte[] payload;
    private OracleStruct payLoadStruct;
    private ANYDATA payLoadANYDATA;
    private RAW payLoadRAW;
    private XMLType payLoadXMLType;
    private OracleJsonDatum payloadJson;
    private Connection conn;
    private String typeName;
    private TypeDescriptor sd;

    AQMessageI(AQMessagePropertiesI aQMessagePropertiesI, Connection connection) {
        this.properties = aQMessagePropertiesI;
        this.conn = connection;
    }

    AQMessageI(AQMessagePropertiesI aQMessagePropertiesI) throws SQLException {
        this.properties = aQMessagePropertiesI;
    }

    void setTypeName(String string) {
        this.typeName = string;
    }

    void setTypeDescriptor(TypeDescriptor typeDescriptor) {
        this.sd = typeDescriptor;
    }

    @Override
    public byte[] getMessageId() {
        return this.id;
    }

    void setMessageId(byte[] byArray) throws SQLException {
        this.id = byArray;
    }

    @Override
    public AQMessageProperties getMessageProperties() {
        return this.properties;
    }

    AQMessagePropertiesI getMessagePropertiesI() {
        return this.properties;
    }

    @Override
    public void setPayload(byte[] byArray) throws SQLException {
        this.payload = byArray;
        this.toid = TypeDescriptor.RAWTOID;
    }

    @Override
    public void setPayload(byte[] byArray, byte[] byArray2) throws SQLException {
        this.payload = byArray;
        this.toid = byArray2;
    }

    @Override
    public void setPayload(Struct struct) throws SQLException {
        assert (struct instanceof OracleStruct) : "_payload is " + struct;
        this.payload = ((OracleStruct)struct).toBytes();
        this.payLoadStruct = (OracleStruct)struct;
        this.toid = ((OracleStruct)struct).getDescriptor().getOracleTypeADT().getTOID();
        this.version = ((OracleStruct)struct).getDescriptor().getOracleTypeADT().getTypeVersion();
    }

    @Override
    public void setPayload(STRUCT sTRUCT) throws SQLException {
        this.payload = sTRUCT.toBytes();
        this.payLoadStruct = sTRUCT;
        this.toid = sTRUCT.getDescriptor().getOracleTypeADT().getTOID();
        this.version = sTRUCT.getDescriptor().getOracleTypeADT().getTypeVersion();
    }

    @Override
    public void setPayload(ANYDATA aNYDATA) throws SQLException {
        this.payload = aNYDATA.toDatum(this.conn).shareBytes();
        this.payLoadANYDATA = aNYDATA;
        this.toid = TypeDescriptor.ANYDATATOID;
    }

    @Override
    public void setPayload(RAW rAW) throws SQLException {
        this.payload = rAW.shareBytes();
        this.payLoadRAW = rAW;
        this.toid = TypeDescriptor.RAWTOID;
    }

    @Override
    public void setPayload(XMLType xMLType) throws SQLException {
        this.payload = xMLType.toBytes();
        this.payLoadXMLType = xMLType;
        this.toid = TypeDescriptor.XMLTYPETOID;
    }

    @Override
    public void setPayload(OracleJsonDatum oracleJsonDatum) {
        this.payload = oracleJsonDatum.shareBytes();
        this.payloadJson = oracleJsonDatum;
        this.toid = TypeDescriptor.JSONTOID;
    }

    @Override
    public byte[] getPayload() {
        return this.payload;
    }

    @Override
    public RAW getRAWPayload() throws SQLException {
        RAW rAW = null;
        if (this.payLoadRAW != null) {
            rAW = this.payLoadRAW;
        } else if (this.isRAWPayload()) {
            rAW = this.payLoadRAW = new RAW(this.payload);
        } else {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 193).fillInStackTrace();
        }
        return rAW;
    }

    @Override
    public boolean isRAWPayload() throws SQLException {
        if (this.toid == null || this.toid.length != 16) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 252).fillInStackTrace();
        }
        return AQMessageI.compareToid(this.toid, TypeDescriptor.RAWTOID);
    }

    @Override
    public OracleJsonDatum getJSONPayload() throws SQLException {
        OracleJsonDatum oracleJsonDatum = null;
        if (this.payloadJson != null) {
            oracleJsonDatum = this.payloadJson;
        } else if (this.isJSONPayload()) {
            oracleJsonDatum = this.payloadJson = new OracleJsonDatum(this.payload);
        } else {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 193).fillInStackTrace();
        }
        return oracleJsonDatum;
    }

    @Override
    public boolean isJSONPayload() throws SQLException {
        if (this.toid == null || this.toid.length != 16) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 252).fillInStackTrace();
        }
        return AQMessageI.compareToid(this.toid, TypeDescriptor.JSONTOID);
    }

    @Override
    public Struct getStructPayload() throws SQLException {
        return this.getSTRUCTPayload();
    }

    @Override
    public STRUCT getSTRUCTPayload() throws SQLException {
        STRUCT sTRUCT = null;
        if (!this.isSTRUCTPayload()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 193).fillInStackTrace();
        }
        if (this.payLoadStruct != null) {
            sTRUCT = this.payLoadStruct instanceof STRUCT ? (STRUCT)this.payLoadStruct : (STRUCT)((TxnReplayableStruct)((Object)this.payLoadStruct)).getDelegateStruct();
        } else {
            if (this.sd == null) {
                this.typeName = OracleTypeADT.toid2typename(this.conn, this.toid);
                this.sd = TypeDescriptor.getTypeDescriptor(this.typeName, (OracleConnection)this.conn);
            }
            if (this.sd instanceof StructDescriptor) {
                sTRUCT = new STRUCT((StructDescriptor)this.sd, this.payload, this.conn);
                this.payLoadStruct = sTRUCT;
            } else {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 193).fillInStackTrace();
            }
        }
        return sTRUCT;
    }

    @Override
    public boolean isSTRUCTPayload() throws SQLException {
        if (this.toid == null || this.toid.length != 16) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 252).fillInStackTrace();
        }
        boolean bl = true;
        boolean bl2 = true;
        for (int i2 = 0; i2 < 15; ++i2) {
            if (this.toid[i2] == 0) continue;
            bl2 = false;
            break;
        }
        if (bl2 || this.isRAWPayload() || this.isANYDATAPayload()) {
            bl = false;
        }
        return bl;
    }

    @Override
    public ANYDATA getANYDATAPayload() throws SQLException {
        ANYDATA aNYDATA = null;
        if (this.payLoadANYDATA != null) {
            aNYDATA = this.payLoadANYDATA;
        } else if (this.isANYDATAPayload()) {
            OpaqueDescriptor opaqueDescriptor = OpaqueDescriptor.createDescriptor("SYS.ANYDATA", this.conn);
            OPAQUE oPAQUE = new OPAQUE(opaqueDescriptor, this.payload, this.conn);
            aNYDATA = this.payLoadANYDATA = new ANYDATA(oPAQUE);
        } else {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 193).fillInStackTrace();
        }
        return aNYDATA;
    }

    @Override
    public boolean isANYDATAPayload() throws SQLException {
        if (this.toid == null || this.toid.length != 16) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 252).fillInStackTrace();
        }
        return this.typeName != null && this.typeName.equals("SYS.ANYDATA") || AQMessageI.compareToid(this.toid, TypeDescriptor.ANYDATATOID);
    }

    @Override
    public XMLType getXMLTypePayload() throws SQLException {
        XMLType xMLType = null;
        if (this.payLoadXMLType != null) {
            xMLType = this.payLoadXMLType;
        } else if (this.isXMLTypePayload()) {
            OpaqueDescriptor opaqueDescriptor = OpaqueDescriptor.createDescriptor("SYS.XMLTYPE", this.conn);
            OPAQUE oPAQUE = new OPAQUE(opaqueDescriptor, this.payload, this.conn);
            xMLType = this.payLoadXMLType = XMLType.createXML((OPAQUE)oPAQUE);
        } else {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 193).fillInStackTrace();
        }
        return xMLType;
    }

    @Override
    public boolean isXMLTypePayload() throws SQLException {
        if (this.toid == null || this.toid.length != 16) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 252).fillInStackTrace();
        }
        return this.typeName != null && this.typeName.equals("SYS.XMLTYPE") || AQMessageI.compareToid(this.toid, TypeDescriptor.XMLTYPETOID);
    }

    @Override
    public byte[] getPayloadTOID() {
        return this.toid;
    }

    public int getPayloadVersion() {
        return this.version;
    }

    static boolean compareToid(byte[] byArray, byte[] byArray2) {
        boolean bl = false;
        if (byArray != null) {
            if (byArray == byArray2) {
                bl = true;
            } else if (byArray.length == byArray2.length) {
                boolean bl2 = true;
                for (int i2 = 0; i2 < byArray.length; ++i2) {
                    if (byArray[i2] == byArray2[i2]) continue;
                    bl2 = false;
                    break;
                }
                if (bl2) {
                    bl = true;
                }
            }
        }
        return bl;
    }

    @Override
    @DisableTrace
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Message Properties={");
        stringBuffer.append(this.properties);
        stringBuffer.append("} ");
        return stringBuffer.toString();
    }

    protected oracle.jdbc.internal.OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

