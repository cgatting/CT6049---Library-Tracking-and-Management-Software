/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.ForwardOnlyResultSet;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ArrayLocatorResultSet
extends ForwardOnlyResultSet {
    static int COUNT_UNLIMITED = -1;
    Map<String, Class<?>> map;
    long beginIndex;
    int count;
    long currentIndex;

    static ArrayLocatorResultSet create(PhysicalConnection physicalConnection, ArrayDescriptor arrayDescriptor, byte[] byArray, Map<String, Class<?>> map) throws SQLException {
        return ArrayLocatorResultSet.create(physicalConnection, arrayDescriptor, byArray, 0L, COUNT_UNLIMITED, map);
    }

    static ArrayLocatorResultSet create(PhysicalConnection physicalConnection, ArrayDescriptor arrayDescriptor, byte[] byArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        ARRAY aRRAY = new ARRAY(arrayDescriptor, physicalConnection, (Object)null);
        aRRAY.setLocator(byArray);
        oraclePreparedStatement = arrayDescriptor.getBaseType() == 2002 || arrayDescriptor.getBaseType() == 2008 ? physicalConnection.prepareStatementInternal("SELECT ROWNUM, SYS_NC_ROWINFO$ FROM TABLE( CAST(:1 AS " + arrayDescriptor.getName() + ") )", 1003, 1007) : physicalConnection.prepareStatementInternal("SELECT ROWNUM, COLUMN_VALUE FROM TABLE( CAST(:1 AS " + arrayDescriptor.getName() + ") )", 1003, 1007);
        oraclePreparedStatement.setArray(1, aRRAY);
        oraclePreparedStatement.executeQuery();
        return new ArrayLocatorResultSet(physicalConnection, oraclePreparedStatement, l2, n2, map);
    }

    private ArrayLocatorResultSet(OracleConnection oracleConnection, OracleStatement oracleStatement, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        super((PhysicalConnection)oracleConnection, oracleStatement);
        if (oracleConnection == null || oracleStatement == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Invalid arguments").fillInStackTrace();
        }
        oracleStatement.closeOnCompletion();
        this.count = n2;
        this.currentIndex = 0L;
        this.beginIndex = l2;
        this.map = map;
    }

    @Override
    public boolean next() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.currentIndex < this.beginIndex) {
                while (this.currentIndex < this.beginIndex) {
                    ++this.currentIndex;
                    if (super.next()) continue;
                    boolean bl = false;
                    return bl;
                }
                boolean bl = true;
                return bl;
            }
            if (this.count == COUNT_UNLIMITED) {
                boolean bl = super.next();
                return bl;
            }
            if (this.currentIndex < this.beginIndex + (long)this.count - 1L) {
                ++this.currentIndex;
                boolean bl = super.next();
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object = this.getObject(n2, this.map);
            return object;
        }
    }

    @Override
    public int findColumn(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (string.equalsIgnoreCase("index")) {
                int n2 = 1;
                return n2;
            }
            if (string.equalsIgnoreCase("value")) {
                int n3 = 2;
                return n3;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6, "get_column_index").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }
}

