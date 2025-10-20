/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.SQLException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.babelfish.BabelfishGenericProxy;
import oracle.jdbc.babelfish.BabelfishPreparedStatement;
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
@ProxyFor(value={OracleCallableStatement.class, CallableStatement.class})
public abstract class BabelfishCallableStatement
extends BabelfishPreparedStatement {
    @Override
    @GetCreator
    protected abstract Object getCreator();

    @Override
    @GetDelegate
    protected abstract Object getDelegate();

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

    public void registerOutParameter(int n2, int n3) throws SQLException {
        try {
            ((OracleCallableStatement)this.getDelegate()).registerOutParameterAtName(String.valueOf(n2), n3);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void registerOutParameter(int n2, int n3, int n4) throws SQLException {
        try {
            ((OracleCallableStatement)this.getDelegate()).registerOutParameterAtName(String.valueOf(n2), n3, n4);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void registerOutParameter(int n2, int n3, String string) throws SQLException {
        try {
            ((OracleCallableStatement)this.getDelegate()).registerOutParameterAtName(String.valueOf(n2), n3, string);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }
}

