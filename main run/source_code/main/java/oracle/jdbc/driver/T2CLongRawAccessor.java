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
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.LongRawAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
class T2CLongRawAccessor
extends LongRawAccessor {
    T2CLongRawAccessor(OracleStatement oracleStatement, int n2, int n3, short s2, int n4) throws SQLException {
        super(oracleStatement, n2, n3, s2, n4);
    }

    T2CLongRawAccessor(OracleStatement oracleStatement, int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2) throws SQLException {
        super(oracleStatement, n2, n3, bl, n4, n5, n6, l2, n7, s2);
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    byte[] getBytesInternal(int n2) throws SQLException {
        if (this.statement.isFetchStreams) {
            assert (!this.isNull(n2));
            int n3 = this.getLength(n2);
            long l2 = this.getOffset(n2);
            return this.rowData.get(l2, n3);
        }
        return super.getBytesInternal(n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        if (this.statement.isFetchStreams) {
            if (this.isNull(n2)) {
                return null;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rowData.get(this.getOffset(n2), this.getLength(n2)));
            try {
                InputStream inputStream = this.statement.connection.conversion.ConvertStream(byteArrayInputStream, 2);
                return inputStream;
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
        return super.getAsciiStream(n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    InputStream getUnicodeStream(int n2) throws SQLException {
        if (this.statement.isFetchStreams) {
            if (this.isNull(n2)) {
                return null;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rowData.get(this.getOffset(n2), this.getLength(n2)));
            try {
                InputStream inputStream = this.statement.connection.conversion.ConvertStream(byteArrayInputStream, 3);
                return inputStream;
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
        return super.getUnicodeStream(n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        if (this.statement.isFetchStreams) {
            if (this.isNull(n2)) {
                return null;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rowData.get(this.getOffset(n2), this.getLength(n2)));
            try {
                Reader reader = this.statement.connection.conversion.ConvertCharacterStream(byteArrayInputStream, 8, this.formOfUse);
                return reader;
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
        return super.getCharacterStream(n2);
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        if (this.statement.isFetchStreams) {
            if (this.isNull(n2)) {
                return null;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rowData.get(this.getOffset(n2), this.getLength(n2)));
            return byteArrayInputStream;
        }
        return super.getBinaryStream(n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void copyStreamDataIntoDBA(int n2) throws SQLException {
        if (this.stream.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 27).fillInStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] byArray = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] byArray2 = this.statement.connection.getByteBuffer(32768);
            try {
                int n3;
                while ((n3 = this.stream.read(byArray2, 0, 32768)) != -1) {
                    byteArrayOutputStream.write(byArray2, 0, n3);
                }
                this.statement.connection.cacheBuffer(byArray2);
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
            byArray = byteArrayOutputStream.toByteArray();
        }
        finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            }
            catch (IOException iOException) {}
        }
        if (byArray == null || byArray.length == 0) {
            this.setLengthAndNull(n2, 0);
        } else {
            this.setOffset(n2);
            this.setLengthAndNull(n2, byArray.length);
            this.rowData.put(byArray);
        }
    }

    @Override
    Accessor copyForDefine(OracleStatement oracleStatement) {
        LongRawAccessor longRawAccessor = (LongRawAccessor)super.copyForDefine(oracleStatement);
        try {
            longRawAccessor.stream = oracleStatement.connection.driverExtension.createInputStream(oracleStatement, this.columnPosition, longRawAccessor);
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
        return longRawAccessor;
    }
}

