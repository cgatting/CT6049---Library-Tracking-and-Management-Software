/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.time.Period;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.Datum;
import oracle.sql.INTERVALYM;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class IntervalymAccessor
extends Accessor {
    static final int MAXLENGTH = 5;
    private static int INTYMYEAROFFSET = Integer.MIN_VALUE;
    private static int INTYMMONTHOFFSET = 60;

    IntervalymAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.INTERVALYM, oracleStatement, 5, bl);
        this.init(oracleStatement, 182, 182, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    IntervalymAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.INTERVALYM, oracleStatement, 5, false);
        this.init(oracleStatement, 182, 182, s2, false);
        this.initForDescribe(182, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    String getString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return this.getINTERVALYM(n2).toString();
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getINTERVALYM(n2);
    }

    @Override
    Period getPeriod(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.getBytesInternal(n2);
        int n3 = (byArray[0] & 0xFF) << 24;
        n3 |= (byArray[1] & 0xFF) << 16;
        n3 |= (byArray[2] & 0xFF) << 8;
        n3 |= byArray[3] & 0xFF;
        int n4 = byArray[4] - INTYMMONTHOFFSET;
        return Period.of(n3 -= INTYMYEAROFFSET, n4, 0);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getINTERVALYM(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getINTERVALYM(n2);
    }

    @Override
    INTERVALYM getINTERVALYM(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new INTERVALYM(this.getBytesInternal(n2));
    }
}

