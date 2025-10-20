/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.math.BigDecimal;
import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CHAR;
import oracle.sql.CharacterSet;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import oracle.sql.TIMESTAMP;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class PlsqlIndexTableAccessor
extends Accessor {
    static final int MAXLENGTH = -1;
    PlsqlIbtBindInfo ibtBindInfo;

    PlsqlIndexTableAccessor(OracleStatement oracleStatement, PlsqlIbtBindInfo plsqlIbtBindInfo, short s2) throws SQLException {
        super(Representation.PLSQL_INDEX_TABLE, oracleStatement, -1, true);
        this.init(oracleStatement, 998, 998, s2, true);
        this.ibtBindInfo = plsqlIbtBindInfo;
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        this.unimpl("initForDataAccess");
    }

    @Override
    PlsqlIbtBindInfo plsqlIndexTableBindInfo() throws SQLException {
        return this.ibtBindInfo;
    }

    Object[] getPlsqlIndexTable(int n2) throws SQLException {
        Object[] objectArray;
        int n3 = 0;
        if (!this.isNull(n2)) {
            this.rowData.setPosition(this.getOffset(n2));
            n3 = this.rowData.getInt();
        }
        switch (this.ibtBindInfo.element_internal_type) {
            case 9: {
                objectArray = new String[n3];
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n4 = this.rowData.getShort();
                    objectArray[i2] = n4 == 0 ? null : this.rowData.getString(n4, this.statement.connection.conversion.getCharacterSet((short)1));
                }
                break;
            }
            case 6: {
                objectArray = new BigDecimal[n3];
                for (int i3 = 0; i3 < n3; ++i3) {
                    int n5 = this.rowData.getShort();
                    objectArray[i3] = n5 == 0 ? null : oracle.sql.NUMBER.toBigDecimal(this.rowData.getBytes(n5));
                }
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
            }
        }
        assert (this.rowData.getPosition() == this.getOffset(n2) + (long)this.getLength(n2)) : "rowData.position(): " + this.rowData.getPosition() + " getOffset(" + n2 + "): " + this.getOffset(n2) + " getLength(" + n2 + "): " + this.getLength(n2);
        return objectArray;
    }

    @Override
    Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        Datum[] datumArray = null;
        int n3 = 0;
        if (!this.isNull(n2)) {
            this.rowData.setPosition(this.getOffset(n2));
            n3 = this.rowData.getInt();
        }
        switch (this.ibtBindInfo.element_internal_type) {
            case 9: {
                datumArray = new CHAR[n3];
                CharacterSet characterSet = this.statement.connection.conversion.getDriverCharSetObj();
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n4 = this.rowData.getShort();
                    datumArray[i2] = n4 == 0 ? null : new CHAR(this.rowData.getBytes(n4), characterSet);
                }
                break;
            }
            case 6: {
                datumArray = new NUMBER[n3];
                for (int i3 = 0; i3 < n3; ++i3) {
                    int n5 = this.rowData.getShort();
                    datumArray[i3] = n5 == 0 ? null : new NUMBER(this.rowData.getBytes(n5));
                }
                break;
            }
            case 12: {
                datumArray = new DATE[n3];
                for (int i4 = 0; i4 < n3; ++i4) {
                    int n6 = this.rowData.getShort();
                    datumArray[i4] = n6 == 0 ? null : new DATE(this.rowData.getBytes(n6));
                }
                break;
            }
            case 180: {
                datumArray = new TIMESTAMP[n3];
                for (int i5 = 0; i5 < n3; ++i5) {
                    int n7 = this.rowData.getShort();
                    datumArray[i5] = n7 == 0 ? null : new TIMESTAMP(this.rowData.getBytes(n7));
                }
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
            }
        }
        if (!this.isNull(n2)) assert (this.rowData.getPosition() == this.getOffset(n2) + (long)this.getLength(n2)) : "rowData.position(): " + this.rowData.getPosition() + " getOffset(" + n2 + "): " + this.getOffset(n2) + " getLength(" + n2 + "): " + this.getLength(n2);
        return datumArray;
    }
}

