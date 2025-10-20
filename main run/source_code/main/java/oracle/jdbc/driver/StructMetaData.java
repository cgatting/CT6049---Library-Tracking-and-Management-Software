/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleBfile;
import oracle.jdbc.OracleBlob;
import oracle.jdbc.OracleClob;
import oracle.jdbc.OracleNClob;
import oracle.jdbc.OracleOpaque;
import oracle.jdbc.OracleRef;
import oracle.jdbc.OracleResultSetMetaData;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleType;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCHAR;
import oracle.jdbc.oracore.OracleTypeFLOAT;
import oracle.jdbc.oracore.OracleTypeNUMBER;
import oracle.jdbc.oracore.OracleTypeRAW;
import oracle.jdbc.oracore.OracleTypeREF;
import oracle.sql.StructDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class StructMetaData
implements oracle.jdbc.internal.StructMetaData {
    StructDescriptor descriptor;
    OracleTypeADT otype;
    OracleType[] types;

    public StructMetaData(StructDescriptor structDescriptor) throws SQLException {
        if (structDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "illegal operation: descriptor is null").fillInStackTrace();
        }
        this.descriptor = structDescriptor;
        this.otype = structDescriptor.getOracleTypeADT();
        this.types = this.otype.getAttrTypes();
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.types.length;
    }

    @Override
    public boolean isAutoIncrement(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean isSearchable(int n2) throws SQLException {
        return false;
    }

    @Override
    public OracleResultSetMetaData.SecurityAttribute getSecurityAttribute(int n2) throws SQLException {
        return OracleResultSetMetaData.SecurityAttribute.NONE;
    }

    @Override
    public boolean isCurrency(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3] instanceof OracleTypeNUMBER || this.types[n3] instanceof OracleTypeFLOAT;
    }

    @Override
    public boolean isCaseSensitive(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3] instanceof OracleTypeCHAR;
    }

    @Override
    public int isNullable(int n2) throws SQLException {
        return 1;
    }

    @Override
    public boolean isSigned(int n2) throws SQLException {
        return true;
    }

    @Override
    public int getColumnDisplaySize(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        if (this.types[n3] instanceof OracleTypeCHAR) {
            return ((OracleTypeCHAR)this.types[n3]).getLength();
        }
        if (this.types[n3] instanceof OracleTypeRAW) {
            return ((OracleTypeRAW)this.types[n3]).getLength();
        }
        return 0;
    }

    @Override
    public String getColumnLabel(int n2) throws SQLException {
        return this.getColumnName(n2);
    }

    @Override
    public String getColumnName(int n2) throws SQLException {
        return this.otype.getAttributeName(n2);
    }

    @Override
    public String getSchemaName(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        if (this.types[n3] instanceof OracleTypeADT) {
            return ((OracleTypeADT)this.types[n3]).getSchemaName();
        }
        return "";
    }

    @Override
    public int getPrecision(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3].getPrecision();
    }

    @Override
    public int getScale(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3].getScale();
    }

    @Override
    public boolean isVariableScale(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3].getScale() == -127;
    }

    @Override
    public String getTableName(int n2) throws SQLException {
        return null;
    }

    @Override
    public String getCatalogName(int n2) throws SQLException {
        return null;
    }

    @Override
    public int getColumnType(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3].getTypeCode();
    }

    @Override
    public String getColumnTypeName(int n2) throws SQLException {
        int n3 = this.getColumnType(n2);
        int n4 = this.getValidColumnIndex(n2);
        switch (n3) {
            case 12: {
                return "VARCHAR";
            }
            case 1: {
                return "CHAR";
            }
            case -2: {
                return "RAW";
            }
            case 6: {
                return "FLOAT";
            }
            case 2: {
                return "NUMBER";
            }
            case 8: {
                return "DOUBLE";
            }
            case 3: {
                return "DECIMAL";
            }
            case 100: {
                return "BINARY_FLOAT";
            }
            case 101: {
                return "BINARY_DOUBLE";
            }
            case 91: {
                return "DATE";
            }
            case -104: {
                return "INTERVALDS";
            }
            case -103: {
                return "INTERVALYM";
            }
            case 93: {
                return "TIMESTAMP";
            }
            case -101: {
                return "TIMESTAMP WITH TIME ZONE";
            }
            case -102: {
                return "TIMESTAMP WITH LOCAL TIME ZONE";
            }
            case 2004: {
                return "BLOB";
            }
            case 2005: {
                return "CLOB";
            }
            case -13: {
                return "BFILE";
            }
            case 2002: 
            case 2003: 
            case 2007: 
            case 2008: {
                return ((OracleTypeADT)this.types[n4]).getFullName();
            }
            case 2006: {
                return "REF " + ((OracleTypeREF)this.types[n4]).getFullName();
            }
            case -15: {
                return "NCHAR";
            }
            case -9: {
                return "NVARCHAR";
            }
            case 2011: {
                return "NCLOB";
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
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int n2) throws SQLException {
        return false;
    }

    @Override
    public String getColumnClassName(int n2) throws SQLException {
        int n3 = this.getColumnType(n2);
        switch (n3) {
            case -15: 
            case -9: 
            case 1: 
            case 12: {
                return "java.lang.String";
            }
            case -2: {
                return "byte[]";
            }
            case 2: 
            case 3: 
            case 6: 
            case 8: {
                return "java.math.BigDecimal";
            }
            case 91: {
                return "java.sql.Timestamp";
            }
            case -103: {
                return "oracle.sql.INTERVALYM";
            }
            case -104: {
                return "oracle.sql.INTERVALDS";
            }
            case 93: {
                return "oracle.sql.TIMESTAMP";
            }
            case -101: {
                return "oracle.sql.TIMESTAMPTZ";
            }
            case -102: {
                return "oracle.sql.TIMESTAMPLTZ";
            }
            case 2004: {
                return OracleBlob.class.getName();
            }
            case 2005: {
                return OracleClob.class.getName();
            }
            case 2011: {
                return OracleNClob.class.getName();
            }
            case -13: {
                return OracleBfile.class.getName();
            }
            case 2002: 
            case 2008: {
                return OracleStruct.class.getName();
            }
            case 2007: {
                return OracleOpaque.class.getName();
            }
            case 2003: {
                return OracleArray.class.getName();
            }
            case 2006: {
                return OracleRef.class.getName();
            }
        }
        return null;
    }

    @Override
    public String getOracleColumnClassName(int n2) throws SQLException {
        int n3 = this.getColumnType(n2);
        switch (n3) {
            case -15: 
            case -9: 
            case 1: 
            case 12: {
                return "CHAR";
            }
            case -2: {
                return "RAW";
            }
            case 2: 
            case 3: 
            case 6: 
            case 8: {
                return "NUMBER";
            }
            case 91: {
                return "DATE";
            }
            case -103: {
                return "INTERVALYM";
            }
            case -104: {
                return "INTERVALDS";
            }
            case 93: {
                return "TIMESTAMP";
            }
            case -101: {
                return "TIMESTAMPTZ";
            }
            case -102: {
                return "TIMESTAMPLTZ";
            }
            case 2004: {
                return "BLOB";
            }
            case 2005: {
                return "CLOB";
            }
            case 2011: {
                return "NCLOB";
            }
            case -13: {
                return "BFILE";
            }
            case 2002: {
                return "STRUCT";
            }
            case 2008: {
                return "JAVA_STRUCT";
            }
            case 2007: {
                return "OPAQUE";
            }
            case 2003: {
                return "ARRAY";
            }
            case 2006: {
                return "REF";
            }
        }
        return null;
    }

    @Override
    public int getLocalColumnCount() throws SQLException {
        return this.descriptor.getLocalAttributeCount();
    }

    @Override
    public boolean isColumnInvisible(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean isColumnJSON(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean isInherited(int n2) throws SQLException {
        return n2 <= this.getColumnCount() - this.getLocalColumnCount();
    }

    @Override
    public String getAttributeJavaName(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.descriptor.getAttributeJavaName(n3);
    }

    private int getValidColumnIndex(int n2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n3 >= this.types.length) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getValidColumnIndex").fillInStackTrace();
        }
        return n3;
    }

    @Override
    public boolean isNCHAR(int n2) throws SQLException {
        int n3 = this.getValidColumnIndex(n2);
        return this.types[n3].isNCHAR();
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
        return null;
    }
}

