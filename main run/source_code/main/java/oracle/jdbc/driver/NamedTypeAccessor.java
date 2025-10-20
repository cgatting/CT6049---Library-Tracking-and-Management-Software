/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.xdb.XMLType
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Map;
import oracle.jdbc.OracleData;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.driver.TypeAccessor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleType;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.Datum;
import oracle.sql.JAVA_STRUCT;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.OpaqueDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TypeDescriptor;
import oracle.xdb.XMLType;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NamedTypeAccessor
extends TypeAccessor {
    static final int MAXLENGTH = -1;

    NamedTypeAccessor(OracleStatement oracleStatement, String string, short s2, int n2, boolean bl) throws SQLException {
        super(Representation.NAMED_TYPE, oracleStatement, -1, bl);
        this.init(oracleStatement, 109, 109, s2, bl);
        this.initForDataAccess(n2, 0, string);
    }

    NamedTypeAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2, String string) throws SQLException {
        super(Representation.NAMED_TYPE, oracleStatement, -1, false);
        this.init(oracleStatement, 109, 109, s2, false);
        this.initForDescribe(109, n2, bl, n3, n4, n5, l2, n6, s2, string);
        this.initForDataAccess(0, n2, string);
    }

    NamedTypeAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2, String string, OracleType oracleType) throws SQLException {
        super(Representation.NAMED_TYPE, oracleStatement, -1, false);
        this.init(oracleStatement, 109, 109, s2, false);
        this.describeOtype = oracleType;
        this.initForDescribe(109, n2, bl, n3, n4, n5, l2, n6, s2, string);
        this.internalOtype = oracleType;
        this.initForDataAccess(0, n2, string);
    }

    @Override
    final OracleType otypeFromName(String string) throws SQLException {
        if (!this.outBind) {
            return TypeDescriptor.getTypeDescriptor(string, this.statement.connection).getPickler();
        }
        if (this.externalType == 2003) {
            return ArrayDescriptor.createDescriptor(string, (Connection)this.statement.connection).getOracleTypeCOLLECTION();
        }
        if (this.externalType == 2007 || this.externalType == 2009) {
            return OpaqueDescriptor.createDescriptor(string, (Connection)this.statement.connection).getPickler();
        }
        return StructDescriptor.createDescriptor(string, (Connection)this.statement.connection).getOracleTypeADT();
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        super.initForDataAccess(n2, n3, string);
        this.byteLength = this.statement.connection.namedTypeAccessorByteLen;
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getObject(n2, this.statement.connection.getTypeMap());
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        if (this.externalType == 0) {
            Datum datum = this.getOracleObject(n2);
            if (datum == null) {
                return null;
            }
            if (datum instanceof STRUCT) {
                return ((STRUCT)datum).toJdbc((Map)map);
            }
            if (datum instanceof OPAQUE) {
                Object object = ((OPAQUE)datum).toJdbc(map);
                return object;
            }
            if (datum instanceof ARRAY) {
                return ((ARRAY)datum).toJdbc(map);
            }
            return datum.toJdbc();
        }
        switch (this.externalType) {
            case 2008: {
                map = null;
            }
            case 2000: 
            case 2002: 
            case 2003: 
            case 2007: {
                Datum datum = this.getOracleObject(n2);
                if (datum == null) {
                    return null;
                }
                if (datum instanceof STRUCT) {
                    return ((STRUCT)datum).toJdbc((Map)map);
                }
                if (datum instanceof ARRAY) {
                    return ((ARRAY)datum).toJdbc(map);
                }
                return datum.toJdbc();
            }
            case 2009: {
                Datum datum = this.getOracleObject(n2);
                if (datum == null) {
                    return null;
                }
                try {
                    return (SQLXML)((Object)datum);
                }
                catch (ClassCastException classCastException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.pickledBytes(n2);
        if (byArray == null || byArray.length == 0) {
            return null;
        }
        PhysicalConnection physicalConnection = this.statement.connection;
        OracleTypeADT oracleTypeADT = (OracleTypeADT)this.internalOtype;
        TypeDescriptor typeDescriptor = TypeDescriptor.getTypeDescriptor(this.internalTypeName == null ? oracleTypeADT.getFullName() : this.internalTypeName, physicalConnection, byArray, 0L);
        switch (typeDescriptor.getTypeCode()) {
            case 2003: {
                return new ARRAY((ArrayDescriptor)typeDescriptor, byArray, physicalConnection);
            }
            case 2002: {
                return new STRUCT((StructDescriptor)typeDescriptor, byArray, physicalConnection);
            }
            case 2009: {
                return XMLFactory.createXML(new OPAQUE((OpaqueDescriptor)typeDescriptor, byArray, physicalConnection));
            }
            case 2007: {
                return new OPAQUE((OpaqueDescriptor)typeDescriptor, byArray, physicalConnection);
            }
            case 2008: {
                return new JAVA_STRUCT((StructDescriptor)typeDescriptor, byArray, physicalConnection);
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1).fillInStackTrace();
    }

    @Override
    OracleData getOracleData(int n2) throws SQLException {
        try {
            return (OracleData)this.getObject(n2);
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    ORAData getORAData(int n2) throws SQLException {
        try {
            return (ORAData)this.getObject(n2);
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    ARRAY getARRAY(int n2) throws SQLException {
        try {
            return (ARRAY)this.getOracleObject(n2);
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    STRUCT getSTRUCT(int n2) throws SQLException {
        try {
            return (STRUCT)this.getOracleObject(n2);
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    Struct getStruct(int n2) throws SQLException {
        try {
            return (Struct)((Object)this.getOracleObject(n2));
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    OPAQUE getOPAQUE(int n2) throws SQLException {
        try {
            return (OPAQUE)this.getOracleObject(n2);
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    SQLXML getSQLXML(int n2) throws SQLException {
        try {
            OPAQUE oPAQUE = (OPAQUE)this.getOracleObject(n2);
            if (oPAQUE == null) {
                return null;
            }
            return (SQLXML)((Object)oPAQUE);
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    String getString(int n2) throws SQLException {
        Datum datum = this.getOracleObject(n2);
        if (datum instanceof XMLType) {
            return ((XMLType)datum).getString();
        }
        return null;
    }

    private static class XMLFactory {
        private XMLFactory() {
        }

        static Datum createXML(OPAQUE oPAQUE) throws SQLException {
            return XMLType.createXML((OPAQUE)oPAQUE);
        }
    }
}

