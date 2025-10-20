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
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.RawCommonAccessor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class LongRawAccessor
extends RawCommonAccessor {
    static final int MAXLENGTH = Integer.MAX_VALUE;
    OracleInputStream stream;
    int columnPosition = 0;

    LongRawAccessor(OracleStatement oracleStatement, int n2, int n3, short s2, int n4) throws SQLException {
        super(oracleStatement, Integer.MAX_VALUE, false);
        this.init(oracleStatement, 24, 24, s2, false);
        this.columnPosition = n2;
        this.initForDataAccess(n4, n3, null);
    }

    LongRawAccessor(OracleStatement oracleStatement, int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2) throws SQLException {
        super(oracleStatement, Integer.MAX_VALUE, false);
        this.init(oracleStatement, 24, 24, s2, false);
        this.columnPosition = n2;
        this.initForDescribe(24, n3, bl, n4, n5, n6, l2, n7, s2, null);
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
        this.byteLength = 0;
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
        if (this.isNull(n2)) {
            return null;
        }
        if (this.statement.isFetchStreams) {
            return super.getBytesInternal(n2);
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
        byte[] byArray = new byte[1024];
        try {
            int n3;
            while ((n3 = this.stream.read(byArray)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n3);
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
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
            return this.convertBytesToStream(n2, 2);
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        return physicalConnection.conversion.ConvertStream(this.stream, 2);
    }

    @Override
    InputStream getUnicodeStream(int n2) throws SQLException {
        PhysicalConnection physicalConnection = this.statement.connection;
        if (this.isNull(n2)) {
            return null;
        }
        if (this.statement.isFetchStreams) {
            return this.convertBytesToStream(n2, 3);
        }
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        return physicalConnection.conversion.ConvertStream(this.stream, 3);
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
                Reader reader2 = reader = this.statement.connection.conversion.ConvertCharacterStream(byteArrayInputStream, 8, this.formOfUse);
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
        return physicalConnection.conversion.ConvertCharacterStream(this.stream, 8, this.formOfUse);
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
        return "LongRawAccessor@" + Integer.toHexString(this.hashCode()) + "{columnPosition = " + this.columnPosition + "}";
    }
}

