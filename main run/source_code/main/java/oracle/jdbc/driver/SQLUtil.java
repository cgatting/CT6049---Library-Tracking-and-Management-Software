/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.xdb.XMLType
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import oracle.jdbc.OracleData;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleType;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.JavaToJavaConverter;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleLog;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleNamedType;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCOLLECTION;
import oracle.jdbc.oracore.OracleTypeOPAQUE;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CharacterSet;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.JAVA_STRUCT;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.OpaqueDescriptor;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.SQLName;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;
import oracle.sql.converter.CharacterSetMetaData;
import oracle.xdb.XMLType;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class SQLUtil {
    private static final int CLASS_NOT_FOUND = -1;
    private static final int CLASS_STRING = 0;
    private static final int CLASS_BOOLEAN = 1;
    private static final int CLASS_INTEGER = 2;
    private static final int CLASS_LONG = 3;
    private static final int CLASS_FLOAT = 4;
    private static final int CLASS_DOUBLE = 5;
    private static final int CLASS_BIGDECIMAL = 6;
    private static final int CLASS_DATE = 7;
    private static final int CLASS_TIME = 8;
    private static final int CLASS_TIMESTAMP = 9;
    private static final int CLASS_SHORT = 10;
    private static final int CLASS_BYTE = 11;
    private static final int TOTAL_CLASSES = 12;
    private static Hashtable<Class<?>, Integer> classTable = new Hashtable(12);

    public static Object SQLToJava(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, Class<?> clazz, Map<String, Class<?>> map) throws SQLException {
        Datum datum = SQLUtil.makeDatum(oracleConnection, byArray, n2, string, 0);
        Object object = SQLUtil.SQLToJava(oracleConnection, datum, clazz, map);
        return object;
    }

    public static CustomDatum SQLToJava(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, CustomDatumFactory customDatumFactory) throws SQLException {
        Datum datum = SQLUtil.makeDatum(oracleConnection, byArray, n2, string, 0);
        CustomDatum customDatum = customDatumFactory.create(datum, n2);
        return customDatum;
    }

    public static ORAData SQLToJava(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, ORADataFactory oRADataFactory) throws SQLException {
        Datum datum = SQLUtil.makeDatum(oracleConnection, byArray, n2, string, 0);
        ORAData oRAData = oRADataFactory.create(datum, n2);
        return oRAData;
    }

    public static OracleData SQLToJava(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, OracleDataFactory oracleDataFactory) throws SQLException {
        Datum datum = SQLUtil.makeDatum(oracleConnection, byArray, n2, string, 0);
        OracleData oracleData = oracleDataFactory.create(datum.toJdbc(), n2);
        return oracleData;
    }

    public static Object SQLToJava(oracle.jdbc.internal.OracleConnection oracleConnection, Datum datum, Class<?> clazz, Map<String, Class<?>> map) throws SQLException {
        Object object = null;
        if (datum instanceof STRUCT) {
            object = clazz == null ? (map != null ? ((STRUCT)datum).toJdbc((Map)map) : datum.toJdbc()) : (map != null ? ((STRUCT)datum).toClass((Class)clazz, (Map)map) : ((STRUCT)datum).toClass((Class)clazz));
        } else if (clazz == null) {
            object = datum.toJdbc();
        } else {
            int n2 = SQLUtil.classNumber(clazz);
            switch (n2) {
                case 0: {
                    object = datum.stringValue();
                    break;
                }
                case 1: {
                    object = datum.longValue() != 0L;
                    break;
                }
                case 10: {
                    object = (short)datum.longValue();
                    break;
                }
                case 11: {
                    object = (byte)datum.longValue();
                    break;
                }
                case 2: {
                    object = (int)datum.longValue();
                    break;
                }
                case 3: {
                    object = datum.longValue();
                    break;
                }
                case 4: {
                    object = Float.valueOf(datum.bigDecimalValue().floatValue());
                    break;
                }
                case 5: {
                    object = datum.bigDecimalValue().doubleValue();
                    break;
                }
                case 6: {
                    object = datum.bigDecimalValue();
                    break;
                }
                case 7: {
                    object = datum.dateValue();
                    break;
                }
                case 8: {
                    object = datum.timeValue();
                    break;
                }
                case 9: {
                    object = datum.timestampValue();
                    break;
                }
                default: {
                    object = datum.toJdbc();
                    if (clazz.isInstance(object)) break;
                    throw (SQLException)DatabaseError.createSqlException(59, "invalid data conversion").fillInStackTrace();
                }
            }
        }
        return object;
    }

    public static byte[] JavaToSQL(oracle.jdbc.internal.OracleConnection oracleConnection, Object object, int n2, String string) throws SQLException {
        if (object == null) {
            return null;
        }
        Datum datum = null;
        if (object instanceof Datum) {
            datum = (Datum)object;
        } else if (object instanceof ORAData) {
            datum = ((ORAData)object).toDatum(oracleConnection);
        } else if (object instanceof CustomDatum) {
            datum = oracleConnection.toDatum((CustomDatum)object);
        } else if (object instanceof SQLData) {
            datum = STRUCT.toSTRUCT(object, oracleConnection);
        }
        if (datum != null) {
            if (!SQLUtil.checkDatumType(datum, n2, string)) {
                datum = null;
            }
        } else {
            datum = SQLUtil.makeDatum(oracleConnection, object, n2, string);
        }
        byte[] byArray = null;
        if (datum != null) {
            byArray = datum instanceof STRUCT ? ((STRUCT)datum).toBytes() : (datum instanceof ARRAY ? ((ARRAY)datum).toBytes() : (datum instanceof OPAQUE ? ((OPAQUE)datum).toBytes() : datum.shareBytes()));
        } else {
            throw (SQLException)DatabaseError.createSqlException(1, "attempt to convert a Datum to incompatible SQL type").fillInStackTrace();
        }
        return byArray;
    }

    public static Datum makeDatum(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, int n3) throws SQLException {
        return SQLUtil.makeDatum(oracleConnection, byArray, n2, string, n3, (short)0, (short)0);
    }

    public static Datum makeDatum(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, int n3, short s2, short s3) throws SQLException {
        Datum datum = null;
        short s4 = s3 == 0 ? oracleConnection.getDbCsId() : s3;
        short s5 = s2 == 0 ? oracleConnection.getJdbcCsId() : s2;
        int n4 = CharacterSetMetaData.getRatio(s5, s4);
        switch (n2) {
            case 96: {
                if (n3 != 0 && n3 < byArray.length && n4 == 1) {
                    datum = new CHAR(byArray, 0, n3, CharacterSet.make(s5));
                    break;
                }
                datum = new CHAR(byArray, CharacterSet.make(s5));
                break;
            }
            case 1: 
            case 8: {
                datum = new CHAR(byArray, CharacterSet.make(s5));
                break;
            }
            case 2: 
            case 6: {
                datum = new NUMBER(byArray);
                break;
            }
            case 100: {
                datum = new BINARY_FLOAT(byArray);
                break;
            }
            case 101: {
                datum = new BINARY_DOUBLE(byArray);
                break;
            }
            case 23: 
            case 24: {
                datum = new RAW(byArray);
                break;
            }
            case 104: {
                datum = new ROWID(byArray);
                break;
            }
            case 102: {
                throw (SQLException)DatabaseError.createSqlException(1, "need resolution: do we want to handle ResultSet?").fillInStackTrace();
            }
            case 12: {
                datum = new DATE(byArray);
                break;
            }
            case 182: {
                datum = new INTERVALYM(byArray);
                break;
            }
            case 183: {
                datum = new INTERVALDS(byArray);
                break;
            }
            case 180: {
                datum = new TIMESTAMP(byArray);
                break;
            }
            case 181: {
                datum = new TIMESTAMPTZ(byArray);
                break;
            }
            case 231: {
                datum = new TIMESTAMPLTZ(byArray);
                break;
            }
            case 113: {
                datum = oracleConnection.createBlob(byArray);
                break;
            }
            case 112: {
                datum = oracleConnection.createClob(byArray);
                break;
            }
            case 114: {
                datum = oracleConnection.createBfile(byArray);
                break;
            }
            case 257: {
                datum = XMLFactory.createXML(oracleConnection, new ByteArrayInputStream(byArray));
                break;
            }
            case 109: {
                TypeDescriptor typeDescriptor = TypeDescriptor.getTypeDescriptor(string, oracleConnection, byArray, 0L);
                switch (typeDescriptor.getTypeCode()) {
                    case 2002: {
                        datum = new STRUCT((StructDescriptor)typeDescriptor, byArray, oracleConnection);
                        break;
                    }
                    case 2008: {
                        datum = new JAVA_STRUCT((StructDescriptor)typeDescriptor, byArray, oracleConnection);
                        break;
                    }
                    case 2003: {
                        datum = new ARRAY((ArrayDescriptor)typeDescriptor, byArray, oracleConnection);
                        break;
                    }
                    case 2009: {
                        datum = XMLFactory.createXML(new OPAQUE((OpaqueDescriptor)typeDescriptor, byArray, oracleConnection));
                        break;
                    }
                    case 2007: {
                        datum = new OPAQUE((OpaqueDescriptor)typeDescriptor, byArray, oracleConnection);
                    }
                }
                break;
            }
            case 111: {
                Object object = SQLUtil.getTypeDescriptor(string, oracleConnection);
                if (object instanceof StructDescriptor) {
                    datum = new REF((StructDescriptor)object, (Connection)oracleConnection, byArray);
                    break;
                }
                throw (SQLException)DatabaseError.createSqlException(1, "program error: REF points to a non-STRUCT").fillInStackTrace();
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(1, "program error: invalid SQL type code").fillInStackTrace();
            }
        }
        return datum;
    }

    public static Datum makeNDatum(oracle.jdbc.internal.OracleConnection oracleConnection, byte[] byArray, int n2, String string, short s2, int n3) throws SQLException {
        Datum datum = null;
        switch (n2) {
            case 96: {
                int n4 = n3 * CharacterSetMetaData.getRatio(oracleConnection.getNCharSet(), 1);
                if (n3 != 0 && n4 < byArray.length) {
                    datum = new CHAR(byArray, 0, n3, CharacterSet.make(oracleConnection.getNCharSet()));
                    break;
                }
                datum = new CHAR(byArray, CharacterSet.make(oracleConnection.getNCharSet()));
                break;
            }
            case 1: 
            case 8: {
                datum = new CHAR(byArray, CharacterSet.make(oracleConnection.getNCharSet()));
                break;
            }
            case 112: {
                datum = oracleConnection.createClob(byArray, s2);
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(1, "program error: invalid SQL type code").fillInStackTrace();
            }
        }
        return datum;
    }

    public static Datum makeDatum(oracle.jdbc.internal.OracleConnection oracleConnection, Object object, int n2, String string) throws SQLException {
        return SQLUtil.makeDatum(oracleConnection, object, n2, string, false);
    }

    public static Datum makeDatum(oracle.jdbc.internal.OracleConnection oracleConnection, Object object, int n2, String string, boolean bl) throws SQLException {
        Datum datum = null;
        switch (n2) {
            case 1: 
            case 8: 
            case 96: {
                datum = new CHAR(object, CharacterSet.make(bl ? oracleConnection.getNCharSet() : oracleConnection.getJdbcCsId()));
                break;
            }
            case 2: 
            case 6: {
                datum = new NUMBER(object);
                break;
            }
            case 100: {
                if (object instanceof String) {
                    datum = new BINARY_FLOAT((String)object);
                    break;
                }
                if (object instanceof Boolean) {
                    datum = new BINARY_FLOAT((Boolean)object);
                    break;
                }
                datum = new BINARY_FLOAT((Float)object);
                break;
            }
            case 101: {
                if (object instanceof String) {
                    datum = new BINARY_DOUBLE((String)object);
                    break;
                }
                if (object instanceof Boolean) {
                    datum = new BINARY_DOUBLE((Boolean)object);
                    break;
                }
                datum = new BINARY_DOUBLE((Double)object);
                break;
            }
            case 23: 
            case 24: {
                datum = new RAW(object);
                break;
            }
            case 104: {
                if (object instanceof String) {
                    datum = new ROWID((String)object);
                    break;
                }
                if (!(object instanceof byte[])) break;
                datum = new ROWID((byte[])object);
                break;
            }
            case 102: {
                throw (SQLException)DatabaseError.createSqlException(1, "need resolution: do we want to handle ResultSet").fillInStackTrace();
            }
            case 12: {
                datum = JavaToJavaConverter.convert(object, DATE.class, (OracleConnection)oracleConnection, null, null);
                break;
            }
            case 182: {
                datum = JavaToJavaConverter.convert(object, INTERVALYM.class, (OracleConnection)oracleConnection, null, null);
                break;
            }
            case 183: {
                datum = JavaToJavaConverter.convert(object, INTERVALDS.class, (OracleConnection)oracleConnection, null, null);
                break;
            }
            case 180: {
                if (object instanceof TIMESTAMP) {
                    datum = (Datum)object;
                    break;
                }
                if (object instanceof Timestamp) {
                    datum = new TIMESTAMP((Timestamp)object);
                    break;
                }
                if (object instanceof Date) {
                    datum = new TIMESTAMP((Date)object);
                    break;
                }
                if (object instanceof Time) {
                    datum = new TIMESTAMP((Time)object);
                    break;
                }
                if (object instanceof DATE) {
                    datum = new TIMESTAMP((DATE)object);
                    break;
                }
                if (object instanceof String) {
                    datum = new TIMESTAMP((String)object);
                    break;
                }
                if (object instanceof byte[]) {
                    datum = new TIMESTAMP((byte[])object);
                    break;
                }
                datum = JavaToJavaConverter.convert(object, TIMESTAMP.class, (OracleConnection)oracleConnection, null, null);
                break;
            }
            case 181: {
                if (object instanceof TIMESTAMPTZ) {
                    datum = (Datum)object;
                    break;
                }
                if (object instanceof Timestamp) {
                    datum = new TIMESTAMPTZ((Connection)oracleConnection, (Timestamp)object);
                    break;
                }
                if (object instanceof Date) {
                    datum = new TIMESTAMPTZ((Connection)oracleConnection, (Date)object);
                    break;
                }
                if (object instanceof Time) {
                    datum = new TIMESTAMPTZ((Connection)oracleConnection, (Time)object);
                    break;
                }
                if (object instanceof DATE) {
                    datum = new TIMESTAMPTZ((Connection)oracleConnection, (DATE)object);
                    break;
                }
                if (object instanceof String) {
                    datum = new TIMESTAMPTZ((Connection)oracleConnection, (String)object);
                    break;
                }
                if (object instanceof byte[]) {
                    datum = new TIMESTAMPTZ((byte[])object);
                    break;
                }
                datum = JavaToJavaConverter.convert(object, TIMESTAMPTZ.class, (OracleConnection)oracleConnection, null, null);
                break;
            }
            case 231: {
                if (object instanceof TIMESTAMPLTZ) {
                    datum = (Datum)object;
                    break;
                }
                if (object instanceof Timestamp) {
                    datum = new TIMESTAMPLTZ((Connection)oracleConnection, (Timestamp)object);
                    break;
                }
                if (object instanceof Date) {
                    datum = new TIMESTAMPLTZ((Connection)oracleConnection, (Date)object);
                    break;
                }
                if (object instanceof Time) {
                    datum = new TIMESTAMPLTZ((Connection)oracleConnection, (Time)object);
                    break;
                }
                if (object instanceof DATE) {
                    datum = new TIMESTAMPLTZ((Connection)oracleConnection, (DATE)object);
                    break;
                }
                if (object instanceof String) {
                    datum = new TIMESTAMPLTZ((Connection)oracleConnection, (String)object);
                    break;
                }
                if (object instanceof byte[]) {
                    datum = new TIMESTAMPLTZ((byte[])object);
                    break;
                }
                datum = JavaToJavaConverter.convert(object, TIMESTAMPLTZ.class, (OracleConnection)oracleConnection, null, null);
                break;
            }
            case 113: {
                if (object instanceof BLOB) {
                    datum = (Datum)object;
                }
                if (!(object instanceof byte[])) break;
                datum = new RAW((byte[])object);
                break;
            }
            case 112: {
                if (object instanceof CLOB) {
                    datum = (Datum)object;
                }
                if (!(object instanceof String)) break;
                CharacterSet characterSet = CharacterSet.make(bl ? oracleConnection.getNCharSet() : oracleConnection.getJdbcCsId());
                datum = new CHAR((String)object, characterSet);
                break;
            }
            case 114: {
                if (!(object instanceof BFILE)) break;
                datum = (Datum)object;
                break;
            }
            case 109: {
                if (!(object instanceof STRUCT) && !(object instanceof ARRAY) && !(object instanceof OPAQUE)) break;
                datum = (Datum)object;
                break;
            }
            case 257: {
                if (!(object instanceof String)) break;
                datum = XMLFactory.createXML(oracleConnection, (String)object);
                break;
            }
            case 111: {
                if (!(object instanceof REF)) break;
                datum = (Datum)object;
                break;
            }
        }
        if (datum == null) {
            throw (SQLException)DatabaseError.createSqlException(1, "Unable to construct a Datum from the specified input").fillInStackTrace();
        }
        return datum;
    }

    private static int classNumber(Class<?> clazz) {
        int n2 = -1;
        Integer n3 = classTable.get(clazz);
        if (n3 != null) {
            n2 = n3;
        }
        return n2;
    }

    public static Object getTypeDescriptor(String string, oracle.jdbc.internal.OracleConnection oracleConnection) throws SQLException {
        Object object = null;
        SQLName sQLName = new SQLName(string, oracleConnection);
        String string2 = sQLName.getName();
        object = oracleConnection.getDescriptor(string2);
        if (object != null) {
            return object;
        }
        OracleTypeADT oracleTypeADT = new OracleTypeADT(string2, (Connection)oracleConnection);
        oracleTypeADT.init(oracleConnection);
        OracleNamedType oracleNamedType = oracleTypeADT.cleanup();
        switch (oracleNamedType.getTypeCode()) {
            case 2003: {
                object = new ArrayDescriptor(sQLName, (OracleTypeCOLLECTION)oracleNamedType, (Connection)oracleConnection);
                break;
            }
            case 2002: 
            case 2008: {
                object = new StructDescriptor(sQLName, (OracleTypeADT)oracleNamedType, (Connection)oracleConnection);
                break;
            }
            case 2007: {
                object = new OpaqueDescriptor(sQLName, (OracleTypeOPAQUE)oracleNamedType, (Connection)oracleConnection);
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(1, "Unrecognized type code").fillInStackTrace();
            }
        }
        oracleConnection.putDescriptor(string2, object);
        return object;
    }

    public static boolean checkDatumType(Datum datum, int n2, String string) throws SQLException {
        boolean bl = false;
        switch (n2) {
            case 1: 
            case 8: 
            case 96: {
                bl = datum instanceof CHAR;
                break;
            }
            case 2: 
            case 6: {
                bl = datum instanceof NUMBER;
                break;
            }
            case 100: {
                bl = datum instanceof BINARY_FLOAT;
                break;
            }
            case 101: {
                bl = datum instanceof BINARY_DOUBLE;
                break;
            }
            case 23: 
            case 24: {
                bl = datum instanceof RAW;
                break;
            }
            case 104: {
                bl = datum instanceof ROWID;
                break;
            }
            case 12: {
                bl = datum instanceof DATE;
                break;
            }
            case 180: {
                bl = datum instanceof TIMESTAMP;
                break;
            }
            case 181: {
                bl = datum instanceof TIMESTAMPTZ;
                break;
            }
            case 231: {
                bl = datum instanceof TIMESTAMPLTZ;
                break;
            }
            case 113: {
                bl = datum instanceof BLOB;
                break;
            }
            case 112: {
                bl = datum instanceof CLOB;
                break;
            }
            case 114: {
                bl = datum instanceof BFILE;
                break;
            }
            case 111: {
                bl = datum instanceof REF && ((REF)datum).getBaseTypeName().equals(string);
                break;
            }
            case 109: {
                if (datum instanceof STRUCT) {
                    bl = ((STRUCT)datum).isInHierarchyOf(string);
                    break;
                }
                if (datum instanceof ARRAY) {
                    bl = ((ARRAY)datum).getSQLTypeName().equals(string);
                    break;
                }
                if (!(datum instanceof OPAQUE)) break;
                bl = ((OPAQUE)datum).getSQLTypeName().equals(string);
                break;
            }
            default: {
                bl = false;
            }
        }
        return bl;
    }

    public static boolean implementsInterface(Class<?> clazz, Class<?> clazz2) {
        if (clazz == null) {
            return false;
        }
        if (clazz == clazz2) {
            return true;
        }
        Class<?>[] classArray = clazz.getInterfaces();
        for (int i2 = 0; i2 < classArray.length; ++i2) {
            if (!SQLUtil.implementsInterface(classArray[i2], clazz2)) continue;
            return true;
        }
        return SQLUtil.implementsInterface(clazz.getSuperclass(), clazz2);
    }

    public static Datum makeOracleDatum(oracle.jdbc.internal.OracleConnection oracleConnection, Object object, int n2, String string) throws SQLException {
        return SQLUtil.makeOracleDatum(oracleConnection, object, n2, string, false);
    }

    public static Datum makeOracleDatum(oracle.jdbc.internal.OracleConnection oracleConnection, Object object, int n2, String string, boolean bl) throws SQLException {
        Datum datum = SQLUtil.makeDatum(oracleConnection, object, SQLUtil.getInternalType(n2), string, bl);
        return datum;
    }

    public static int getInternalType(int n2) throws SQLException {
        int n3 = 0;
        switch (n2) {
            case -7: 
            case -6: 
            case -5: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                n3 = 6;
                break;
            }
            case 2: {
                n3 = 2;
                break;
            }
            case 100: {
                n3 = 100;
                break;
            }
            case 101: {
                n3 = 101;
                break;
            }
            case 999: {
                n3 = 999;
                break;
            }
            case 1: {
                n3 = 96;
                break;
            }
            case -15: {
                n3 = 96;
                break;
            }
            case 12: {
                n3 = 1;
                break;
            }
            case -9: {
                n3 = 1;
                break;
            }
            case -1: {
                n3 = 8;
                break;
            }
            case 91: 
            case 92: {
                n3 = 12;
                break;
            }
            case -100: 
            case 93: {
                n3 = 180;
                break;
            }
            case -101: {
                n3 = 181;
                break;
            }
            case -102: {
                n3 = 231;
                break;
            }
            case -104: {
                n3 = 183;
                break;
            }
            case -103: {
                n3 = 182;
                break;
            }
            case -3: 
            case -2: {
                n3 = 23;
                break;
            }
            case -4: {
                n3 = 24;
                break;
            }
            case -8: {
                n3 = 104;
                break;
            }
            case 2004: {
                n3 = 113;
                break;
            }
            case 2016: {
                n3 = 119;
                break;
            }
            case 2005: {
                n3 = 112;
                break;
            }
            case 2011: {
                n3 = 112;
                break;
            }
            case -13: {
                n3 = 114;
                break;
            }
            case -10: 
            case 2012: {
                n3 = 102;
                break;
            }
            case 2009: {
                n3 = 257;
                break;
            }
            case 2002: 
            case 2003: 
            case 2007: 
            case 2008: {
                n3 = 109;
                break;
            }
            case 2006: {
                n3 = 111;
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(4, "get_internal_type").fillInStackTrace();
            }
        }
        return n3;
    }

    public static SQLType getExternalType(int n2) {
        OracleType oracleType = OracleType.ANYTYPE;
        switch (n2) {
            case 1: {
                oracleType = OracleType.VARCHAR2;
                break;
            }
            case 96: {
                oracleType = OracleType.CHAR;
                break;
            }
            case 2: {
                oracleType = OracleType.NUMBER;
                break;
            }
            case 12: {
                oracleType = OracleType.DATE;
                break;
            }
            case 23: {
                oracleType = OracleType.RAW;
                break;
            }
            case 180: {
                oracleType = OracleType.TIMESTAMP;
                break;
            }
            case 231: {
                oracleType = OracleType.TIMESTAMP_WITH_LOCAL_TIME_ZONE;
                break;
            }
            default: {
                oracleType = OracleType.ANYTYPE;
            }
        }
        return oracleType;
    }

    protected oracle.jdbc.internal.OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    static {
        try {
            classTable.put(Class.forName("java.lang.String"), 0);
            classTable.put(Class.forName("java.lang.Boolean"), 1);
            classTable.put(Class.forName("java.lang.Integer"), 2);
            classTable.put(Class.forName("java.lang.Long"), 3);
            classTable.put(Class.forName("java.lang.Float"), 4);
            classTable.put(Class.forName("java.lang.Double"), 5);
            classTable.put(Class.forName("java.math.BigDecimal"), 6);
            classTable.put(Class.forName("java.sql.Date"), 7);
            classTable.put(Class.forName("java.sql.Time"), 8);
            classTable.put(Class.forName("java.sql.Timestamp"), 9);
            classTable.put(Class.forName("java.lang.Short"), 10);
            classTable.put(Class.forName("java.lang.Byte"), 11);
        }
        catch (ClassNotFoundException classNotFoundException) {
            OracleLog.log(null, Level.SEVERE, SQLUtil.class, null, classNotFoundException.getMessage() + "\n", classNotFoundException);
        }
    }

    private static class XMLFactory {
        private XMLFactory() {
        }

        static Datum createXML(OPAQUE oPAQUE) throws SQLException {
            return XMLType.createXML((OPAQUE)oPAQUE);
        }

        static Datum createXML(oracle.jdbc.internal.OracleConnection oracleConnection, String string) throws SQLException {
            return XMLType.createXML((Connection)oracleConnection, (String)string);
        }

        static Datum createXML(oracle.jdbc.internal.OracleConnection oracleConnection, InputStream inputStream) throws SQLException {
            return XMLType.createXML((Connection)oracleConnection, (InputStream)inputStream);
        }
    }
}

