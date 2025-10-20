/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTranslatingConnection;
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
import oracle.jdbc.proxy.annotation.ProxyLocale;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
@DefaultLevel(value=Logging.FINEST)
@ProxyFor(value={Connection.class, OracleConnection.class, oracle.jdbc.internal.OracleConnection.class})
@ProxyLocale
public abstract class BabelfishConnection
extends BabelfishGenericProxy
implements OracleTranslatingConnection {
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

    public PreparedStatement prepareStatement(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            PreparedStatement preparedStatement = (PreparedStatement)this.proxify(((Connection)this.getDelegate()).prepareStatement(string2), this);
            ((BabelfishGenericProxy)((Object)preparedStatement)).setTranslator(this.translator);
            return preparedStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public PreparedStatement prepareStatement(String string, int n2) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            PreparedStatement preparedStatement = (PreparedStatement)this.proxify(((Connection)this.getDelegate()).prepareStatement(string2, n2), this);
            ((BabelfishGenericProxy)((Object)preparedStatement)).setTranslator(this.translator);
            return preparedStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public PreparedStatement prepareStatement(String string, int[] nArray) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            PreparedStatement preparedStatement = (PreparedStatement)this.proxify(((Connection)this.getDelegate()).prepareStatement(string2, nArray), this);
            ((BabelfishGenericProxy)((Object)preparedStatement)).setTranslator(this.translator);
            return preparedStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public PreparedStatement prepareStatement(String string, String[] stringArray) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            PreparedStatement preparedStatement = (PreparedStatement)this.proxify(((Connection)this.getDelegate()).prepareStatement(string2, stringArray), this);
            ((BabelfishGenericProxy)((Object)preparedStatement)).setTranslator(this.translator);
            return preparedStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public PreparedStatement prepareStatement(String string, int n2, int n3) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            PreparedStatement preparedStatement = (PreparedStatement)this.proxify(((Connection)this.getDelegate()).prepareStatement(string2, n2, n3), this);
            ((BabelfishGenericProxy)((Object)preparedStatement)).setTranslator(this.translator);
            return preparedStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            PreparedStatement preparedStatement = (PreparedStatement)this.proxify(((Connection)this.getDelegate()).prepareStatement(string2, n2, n3, n4), this);
            ((BabelfishGenericProxy)((Object)preparedStatement)).setTranslator(this.translator);
            return preparedStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public CallableStatement prepareCall(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            CallableStatement callableStatement = (CallableStatement)this.proxify(((Connection)this.getDelegate()).prepareCall(string2), this);
            ((BabelfishGenericProxy)((Object)callableStatement)).setTranslator(this.translator);
            return callableStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public CallableStatement prepareCall(String string, int n2, int n3) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            CallableStatement callableStatement = (CallableStatement)this.proxify(((Connection)this.getDelegate()).prepareCall(string2, n2, n3), this);
            ((BabelfishGenericProxy)((Object)callableStatement)).setTranslator(this.translator);
            return callableStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public CallableStatement prepareCall(String string, int n2, int n3, int n4) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            CallableStatement callableStatement = (CallableStatement)this.proxify(((Connection)this.getDelegate()).prepareCall(string2, n2, n3, n4), this);
            ((BabelfishGenericProxy)((Object)callableStatement)).setTranslator(this.translator);
            return callableStatement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public String nativeSQL(String string) throws SQLException {
        try {
            String string2 = this.translator.translateQuery(string);
            return ((Connection)this.getDelegate()).nativeSQL(string2);
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    public void close() throws SQLException {
        this.translator.deactivateServerTranslation();
        ((Connection)this.getDelegate()).close();
    }

    public Statement createStatement() throws SQLException {
        try {
            Statement statement = (Statement)this.proxify(((Connection)this.getDelegate()).createStatement(), this);
            ((BabelfishGenericProxy)((Object)statement)).setTranslator(this.translator);
            return statement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    @Override
    public Statement createStatement(boolean bl) throws SQLException {
        Statement statement = !bl ? ((Connection)this.getDelegate()).createStatement() : this.createStatement();
        return statement;
    }

    public Statement createStatement(int n2, int n3) throws SQLException {
        try {
            Statement statement = (Statement)this.proxify(((Connection)this.getDelegate()).createStatement(n2, n3), this);
            ((BabelfishGenericProxy)((Object)statement)).setTranslator(this.translator);
            return statement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    @Override
    public Statement createStatement(int n2, int n3, boolean bl) throws SQLException {
        Statement statement = !bl ? ((Connection)this.getDelegate()).createStatement(n2, n3) : this.createStatement(n2, n3);
        return statement;
    }

    public Statement createStatement(int n2, int n3, int n4) throws SQLException {
        try {
            Statement statement = (Statement)this.proxify(((Connection)this.getDelegate()).createStatement(n2, n3, n4), this);
            ((BabelfishGenericProxy)((Object)statement)).setTranslator(this.translator);
            return statement;
        }
        catch (SQLException sQLException) {
            throw this.translator.translateError(sQLException);
        }
    }

    @Override
    public Statement createStatement(int n2, int n3, int n4, boolean bl) throws SQLException {
        Statement statement = !bl ? ((Connection)this.getDelegate()).createStatement(n2, n3, n4) : this.createStatement(n2, n3, n4);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String string, boolean bl) throws SQLException {
        PreparedStatement preparedStatement = !bl ? ((Connection)this.getDelegate()).prepareStatement(string) : this.prepareStatement(string);
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, boolean bl) throws SQLException {
        PreparedStatement preparedStatement = !bl ? ((Connection)this.getDelegate()).prepareStatement(string, n2) : this.prepareStatement(string, n2);
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] nArray, boolean bl) throws SQLException {
        PreparedStatement preparedStatement = !bl ? ((Connection)this.getDelegate()).prepareStatement(string, nArray) : this.prepareStatement(string, nArray);
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] stringArray, boolean bl) throws SQLException {
        PreparedStatement preparedStatement = !bl ? ((Connection)this.getDelegate()).prepareStatement(string, stringArray) : this.prepareStatement(string, stringArray);
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3, boolean bl) throws SQLException {
        PreparedStatement preparedStatement = !bl ? ((Connection)this.getDelegate()).prepareStatement(string, n2, n3) : this.prepareStatement(string, n2, n3);
        return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4, boolean bl) throws SQLException {
        PreparedStatement preparedStatement = !bl ? ((Connection)this.getDelegate()).prepareStatement(string, n2, n3, n4) : this.prepareStatement(string, n2, n3, n4);
        return preparedStatement;
    }

    @Override
    public CallableStatement prepareCall(String string, boolean bl) throws SQLException {
        CallableStatement callableStatement = !bl ? ((Connection)this.getDelegate()).prepareCall(string) : this.prepareCall(string);
        return callableStatement;
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3, boolean bl) throws SQLException {
        CallableStatement callableStatement = !bl ? ((Connection)this.getDelegate()).prepareCall(string, n2, n3) : this.prepareCall(string, n2, n3);
        return callableStatement;
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3, int n4, boolean bl) throws SQLException {
        CallableStatement callableStatement = !bl ? ((Connection)this.getDelegate()).prepareCall(string, n2, n3, n4) : this.prepareCall(string, n2, n3, n4);
        return callableStatement;
    }

    @Override
    public Map<OracleTranslatingConnection.SqlTranslationVersion, String> getSqlTranslationVersions(String string, boolean bl) throws SQLException {
        EnumMap<OracleTranslatingConnection.SqlTranslationVersion, String> enumMap = new EnumMap<OracleTranslatingConnection.SqlTranslationVersion, String>(OracleTranslatingConnection.SqlTranslationVersion.class);
        enumMap.put(OracleTranslatingConnection.SqlTranslationVersion.ORIGINAL_SQL, string);
        String string2 = this.translator.convertParameterMarkersToOracleStyle(string);
        enumMap.put(OracleTranslatingConnection.SqlTranslationVersion.JDBC_MARKER_CONVERTED, string2);
        try {
            string2 = this.translator.translateQuery(string);
            enumMap.put(OracleTranslatingConnection.SqlTranslationVersion.TRANSLATED, string2);
        }
        catch (SQLException sQLException) {
            if (bl) {
                enumMap.put(OracleTranslatingConnection.SqlTranslationVersion.TRANSLATED, null);
            }
            throw sQLException;
        }
        return enumMap;
    }
}

