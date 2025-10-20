/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import oracle.jdbc.driver.CharCommonAccessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class LongAccessor
extends CharCommonAccessor {
    static final int MAXLENGTH = Integer.MAX_VALUE;
    OracleInputStream stream;
    int columnPosition = 0;

    LongAccessor(OracleStatement oracleStatement, int n2, int n3, short s2, int n4) throws SQLException {
        super(oracleStatement, n3 > 0 && n3 < Integer.MAX_VALUE ? n3 : Integer.MAX_VALUE, s2, false);
        this.init(oracleStatement, 8, 8, s2, false);
        this.columnPosition = n2;
        this.initForDataAccess(n4, n3, null);
    }

    LongAccessor(OracleStatement oracleStatement, int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2) throws SQLException {
        super(oracleStatement, n3 > 0 && n3 < Integer.MAX_VALUE ? n3 : Integer.MAX_VALUE, s2, false);
        this.init(oracleStatement, 8, 8, s2, false);
        this.columnPosition = n2;
        this.initForDescribe(8, n3, bl, n4, n5, n6, l2, n7, s2, null);
        int n8 = oracleStatement.maxFieldSize;
        if (n8 > 0 && (n3 == 0 || n8 < n3)) {
            n3 = n8;
        }
        this.initForDataAccess(0, n3, null);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        if (n2 != 0) {
            this.externalType = n2;
        }
        this.isStream = true;
        this.isColumnNumberAware = true;
        this.charLength = 0;
        this.stream = this.statement.connection.driverExtension.createInputStream(this.statement, this.columnPosition, this);
    }

    @Override
    OracleInputStream initForNewRow() throws SQLException {
        this.stream = this.statement.connection.driverExtension.createInputStream(this.statement, this.columnPosition, this);
        return this.stream;
    }

    @Override
    void updateColumnNumber(int n2) {
        this.columnPosition = ++n2;
        if (this.stream != null) {
            this.stream.columnIndex = n2;
        }
    }

    @Override
    byte[] getBytesInternal(int n2) throws SQLException {
        if (this.statement.isFetchStreams) {
            return super.getBytesInternal(n2);
        }
        if (this.isNull(n2)) {
            return null;
        }
        if (this.stream == null) {
            return null;
        }
        if (!this.isStream) {
            return super.getBytesInternal(n2);
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] byArray = this.statement.connection.getByteBuffer(32768);
        try {
            int n3;
            while ((n3 = this.stream.read(byArray, 0, 32768)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n3);
            }
            this.statement.connection.cacheBuffer(byArray);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    String getString(int n2) throws SQLException {
        String string = null;
        byte[] byArray = this.getBytes(n2);
        if (byArray != null) {
            int n3 = this.statement.maxFieldSize > 0 && this.statement.maxFieldSize < this.representationMaxLength ? this.statement.maxFieldSize : this.representationMaxLength;
            int n4 = Math.min(byArray.length, n3);
            assert (n4 > 0) : "len: " + n4;
            string = this.formOfUse == 2 ? this.statement.connection.conversion.NCharBytesToString(byArray, n4) : this.statement.connection.conversion.CharBytesToString(byArray, n4);
        }
        return string;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected InputStream convertBytesToStream(int n2, int n3) throws SQLException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.getBytesInternal(n2));
        try {
            InputStream inputStream;
            InputStream inputStream2 = inputStream = this.statement.connection.conversion.ConvertStream(byteArrayInputStream, n3);
            return inputStream2;
        }
        finally {
            try {
                if (byteArrayInputStream != null) {
                    ((InputStream)byteArrayInputStream).close();
                }
            }
            catch (IOException iOException) {}
        }
    }

    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        PhysicalConnection physicalConnection = this.statement.connection;
        if (this.isNull(n2)) {
            return null;
        }
        if (this.statement.isFetchStreams) {
            return this.convertBytesToStream(n2, 0);
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        return physicalConnection.conversion.ConvertStream(this.stream, 0);
    }

    @Override
    InputStream getUnicodeStream(int n2) throws SQLException {
        PhysicalConnection physicalConnection = this.statement.connection;
        if (this.isNull(n2)) {
            return null;
        }
        if (this.statement.isFetchStreams) {
            return this.convertBytesToStream(n2, 1);
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        return physicalConnection.conversion.ConvertStream(this.stream, 1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        if (this.statement.isFetchStreams) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.getBytesInternal(n2));
            try {
                Reader reader;
                PhysicalConnection physicalConnection = this.statement.connection;
                Reader reader2 = reader = this.statement.connection.conversion.ConvertCharacterStream(byteArrayInputStream, 9, this.formOfUse);
                return reader2;
            }
            finally {
                try {
                    if (byteArrayInputStream != null) {
                        ((InputStream)byteArrayInputStream).close();
                    }
                }
                catch (IOException iOException) {}
            }
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        PhysicalConnection physicalConnection = this.statement.connection;
        return physicalConnection.conversion.ConvertCharacterStream(this.stream, 9, this.formOfUse);
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        PhysicalConnection physicalConnection = this.statement.connection;
        if (this.isNull(n2)) {
            return null;
        }
        if (this.statement.isFetchStreams) {
            return this.convertBytesToStream(n2, 6);
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        return physicalConnection.conversion.ConvertStream(this.stream, 6);
    }

    @Override
    @DisableTrace
    public String toString() {
        return "LongAccessor@" + Integer.toHexString(this.hashCode()) + "{columnPosition = " + this.columnPosition + "}";
    }
}

