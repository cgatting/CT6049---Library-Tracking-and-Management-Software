/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleParameterMetaDataParser;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleParameterMetaData
implements oracle.jdbc.internal.OracleParameterMetaData {
    static final Set<Integer> BAD_SQL = Collections.synchronizedSet(new HashSet());
    static final int BAD_SQL_LIMIT = 10000;
    int parameterCount = 0;
    int[] isNullable;
    boolean[] isSigned;
    int[] precision;
    int[] scale;
    int[] parameterType;
    String[] parameterTypeName;
    String[] parameterClassName;
    int[] parameterMode;
    boolean throwUnsupportedFeature = false;
    int parameterNoNulls = 0;
    int parameterNullable = 1;
    int parameterNullableUnknown = 2;
    int parameterModeUnknown = 0;
    int parameterModeIn = 1;
    int parameterModeInOut = 2;
    int parameterModeOut = 4;
    Object acProxy;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static final ParameterMetaData getParameterMetaData(OracleSql oracleSql, Connection connection, OraclePreparedStatement oraclePreparedStatement) throws SQLException {
        OracleParameterMetaData oracleParameterMetaData = null;
        String string = oracleSql.getSql(true, true);
        int n2 = oracleSql.getParameterCount();
        OracleParameterMetaDataParser oracleParameterMetaDataParser = null;
        String string2 = null;
        if (!oracleSql.sqlKind.isPlsqlOrCall() && oracleSql.getReturnParameterCount() < 1 && n2 > 0 && !BAD_SQL.contains(string.hashCode())) {
            oracleParameterMetaDataParser = new OracleParameterMetaDataParser();
            oracleParameterMetaDataParser.initialize(string, oracleSql.sqlKind, n2);
            try {
                string2 = oracleParameterMetaDataParser.getParameterMetaDataSql();
            }
            catch (Exception exception) {
                string2 = null;
            }
        }
        if (string2 == null) {
            oracleParameterMetaData = new OracleParameterMetaData(n2);
        } else {
            try (PreparedStatement preparedStatement = null;){
                preparedStatement = connection.prepareStatement(string2);
                ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
                oracleParameterMetaData = resultSetMetaData.getColumnCount() != n2 ? new OracleParameterMetaData(n2) : (oracleParameterMetaDataParser != null && oracleParameterMetaDataParser.needBindStatusForParameterMetaData() ? new OracleParameterMetaData(resultSetMetaData, oracleSql.getParameterCount(), oracleParameterMetaDataParser.getBindStatusForInsert()) : new OracleParameterMetaData(resultSetMetaData));
            }
        }
        return oracleParameterMetaData;
    }

    private OracleParameterMetaData(ResultSetMetaData resultSetMetaData) throws SQLException {
        this.parameterCount = resultSetMetaData.getColumnCount();
        this.isNullable = new int[this.parameterCount];
        this.isSigned = new boolean[this.parameterCount];
        this.precision = new int[this.parameterCount];
        this.scale = new int[this.parameterCount];
        this.parameterType = new int[this.parameterCount];
        this.parameterTypeName = new String[this.parameterCount];
        this.parameterClassName = new String[this.parameterCount];
        this.parameterMode = new int[this.parameterCount];
        int n2 = 1;
        int n3 = 0;
        while (n2 <= this.parameterCount) {
            this.isNullable[n3] = resultSetMetaData.isNullable(n2);
            this.isSigned[n3] = resultSetMetaData.isSigned(n2);
            this.precision[n3] = resultSetMetaData.getPrecision(n2);
            this.scale[n3] = resultSetMetaData.getScale(n2);
            this.parameterType[n3] = resultSetMetaData.getColumnType(n2);
            this.parameterTypeName[n3] = resultSetMetaData.getColumnTypeName(n2);
            this.parameterClassName[n3] = resultSetMetaData.getColumnClassName(n2);
            this.parameterMode[n3] = this.parameterModeIn;
            ++n2;
            ++n3;
        }
    }

    private OracleParameterMetaData(ResultSetMetaData resultSetMetaData, int n2, byte[] byArray) throws SQLException {
        this.parameterCount = n2;
        this.isNullable = new int[this.parameterCount];
        this.isSigned = new boolean[this.parameterCount];
        this.precision = new int[this.parameterCount];
        this.scale = new int[this.parameterCount];
        this.parameterType = new int[this.parameterCount];
        this.parameterTypeName = new String[this.parameterCount];
        this.parameterClassName = new String[this.parameterCount];
        this.parameterMode = new int[this.parameterCount];
        int n3 = 0;
        for (int i2 = 1; i2 <= this.parameterCount; ++i2) {
            this.isNullable[n3] = resultSetMetaData.isNullable(i2);
            this.isSigned[n3] = resultSetMetaData.isSigned(i2);
            this.precision[n3] = resultSetMetaData.getPrecision(i2);
            this.scale[n3] = resultSetMetaData.getScale(i2);
            this.parameterType[n3] = resultSetMetaData.getColumnType(i2);
            this.parameterTypeName[n3] = resultSetMetaData.getColumnTypeName(i2);
            this.parameterClassName[n3] = resultSetMetaData.getColumnClassName(i2);
            this.parameterMode[n3] = this.parameterModeIn;
            ++n3;
        }
    }

    OracleParameterMetaData(int n2) throws SQLException {
        this.parameterCount = n2;
        this.throwUnsupportedFeature = true;
    }

    @Override
    public int getParameterCount() throws SQLException {
        return this.parameterCount;
    }

    void checkValidIndex(int n2) throws SQLException {
        if (this.throwUnsupportedFeature) {
            throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("checkValidIndex").fillInStackTrace();
        }
        if (n2 < 1 || n2 > this.parameterCount) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
    }

    @Override
    public int isNullable(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.isNullable[n2 - 1];
    }

    @Override
    public boolean isSigned(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.isSigned[n2 - 1];
    }

    @Override
    public int getPrecision(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.precision[n2 - 1];
    }

    @Override
    public int getScale(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.scale[n2 - 1];
    }

    @Override
    public int getParameterType(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.parameterType[n2 - 1];
    }

    @Override
    public String getParameterTypeName(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.parameterTypeName[n2 - 1];
    }

    @Override
    public String getParameterClassName(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.parameterClassName[n2 - 1];
    }

    @Override
    public int getParameterMode(int n2) throws SQLException {
        this.checkValidIndex(n2);
        return this.parameterMode[n2 - 1];
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }
}

