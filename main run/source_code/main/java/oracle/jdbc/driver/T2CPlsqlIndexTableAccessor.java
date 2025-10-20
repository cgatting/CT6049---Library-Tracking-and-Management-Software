/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.math.BigDecimal;
import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.jdbc.driver.PlsqlIndexTableAccessor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CHAR;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;
import oracle.sql.NUMBER;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
class T2CPlsqlIndexTableAccessor
extends PlsqlIndexTableAccessor {
    int ibtMetaIndex;

    T2CPlsqlIndexTableAccessor(OracleStatement oracleStatement, PlsqlIbtBindInfo plsqlIbtBindInfo, short s2) throws SQLException {
        super(oracleStatement, plsqlIbtBindInfo, s2);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        this.unimpl("initForDataAccess");
    }

    @Override
    Object[] getPlsqlIndexTable(int n2) throws SQLException {
        Object[] objectArray;
        short[] sArray = this.statement.ibtBindIndicators;
        int n3 = ((sArray[this.ibtMetaIndex + 4] & 0xFFFF) << 16) + (sArray[this.ibtMetaIndex + 5] & 0xFFFF);
        long l2 = this.getOffset(n2);
        int n4 = this.ibtBindInfo.elemMaxLen;
        switch (this.ibtBindInfo.element_internal_type) {
            case 9: {
                int[] nArray = new int[1];
                objectArray = new String[n3];
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.rowData.setPosition(l2);
                    char[] cArray = this.rowData.getChars(l2, 1, this.statement.connection.conversion.getCharacterSet((short)1), nArray);
                    int n5 = cArray[0] / 2;
                    this.rowData.setPosition(l2 + 1L);
                    objectArray[i2] = n5 == 0 ? null : this.rowData.getString(n5, this.statement.connection.conversion.getCharacterSet((short)1));
                    l2 += (long)n4;
                }
                break;
            }
            case 6: {
                objectArray = new BigDecimal[n3];
                for (int i3 = 0; i3 < n3; ++i3) {
                    this.rowData.setPosition(l2);
                    int n6 = this.rowData.get() & 0xFF;
                    objectArray[i3] = n6 == 0 ? null : oracle.sql.NUMBER.toBigDecimal(this.rowData.getBytes(n6));
                    l2 += (long)n4;
                }
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
            }
        }
        return objectArray;
    }

    @Override
    Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        short[] sArray = this.statement.ibtBindIndicators;
        int n3 = ((sArray[this.ibtMetaIndex + 4] & 0xFFFF) << 16) + (sArray[this.ibtMetaIndex + 5] & 0xFFFF);
        long l2 = this.getOffset(n2);
        int n4 = this.ibtBindInfo.elemMaxLen;
        Datum[] datumArray = null;
        switch (this.ibtBindInfo.element_internal_type) {
            case 9: {
                int[] nArray = new int[1];
                datumArray = new CHAR[n3];
                CharacterSet characterSet = this.statement.connection.conversion.getDriverCharSetObj();
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.rowData.setPosition(l2);
                    char[] cArray = this.rowData.getChars(l2, 1, this.statement.connection.conversion.getCharacterSet((short)1), nArray);
                    int n5 = cArray[0] / 2;
                    this.rowData.setPosition(l2 + 1L);
                    if (n5 == 0) {
                        datumArray[i2] = null;
                    } else {
                        String string = this.rowData.getString(n5, this.statement.connection.conversion.getCharacterSet((short)1));
                        datumArray[i2] = new CHAR(string, characterSet);
                    }
                    l2 += (long)n4;
                }
                break;
            }
            case 6: {
                datumArray = new NUMBER[n3];
                for (int i3 = 0; i3 < n3; ++i3) {
                    this.rowData.setPosition(l2);
                    int n6 = this.rowData.get() & 0xFF;
                    datumArray[i3] = n6 == 0 ? null : new NUMBER(this.rowData.getBytes(n6));
                    l2 += (long)n4;
                }
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
            }
        }
        return datumArray;
    }
}

