/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.xdb.XMLType
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import java.sql.Struct;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.sql.ANYDATA;
import oracle.sql.RAW;
import oracle.sql.STRUCT;
import oracle.sql.json.OracleJsonDatum;
import oracle.xdb.XMLType;

public interface AQMessage {
    public byte[] getMessageId() throws SQLException;

    public AQMessageProperties getMessageProperties() throws SQLException;

    public void setPayload(byte[] var1) throws SQLException;

    public void setPayload(byte[] var1, byte[] var2) throws SQLException;

    public void setPayload(Struct var1) throws SQLException;

    public void setPayload(STRUCT var1) throws SQLException;

    public void setPayload(ANYDATA var1) throws SQLException;

    public void setPayload(RAW var1) throws SQLException;

    public void setPayload(XMLType var1) throws SQLException;

    public void setPayload(OracleJsonDatum var1) throws SQLException;

    public byte[] getPayload() throws SQLException;

    public byte[] getPayloadTOID();

    public STRUCT getSTRUCTPayload() throws SQLException;

    public Struct getStructPayload() throws SQLException;

    public boolean isSTRUCTPayload() throws SQLException;

    public ANYDATA getANYDATAPayload() throws SQLException;

    public boolean isANYDATAPayload() throws SQLException;

    public RAW getRAWPayload() throws SQLException;

    public boolean isRAWPayload() throws SQLException;

    public XMLType getXMLTypePayload() throws SQLException;

    public boolean isXMLTypePayload() throws SQLException;

    public OracleJsonDatum getJSONPayload() throws SQLException;

    public boolean isJSONPayload() throws SQLException;

    public String toString();
}

