/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.babelfish.BabelfishGenericProxy;
import oracle.jdbc.babelfish.BabelfishStatement;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.proxy.annotation.GetCreator;
import oracle.jdbc.proxy.annotation.GetDelegate;
import oracle.jdbc.proxy.annotation.GetProxy;
import oracle.jdbc.proxy.annotation.OnError;
import oracle.jdbc.proxy.annotation.Post;
import oracle.jdbc.proxy.annotation.ProxyFor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
@DefaultLevel(value=Logging.FINEST)
@ProxyFor(value={OraclePreparedStatement.class, PreparedStatement.class})
public abstract class BabelfishPreparedStatement
extends BabelfishStatement {
    @Override
    @GetCreator
    protected abstract Object getCreator();

    @Override
    @GetDelegate
    protected abstract Object getDelegate();

    @Override
    @GetProxy
    protected abstract Object proxify(Object var1, Object var2);

    @Override
    @OnError(value=SQLException.class)
    protected Object translateError(Method method, SQLException sQLException) throws SQLException {
        throw this.translator.translateError(sQLException);
    }

    @Override
    @Post
    protected Object post_Methods(Method method, Object object) {
        if (object instanceof BabelfishGenericProxy) {
            ((BabelfishGenericProxy)object).setTranslator(this.translator);
        }
        return object;
    }

    public void setArray(int n2, Array array) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setArrayAtName(String.valueOf(n2), array);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setAsciiStream(int n2, InputStream inputStream) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setAsciiStreamAtName(String.valueOf(n2), inputStream);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setAsciiStreamAtName(String.valueOf(n2), inputStream, n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setAsciiStreamAtName(String.valueOf(n2), inputStream, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBigDecimalAtName(String.valueOf(n2), bigDecimal);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBinaryStream(int n2, InputStream inputStream) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBinaryStreamAtName(String.valueOf(n2), inputStream);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBinaryStreamAtName(String.valueOf(n2), inputStream, n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBinaryStreamAtName(String.valueOf(n2), inputStream, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBlob(int n2, Blob blob) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBlobAtName(String.valueOf(n2), blob);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBlob(int n2, InputStream inputStream) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBlobAtName(String.valueOf(n2), inputStream);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBlobAtName(String.valueOf(n2), inputStream, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBoolean(int n2, boolean bl) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBooleanAtName(String.valueOf(n2), bl);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setByte(int n2, byte by) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setByteAtName(String.valueOf(n2), by);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setBytes(int n2, byte[] byArray) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setBytesAtName(String.valueOf(n2), byArray);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setCharacterStream(int n2, Reader reader) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setCharacterStreamAtName(String.valueOf(n2), reader);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setCharacterStreamAtName(String.valueOf(n2), reader, n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setCharacterStreamAtName(String.valueOf(n2), reader, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setClob(int n2, Clob clob) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setClobAtName(String.valueOf(n2), clob);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setClob(int n2, Reader reader) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setClobAtName(String.valueOf(n2), reader);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setClob(int n2, Reader reader, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setClobAtName(String.valueOf(n2), reader, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setDate(int n2, Date date) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setDateAtName(String.valueOf(n2), date);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setDate(int n2, Date date, Calendar calendar) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setDateAtName(String.valueOf(n2), date, calendar);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setDouble(int n2, double d2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setDoubleAtName(String.valueOf(n2), d2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setFloat(int n2, float f2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setFloatAtName(String.valueOf(n2), f2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setInt(int n2, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setIntAtName(String.valueOf(n2), n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setLong(int n2, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setLongAtName(String.valueOf(n2), l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNCharacterStream(int n2, Reader reader) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNCharacterStreamAtName(String.valueOf(n2), reader);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNCharacterStreamAtName(String.valueOf(n2), reader, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNClob(int n2, NClob nClob) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNClobAtName(String.valueOf(n2), nClob);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNClob(int n2, Reader reader) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNClobAtName(String.valueOf(n2), reader);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNClob(int n2, Reader reader, long l2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNClobAtName(String.valueOf(n2), reader, l2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNString(int n2, String string) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNStringAtName(String.valueOf(n2), string);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNull(int n2, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNullAtName(String.valueOf(n2), n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setNull(int n2, int n3, String string) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setNullAtName(String.valueOf(n2), n3, string);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setObject(int n2, Object object) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setObjectAtName(String.valueOf(n2), object);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setObject(int n2, Object object, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setObjectAtName(String.valueOf(n2), object, n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setObject(int n2, Object object, int n3, int n4) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setObjectAtName(String.valueOf(n2), object, n3, n4);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setRef(int n2, Ref ref) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setRefAtName(String.valueOf(n2), ref);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setRowId(int n2, RowId rowId) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setRowIdAtName(String.valueOf(n2), rowId);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setShort(int n2, short s2) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setShortAtName(String.valueOf(n2), s2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setSQLXMLAtName(String.valueOf(n2), sQLXML);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setString(int n2, String string) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setStringAtName(String.valueOf(n2), string);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setTime(int n2, Time time) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setTimeAtName(String.valueOf(n2), time);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setTime(int n2, Time time, Calendar calendar) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setTimeAtName(String.valueOf(n2), time, calendar);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setTimestamp(int n2, Timestamp timestamp) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setTimestampAtName(String.valueOf(n2), timestamp);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setTimestampAtName(String.valueOf(n2), timestamp, calendar);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setUnicodeStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setUnicodeStreamAtName(String.valueOf(n2), inputStream, n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void setURL(int n2, URL uRL) throws SQLException {
        try {
            ((OraclePreparedStatement)this.getDelegate()).setURLAtName(String.valueOf(n2), uRL);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }
}

