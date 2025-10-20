/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleDataFactory;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

public interface OracleResultSet
extends ResultSet {
    public ARRAY getARRAY(int var1) throws SQLException;

    public ARRAY getARRAY(String var1) throws SQLException;

    public BFILE getBfile(int var1) throws SQLException;

    public BFILE getBFILE(int var1) throws SQLException;

    public BFILE getBfile(String var1) throws SQLException;

    public BFILE getBFILE(String var1) throws SQLException;

    public BLOB getBLOB(int var1) throws SQLException;

    public BLOB getBLOB(String var1) throws SQLException;

    public CHAR getCHAR(int var1) throws SQLException;

    public CHAR getCHAR(String var1) throws SQLException;

    public CLOB getCLOB(int var1) throws SQLException;

    public CLOB getCLOB(String var1) throws SQLException;

    public OPAQUE getOPAQUE(int var1) throws SQLException;

    public OPAQUE getOPAQUE(String var1) throws SQLException;

    public INTERVALYM getINTERVALYM(int var1) throws SQLException;

    public INTERVALYM getINTERVALYM(String var1) throws SQLException;

    public INTERVALDS getINTERVALDS(int var1) throws SQLException;

    public INTERVALDS getINTERVALDS(String var1) throws SQLException;

    public TIMESTAMP getTIMESTAMP(int var1) throws SQLException;

    public TIMESTAMP getTIMESTAMP(String var1) throws SQLException;

    public TIMESTAMPTZ getTIMESTAMPTZ(int var1) throws SQLException;

    public TIMESTAMPTZ getTIMESTAMPTZ(String var1) throws SQLException;

    public TIMESTAMPLTZ getTIMESTAMPLTZ(int var1) throws SQLException;

    public TIMESTAMPLTZ getTIMESTAMPLTZ(String var1) throws SQLException;

    public ResultSet getCursor(int var1) throws SQLException;

    public ResultSet getCursor(String var1) throws SQLException;

    public CustomDatum getCustomDatum(int var1, CustomDatumFactory var2) throws SQLException;

    public ORAData getORAData(int var1, ORADataFactory var2) throws SQLException;

    public Object getObject(int var1, OracleDataFactory var2) throws SQLException;

    public Object getObject(String var1, OracleDataFactory var2) throws SQLException;

    public CustomDatum getCustomDatum(String var1, CustomDatumFactory var2) throws SQLException;

    public ORAData getORAData(String var1, ORADataFactory var2) throws SQLException;

    public DATE getDATE(int var1) throws SQLException;

    public DATE getDATE(String var1) throws SQLException;

    public NUMBER getNUMBER(int var1) throws SQLException;

    public NUMBER getNUMBER(String var1) throws SQLException;

    public Datum getOracleObject(int var1) throws SQLException;

    public Datum getOracleObject(String var1) throws SQLException;

    public RAW getRAW(int var1) throws SQLException;

    public RAW getRAW(String var1) throws SQLException;

    public REF getREF(int var1) throws SQLException;

    public REF getREF(String var1) throws SQLException;

    public ROWID getROWID(int var1) throws SQLException;

    public ROWID getROWID(String var1) throws SQLException;

    public STRUCT getSTRUCT(int var1) throws SQLException;

    public STRUCT getSTRUCT(String var1) throws SQLException;

    public void updateARRAY(int var1, ARRAY var2) throws SQLException;

    public void updateARRAY(String var1, ARRAY var2) throws SQLException;

    public void updateBfile(int var1, BFILE var2) throws SQLException;

    public void updateBFILE(int var1, BFILE var2) throws SQLException;

    public void updateBfile(String var1, BFILE var2) throws SQLException;

    public void updateBFILE(String var1, BFILE var2) throws SQLException;

    public void updateBLOB(int var1, BLOB var2) throws SQLException;

    public void updateBLOB(String var1, BLOB var2) throws SQLException;

    public void updateCHAR(int var1, CHAR var2) throws SQLException;

    public void updateCHAR(String var1, CHAR var2) throws SQLException;

    public void updateCLOB(int var1, CLOB var2) throws SQLException;

    public void updateCLOB(String var1, CLOB var2) throws SQLException;

    public void updateCustomDatum(int var1, CustomDatum var2) throws SQLException;

    public void updateORAData(int var1, ORAData var2) throws SQLException;

    public void updateCustomDatum(String var1, CustomDatum var2) throws SQLException;

    public void updateORAData(String var1, ORAData var2) throws SQLException;

    public void updateDATE(int var1, DATE var2) throws SQLException;

    public void updateDATE(String var1, DATE var2) throws SQLException;

    public void updateINTERVALYM(int var1, INTERVALYM var2) throws SQLException;

    public void updateINTERVALYM(String var1, INTERVALYM var2) throws SQLException;

    public void updateINTERVALDS(int var1, INTERVALDS var2) throws SQLException;

    public void updateINTERVALDS(String var1, INTERVALDS var2) throws SQLException;

    public void updateTIMESTAMP(int var1, TIMESTAMP var2) throws SQLException;

    public void updateTIMESTAMP(String var1, TIMESTAMP var2) throws SQLException;

    public void updateTIMESTAMPTZ(int var1, TIMESTAMPTZ var2) throws SQLException;

    public void updateTIMESTAMPTZ(String var1, TIMESTAMPTZ var2) throws SQLException;

    public void updateTIMESTAMPLTZ(int var1, TIMESTAMPLTZ var2) throws SQLException;

    public void updateTIMESTAMPLTZ(String var1, TIMESTAMPLTZ var2) throws SQLException;

    public void updateNUMBER(int var1, NUMBER var2) throws SQLException;

    public void updateNUMBER(String var1, NUMBER var2) throws SQLException;

    public void updateOracleObject(int var1, Datum var2) throws SQLException;

    public void updateOracleObject(String var1, Datum var2) throws SQLException;

    public void updateRAW(int var1, RAW var2) throws SQLException;

    public void updateRAW(String var1, RAW var2) throws SQLException;

    public void updateREF(int var1, REF var2) throws SQLException;

    public void updateREF(String var1, REF var2) throws SQLException;

    public void updateROWID(int var1, ROWID var2) throws SQLException;

    public void updateROWID(String var1, ROWID var2) throws SQLException;

    public void updateSTRUCT(int var1, STRUCT var2) throws SQLException;

    public void updateSTRUCT(String var1, STRUCT var2) throws SQLException;

    public boolean isFromResultSetCache() throws SQLException;

    public byte[] getCompileKey() throws SQLException;

    public byte[] getRuntimeKey() throws SQLException;

    public AuthorizationIndicator getAuthorizationIndicator(int var1) throws SQLException;

    public AuthorizationIndicator getAuthorizationIndicator(String var1) throws SQLException;

    public static enum AuthorizationIndicator {
        NONE,
        UNAUTHORIZED,
        UNKNOWN;

    }
}

