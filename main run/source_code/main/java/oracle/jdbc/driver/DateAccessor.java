/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.DateTimeCommonAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.Datum;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class DateAccessor
extends DateTimeCommonAccessor {
    static final int MAXLENGTH = 7;

    DateAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.DATE, oracleStatement, 7, bl);
        this.init(oracleStatement, 12, 12, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    DateAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.DATE, oracleStatement, 7, false);
        this.init(oracleStatement, 12, 12, s2, false);
        this.initForDescribe(12, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    String getString(int n2) throws SQLException {
        String string = null;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        if (this.externalType == 0) {
            string = this.statement.connection.mapDateToTimestamp ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.getTimestamp(n2)) : this.getDate(n2).toString();
        } else {
            this.getBytesInternal(n2, this.tmpBytes);
            int n3 = this.oracleYear(this.tmpBytes);
            int n4 = 0;
            n4 = this.tmpBytes[4] - 1;
            string = this.toText(n3, this.tmpBytes[2], this.tmpBytes[3], n4, this.tmpBytes[5] - 1, this.tmpBytes[6] - 1, -1, n4 < 12, null);
        }
        return string;
    }

    @Override
    Object getObject(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        if (this.externalType == 0) {
            if (this.statement.connection.mapDateToTimestamp) {
                return this.getTimestamp(n2);
            }
            return this.getDate(n2);
        }
        switch (this.externalType) {
            case 91: {
                return this.getDate(n2);
            }
            case 92: {
                return this.getTime(n2);
            }
            case 93: {
                return this.getTimestamp(n2);
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getDATE(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getObject(n2);
    }
}

