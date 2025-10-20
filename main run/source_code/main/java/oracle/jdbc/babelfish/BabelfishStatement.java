/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.babelfish.BabelfishGenericProxy;
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
@ProxyFor(value={OracleStatement.class, Statement.class})
public abstract class BabelfishStatement
extends BabelfishGenericProxy {
    @Override
    @GetCreator
    protected abstract Object getCreator();

    @Override
    @GetDelegate
    protected abstract Object getDelegate();

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

    public void addBatch(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            ((Statement)this.getDelegate()).addBatch(string2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public boolean execute(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).execute(string2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public boolean execute(String string, int n2) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).execute(string2, n2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public boolean execute(String string, int[] nArray) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).execute(string2, nArray);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public boolean execute(String string, String[] stringArray) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).execute(string2, stringArray);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public ResultSet executeQuery(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            ResultSet resultSet = (ResultSet)this.proxify(((Statement)this.getDelegate()).executeQuery(string2), this);
            ((BabelfishGenericProxy)((Object)resultSet)).setTranslator(this.translator);
            return resultSet;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public int executeUpdate(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).executeUpdate(string2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public int executeUpdate(String string, int n2) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).executeUpdate(string2, n2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public int executeUpdate(String string, int[] nArray) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).executeUpdate(string2, nArray);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public int executeUpdate(String string, String[] stringArray) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Statement)this.getDelegate()).executeUpdate(string2, stringArray);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }
}

