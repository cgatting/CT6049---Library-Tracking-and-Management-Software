/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OraclePreparedStatement;

public class PlsqlIbtBindInfo {
    Object[] arrayData;
    int maxLen;
    int curLen;
    int element_internal_type;
    int elemMaxLen;
    int ibtByteLength;
    int ibtCharLength;
    int ibtValueIndex;
    int ibtIndicatorIndex;
    int ibtLengthIndex;

    PlsqlIbtBindInfo(OraclePreparedStatement oraclePreparedStatement, Object[] objectArray, int n2, int n3, int n4, int n5) throws SQLException {
        this.arrayData = objectArray;
        this.maxLen = n2;
        this.curLen = n3;
        this.element_internal_type = n4;
        boolean bl = this.arrayData == null;
        switch (this.element_internal_type) {
            case 1: 
            case 96: {
                if (bl) {
                    this.elemMaxLen = n5 + 1;
                    this.ibtCharLength = this.elemMaxLen * this.maxLen;
                } else {
                    this.elemMaxLen = n5 == 0 ? 2 : n5 + 1;
                    this.ibtCharLength = this.elemMaxLen * this.maxLen;
                }
                this.element_internal_type = 9;
                break;
            }
            case 6: 
            case 12: 
            case 180: {
                this.elemMaxLen = 22;
                this.ibtByteLength = this.elemMaxLen * this.maxLen;
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(97).fillInStackTrace();
            }
        }
    }
}

