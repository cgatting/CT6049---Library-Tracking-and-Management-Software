/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
@DefaultLevel(value=Logging.FINEST)
class TranslatedErrorInfo {
    private int errorCode;
    private String sqlState;

    TranslatedErrorInfo() {
    }

    TranslatedErrorInfo(int n2, String string) {
        this();
        this.errorCode = n2;
        this.sqlState = string;
    }

    int getErrorCode() {
        return this.errorCode;
    }

    String getSqlState() {
        return this.sqlState;
    }

    void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    void setSqlState(String string) {
        this.sqlState = string;
    }
}

