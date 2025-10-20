/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.json.JsonArray
 *  javax.json.JsonNumber
 *  javax.json.JsonObject
 *  javax.json.JsonString
 *  javax.json.JsonStructure
 *  javax.json.JsonValue
 *  javax.json.JsonValue$ValueType
 *  javax.json.stream.JsonParser
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.LobCommonAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.json.OracleJsonArray;
import oracle.sql.json.OracleJsonBinary;
import oracle.sql.json.OracleJsonDate;
import oracle.sql.json.OracleJsonDatum;
import oracle.sql.json.OracleJsonDecimal;
import oracle.sql.json.OracleJsonDouble;
import oracle.sql.json.OracleJsonException;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonFloat;
import oracle.sql.json.OracleJsonGenerator;
import oracle.sql.json.OracleJsonIntervalDS;
import oracle.sql.json.OracleJsonIntervalYM;
import oracle.sql.json.OracleJsonNumber;
import oracle.sql.json.OracleJsonObject;
import oracle.sql.json.OracleJsonParser;
import oracle.sql.json.OracleJsonString;
import oracle.sql.json.OracleJsonStructure;
import oracle.sql.json.OracleJsonTimestamp;
import oracle.sql.json.OracleJsonValue;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class JsonAccessor
extends LobCommonAccessor {
    static final int MAXLENGTH = 4000;
    private final OracleJsonFactory factory;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    JsonAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.JSON, oracleStatement, 4000, bl);
        this.init(oracleStatement, 119, 119, s2, bl);
        this.initForDataAccess(n3, n2, null);
        this.factory = oracleStatement.connection.getOracleJsonFactory();
    }

    JsonAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.JSON, oracleStatement, 4000, false);
        this.init(oracleStatement, 119, 119, s2, false);
        this.initForDescribe(119, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
        this.factory = oracleStatement.connection.getOracleJsonFactory();
    }

    @Override
    byte[] getBytes(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        try {
            OracleJsonParser oracleJsonParser = this.factory.createJsonBinaryParser(this.getBufferInternal(n2));
            this.out.reset();
            OracleJsonGenerator oracleJsonGenerator = this.factory.createJsonTextGenerator(this.out);
            oracleJsonGenerator.writeParser(oracleJsonParser);
            oracleJsonGenerator.close();
            return this.out.toByteArray();
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    @Override
    JsonValue getJsonValue(int n2) throws SQLException {
        byte[] byArray = this.getBytesInternal(n2);
        try {
            return this.factory.createJsonBinaryValue(ByteBuffer.wrap(byArray)).wrap(JsonValue.class);
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    @Override
    JsonParser getJsonParser(int n2) throws SQLException {
        byte[] byArray = this.getBytesInternal(n2);
        try {
            return this.factory.createJsonBinaryParser(ByteBuffer.wrap(byArray)).wrap(JsonParser.class);
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    @Override
    JsonStructure getJsonStructure(int n2) throws SQLException {
        JsonValue jsonValue = this.getJsonValue(n2);
        if (jsonValue.getValueType() == JsonValue.ValueType.ARRAY || jsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
            return (JsonStructure)jsonValue;
        }
        throw this.invalidColumnType();
    }

    @Override
    JsonObject getJsonObject(int n2) throws SQLException {
        return (JsonObject)this.getJsonValue(n2, JsonValue.ValueType.OBJECT);
    }

    @Override
    JsonArray getJsonArray(int n2) throws SQLException {
        return (JsonArray)this.getJsonValue(n2, JsonValue.ValueType.ARRAY);
    }

    @Override
    JsonString getJsonString(int n2) throws SQLException {
        return (JsonString)this.getJsonValue(n2, JsonValue.ValueType.STRING);
    }

    @Override
    JsonNumber getJsonNumber(int n2) throws SQLException {
        return (JsonNumber)this.getJsonValue(n2, JsonValue.ValueType.NUMBER);
    }

    private JsonValue getJsonValue(int n2, JsonValue.ValueType valueType) throws SQLException {
        JsonValue jsonValue = this.getJsonValue(n2);
        if (jsonValue.getValueType() == valueType) {
            return jsonValue;
        }
        throw this.invalidColumnType();
    }

    @Override
    OracleJsonParser getOracleJsonParser(int n2) throws SQLException {
        try {
            return this.factory.createJsonBinaryParser(this.getBufferInternal(n2));
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    @Override
    OracleJsonDatum getOracleJsonDatum(int n2) throws SQLException {
        try {
            return new OracleJsonDatum(this.getBytesInternal(n2));
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    @Override
    Datum getDatum(int n2) throws SQLException {
        return this.getOracleJsonDatum(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getOracleJsonDatum(n2);
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return super.getOracleObject(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return super.getOracleObject(n2);
    }

    @Override
    ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        if (oRADataFactory == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 281).fillInStackTrace();
        }
        Datum datum = this.getOracleObject(n2);
        return oRADataFactory.create(datum, 2016);
    }

    @Override
    OracleJsonValue getOracleJsonValue(int n2) throws SQLException {
        try {
            return this.factory.createJsonBinaryValue(this.getBufferInternal(n2));
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    @Override
    OracleJsonStructure getOracleJsonStructure(int n2) throws SQLException {
        return (OracleJsonStructure)this.getOracleJsonValueAbstract(n2, OracleJsonValue.OracleJsonType.OBJECT, OracleJsonValue.OracleJsonType.ARRAY);
    }

    @Override
    OracleJsonNumber getOracleJsonNumber(int n2) throws SQLException {
        return (OracleJsonNumber)this.getOracleJsonValueAbstract(n2, OracleJsonValue.OracleJsonType.DECIMAL, OracleJsonValue.OracleJsonType.DOUBLE, OracleJsonValue.OracleJsonType.FLOAT);
    }

    @Override
    OracleJsonObject getOracleJsonObject(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.OBJECT).asJsonObject();
    }

    @Override
    OracleJsonArray getOracleJsonArray(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.ARRAY).asJsonArray();
    }

    @Override
    OracleJsonDecimal getOracleJsonDecimal(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.DECIMAL).asJsonDecimal();
    }

    @Override
    OracleJsonDouble getOracleJsonDouble(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.DOUBLE).asJsonDouble();
    }

    @Override
    OracleJsonFloat getOracleJsonFloat(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.FLOAT).asJsonFloat();
    }

    @Override
    OracleJsonString getOracleJsonString(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.STRING).asJsonString();
    }

    @Override
    OracleJsonBinary getOracleJsonBinary(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.BINARY).asJsonBinary();
    }

    @Override
    OracleJsonTimestamp getOracleJsonTimestamp(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.TIMESTAMP).asJsonTimestamp();
    }

    @Override
    OracleJsonDate getOracleJsonDate(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.DATE).asJsonDate();
    }

    @Override
    OracleJsonIntervalDS getOracleJsonIntervalDS(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.INTERVALDS).asJsonIntervalDS();
    }

    @Override
    OracleJsonIntervalYM getOracleJsonIntervalYM(int n2) throws SQLException {
        return this.getOracleJsonValue(n2, OracleJsonValue.OracleJsonType.INTERVALYM).asJsonIntervalYM();
    }

    private OracleJsonValue getOracleJsonValue(int n2, OracleJsonValue.OracleJsonType oracleJsonType) throws SQLException {
        OracleJsonValue oracleJsonValue = this.getOracleJsonValue(n2);
        if (oracleJsonValue.getOracleJsonType() == oracleJsonType) {
            return oracleJsonValue;
        }
        throw this.invalidColumnType();
    }

    private OracleJsonValue getOracleJsonValueAbstract(int n2, OracleJsonValue.OracleJsonType ... oracleJsonTypeArray) throws SQLException {
        OracleJsonValue oracleJsonValue = this.getOracleJsonValue(n2);
        for (OracleJsonValue.OracleJsonType oracleJsonType : oracleJsonTypeArray) {
            if (oracleJsonType != oracleJsonValue.getOracleJsonType()) continue;
            return oracleJsonValue;
        }
        throw this.invalidColumnType();
    }

    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        return string == null ? null : new StringReader(string);
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        return byArray == null ? null : new ByteArrayInputStream(byArray);
    }

    @Override
    String getString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        try {
            OracleJsonParser oracleJsonParser = this.factory.createJsonBinaryParser(this.getBufferInternal(n2));
            StringWriter stringWriter = new StringWriter();
            OracleJsonGenerator oracleJsonGenerator = this.factory.createJsonTextGenerator(stringWriter);
            oracleJsonGenerator.writeParser(oracleJsonParser);
            oracleJsonGenerator.close();
            return stringWriter.toString();
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    private ByteBuffer getBufferInternal(int n2) throws SQLException {
        return ByteBuffer.wrap(this.getBytesInternal(n2));
    }

    private SQLException toSQLException(RuntimeException runtimeException) {
        return (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), runtimeException).fillInStackTrace();
    }

    private SQLException invalidColumnType() {
        return (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
    }

    @Override
    long updateChecksum(long l2, int n2) throws SQLException {
        this.unimpl("updateChecksum");
        return -1L;
    }
}

