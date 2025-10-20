/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.json.JsonValue
 */
package oracle.jdbc.driver;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.Map;
import javax.json.JsonValue;
import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleBfile;
import oracle.jdbc.OracleBlob;
import oracle.jdbc.OracleClob;
import oracle.jdbc.OracleNClob;
import oracle.jdbc.OracleOpaque;
import oracle.jdbc.OracleRef;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleResultSetMetaData;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleNamedType;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.ROWID;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.METADATA})
class OracleResultSetMetaData
implements oracle.jdbc.internal.OracleResultSetMetaData {
    PhysicalConnection connection;
    OracleStatement statement;
    int offsetOfFirstUserColumn;
    Object acProxy;

    public OracleResultSetMetaData() {
    }

    OracleResultSetMetaData(PhysicalConnection physicalConnection, OracleStatement oracleStatement, int n2) throws SQLException {
        this.connection = physicalConnection;
        this.statement = oracleStatement;
        oracleStatement.describe();
        oracleStatement.computeOffsetOfFirstUserColumn();
        oracleStatement.computeNumberOfUserColumns();
        this.offsetOfFirstUserColumn = n2;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.statement.getNumberOfUserColumns();
    }

    @Override
    public boolean isAutoIncrement(int n2) throws SQLException {
        return false;
    }

    int getValidColumnIndex(int n2) throws SQLException {
        if (n2 <= 0 || n2 > this.statement.getNumberOfUserColumns()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getValidColumnIndex").fillInStackTrace();
        }
        int n3 = n2 + this.offsetOfFirstUserColumn;
        return n3;
    }

    @Override
    public boolean isCaseSensitive(int n2) throws SQLException {
        int n3 = this.getColumnType(n2);
        return n3 == 1 || n3 == 12 || n3 == -1 || n3 == -15 || n3 == -9 || n3 == 2005 || n3 == 2011 || n3 == 2009;
    }

    @Override
    public boolean isSearchable(int n2) throws SQLException {
        int n3 = this.getColumnType(n2);
        return n3 != -4 && n3 != -1 && n3 != 2004 && n3 != 2005 && n3 != -13 && n3 != 2011 && n3 != 2016 && n3 != 2002 && n3 != 2008 && n3 != 2007 && n3 != 2003 && n3 != 2006 && n3 != -10 && n3 != 2012;
    }

    @Override
    public boolean isCurrency(int n2) throws SQLException {
        int n3 = this.getColumnType(n2);
        return n3 == 2 || n3 == 6;
    }

    @Override
    public int isNullable(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.getDescription()[n3].nullable ? 1 : 0;
    }

    @Override
    public boolean isSigned(int n2) throws SQLException {
        return true;
    }

    @Override
    public int getColumnDisplaySize(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].describeType;
        switch (n4) {
            case 2: {
                int n5 = this.getPrecision(n2);
                int n6 = this.getDescription()[n3].scale;
                if (n5 != 0 && n6 == -127) {
                    n5 = (int)((double)n5 / 3.32193);
                    n6 = 1;
                } else {
                    if (n5 == 0) {
                        n5 = 38;
                    }
                    if (n6 == -127) {
                        n6 = 0;
                    }
                }
                int n7 = n5 + (n6 != 0 ? 1 : 0) + 1;
                return n7;
            }
            case 1: 
            case 96: {
                Accessor accessor = this.getDescription()[n3];
                if (this.statement.connection.protocolId == 3) {
                    return accessor.describeMaxLengthChars;
                }
                if (accessor.describeMaxLengthChars <= 0) break;
                if (accessor.definedColumnSize > 0) {
                    return Math.min(accessor.describeMaxLengthChars, accessor.definedColumnSize);
                }
                return accessor.describeMaxLengthChars;
            }
        }
        return this.getDescription()[n3].describeMaxLength;
    }

    @Override
    public String getColumnLabel(int n2) throws SQLException {
        return this.getColumnName(n2);
    }

    @Override
    public String getColumnName(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.statement.getDescriptionWithNames()[n3].columnName;
    }

    @Override
    public String getSchemaName(int n2) throws SQLException {
        return "";
    }

    @Override
    public int getPrecision(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].describeType;
        switch (n4) {
            case 112: 
            case 113: 
            case 119: {
                return -1;
            }
            case 8: 
            case 24: {
                return Integer.MAX_VALUE;
            }
            case 1: 
            case 96: {
                Accessor accessor = this.getDescription()[n3];
                if (this.statement.connection.protocolId == 3) {
                    return accessor.describeMaxLengthChars;
                }
                if (accessor.describeMaxLengthChars > 0) {
                    if (accessor.definedColumnSize > 0) {
                        return Math.min(accessor.describeMaxLengthChars, accessor.definedColumnSize);
                    }
                    return accessor.describeMaxLengthChars;
                }
                return accessor.describeMaxLength;
            }
        }
        return this.getDescription()[n3].precision;
    }

    @Override
    public OracleResultSetMetaData.SecurityAttribute getSecurityAttribute(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.getDescription()[n3].securityAttribute;
    }

    @Override
    public int getScale(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].scale;
        return n4 == -127 && this.statement.connection.j2ee13Compliant ? 0 : n4;
    }

    @Override
    public boolean isVariableScale(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].scale;
        return n4 == -127;
    }

    @Override
    public String getTableName(int n2) throws SQLException {
        return "";
    }

    @Override
    public String getCatalogName(int n2) throws SQLException {
        return "";
    }

    @Override
    public int getColumnType(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].describeType;
        switch (n4) {
            case 96: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return -15;
                }
                return 1;
            }
            case 1: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return -9;
                }
                return 12;
            }
            case 8: {
                return -1;
            }
            case 2: 
            case 6: {
                if (this.statement.connection.j2ee13Compliant && this.getDescription()[n3].precision != 0 && this.getDescription()[n3].scale == -127) {
                    return 6;
                }
                return 2;
            }
            case 100: {
                return 100;
            }
            case 101: {
                return 101;
            }
            case 23: {
                return -3;
            }
            case 24: {
                return -4;
            }
            case 104: 
            case 208: {
                return -8;
            }
            case 102: {
                return -10;
            }
            case 12: {
                return this.connection.mapDateToTimestamp ? 93 : 91;
            }
            case 180: {
                return 93;
            }
            case 181: {
                return -101;
            }
            case 231: {
                return -102;
            }
            case 113: {
                return 2004;
            }
            case 119: {
                return 2016;
            }
            case 112: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return 2011;
                }
                return 2005;
            }
            case 114: {
                return -13;
            }
            case 109: {
                TypeDescriptor typeDescriptor = TypeDescriptor.getTypeDescriptor(this.getDescription()[n3].describeTypeName, this.connection);
                if (typeDescriptor != null) {
                    return typeDescriptor.getTypeCode();
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 60).fillInStackTrace();
            }
            case 111: {
                return 2006;
            }
            case 182: {
                return -103;
            }
            case 183: {
                return -104;
            }
        }
        return 1111;
    }

    @Override
    public String getColumnTypeName(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].describeType;
        switch (n4) {
            case 96: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return "NCHAR";
                }
                return "CHAR";
            }
            case 1: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return "NVARCHAR2";
                }
                return "VARCHAR2";
            }
            case 8: {
                return "LONG";
            }
            case 2: 
            case 6: {
                if (this.statement.connection.j2ee13Compliant && this.getDescription()[n3].precision != 0 && this.getDescription()[n3].scale == -127) {
                    return "FLOAT";
                }
                return "NUMBER";
            }
            case 100: {
                return "BINARY_FLOAT";
            }
            case 101: {
                return "BINARY_DOUBLE";
            }
            case 23: {
                return "RAW";
            }
            case 24: {
                return "LONG RAW";
            }
            case 104: 
            case 208: {
                return "ROWID";
            }
            case 102: {
                return "REFCURSOR";
            }
            case 12: {
                return "DATE";
            }
            case 180: {
                return "TIMESTAMP";
            }
            case 181: {
                return "TIMESTAMP WITH TIME ZONE";
            }
            case 231: {
                return "TIMESTAMP WITH LOCAL TIME ZONE";
            }
            case 113: {
                return "BLOB";
            }
            case 119: {
                return "JSON";
            }
            case 112: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return "NCLOB";
                }
                return "CLOB";
            }
            case 114: {
                return "BFILE";
            }
            case 109: {
                OracleTypeADT oracleTypeADT = (OracleTypeADT)this.getDescription()[n3].describeOtype;
                return oracleTypeADT.getFullName();
            }
            case 111: {
                OracleTypeADT oracleTypeADT = (OracleTypeADT)this.getDescription()[n3].describeOtype;
                return oracleTypeADT.getFullName();
            }
            case 182: {
                return "INTERVALYM";
            }
            case 183: {
                return "INTERVALDS";
            }
        }
        return null;
    }

    @Override
    public boolean isReadOnly(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean isWritable(int n2) throws SQLException {
        return true;
    }

    @Override
    public boolean isDefinitelyWritable(int n2) throws SQLException {
        return false;
    }

    @Override
    public String getColumnClassName(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        int n4 = this.getDescription()[n3].describeType;
        switch (n4) {
            case 1: 
            case 8: 
            case 96: 
            case 999: {
                return String.class.getName();
            }
            case 2: 
            case 6: {
                if (this.getDescription()[n3].precision != 0 && this.getDescription()[n3].scale == -127) {
                    return Double.class.getName();
                }
                return BigDecimal.class.getName();
            }
            case 23: 
            case 24: {
                return byte[].class.getName();
            }
            case 12: {
                return Timestamp.class.getName();
            }
            case 180: {
                if (this.statement.connection.j2ee13Compliant) {
                    return Timestamp.class.getName();
                }
                return TIMESTAMP.class.getName();
            }
            case 181: {
                return TIMESTAMPTZ.class.getName();
            }
            case 231: {
                return TIMESTAMPLTZ.class.getName();
            }
            case 182: {
                return INTERVALYM.class.getName();
            }
            case 183: {
                return INTERVALDS.class.getName();
            }
            case 104: 
            case 208: {
                return ROWID.class.getName();
            }
            case 113: {
                return OracleBlob.class.getName();
            }
            case 112: {
                if (this.getDescription()[n3].formOfUse == 2) {
                    return OracleNClob.class.getName();
                }
                return OracleClob.class.getName();
            }
            case 114: {
                return OracleBfile.class.getName();
            }
            case 102: {
                return OracleResultSet.class.getName();
            }
            case 109: {
                switch (this.getColumnType(n2)) {
                    case 2003: {
                        return OracleArray.class.getName();
                    }
                    case 2007: {
                        return OracleOpaque.class.getName();
                    }
                    case 2008: {
                        Class<?> clazz;
                        OracleNamedType oracleNamedType = (OracleNamedType)this.getDescription()[n3].describeOtype;
                        Map<String, Class<?>> map = this.connection.getJavaObjectTypeMap();
                        if (map != null && (clazz = map.get(oracleNamedType.getFullName())) != null) {
                            return clazz.getName();
                        }
                        return StructDescriptor.getJavaObjectClassName(this.connection, oracleNamedType.getSchemaName(), oracleNamedType.getSimpleName());
                    }
                    case 2002: {
                        Class<?> clazz;
                        Map<String, Class<?>> map = this.connection.getTypeMap();
                        if (map != null && (clazz = map.get(((OracleNamedType)this.getDescription()[n3].describeOtype).getFullName())) != null) {
                            return clazz.getName();
                        }
                        return OracleStruct.class.getName();
                    }
                    case 2009: {
                        return SQLXML.class.getName();
                    }
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1).fillInStackTrace();
            }
            case 111: {
                return OracleRef.class.getName();
            }
            case 101: {
                return Double.class.getName();
            }
            case 100: {
                return Float.class.getName();
            }
            case 119: {
                try {
                    return JsonValue.class.getName();
                }
                catch (NoClassDefFoundError noClassDefFoundError) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isNCHAR(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.getDescription()[n3].formOfUse == 2;
    }

    @Override
    public boolean isColumnInvisible(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.getDescription()[n3].isColumnInvisible();
    }

    @Override
    public boolean isColumnJSON(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.getDescription()[n3].isColumnJSON();
    }

    Accessor[] getDescription() throws SQLException {
        return this.statement.getDescription();
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.connection;
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }
}

