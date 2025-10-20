/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.sql.Wrapper;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.babelfish.Translator;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.proxy.annotation.GetCreator;
import oracle.jdbc.proxy.annotation.GetDelegate;
import oracle.jdbc.proxy.annotation.OnError;
import oracle.jdbc.proxy.annotation.Post;
import oracle.jdbc.proxy.annotation.ProxyFor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
@DefaultLevel(value=Logging.FINEST)
@ProxyFor(value={Array.class, Blob.class, Clob.class, DatabaseMetaData.class, NClob.class, ParameterMetaData.class, Ref.class, ResultSet.class, ResultSetMetaData.class, RowId.class, Savepoint.class, SQLData.class, SQLInput.class, SQLOutput.class, SQLXML.class, Struct.class, Wrapper.class, OracleResultSet.class})
public abstract class BabelfishGenericProxy {
    Translator translator;

    @GetCreator
    protected abstract Object getCreator();

    @GetDelegate
    protected abstract Object getDelegate();

    @OnError(value=SQLException.class)
    protected Object translateError(Method method, SQLException sQLException) throws SQLException {
        throw this.translator.translateError(sQLException);
    }

    @Post
    protected Object post_Methods(Method method, Object object) {
        if (object instanceof BabelfishGenericProxy) {
            ((BabelfishGenericProxy)object).setTranslator(this.translator);
        }
        return object;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }
}

