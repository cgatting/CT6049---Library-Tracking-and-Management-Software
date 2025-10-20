/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DBACopyingBinder;
import oracle.jdbc.driver.LongRawStreamBinder;
import oracle.jdbc.driver.LongRawStreamForBytesBinder;
import oracle.jdbc.driver.LongRawStreamForBytesCopyingBinder;
import oracle.jdbc.driver.LongStreamBinder;
import oracle.jdbc.driver.LongStreamForStringBinder;
import oracle.jdbc.driver.LongStreamForStringCopyingBinder;
import oracle.jdbc.driver.OutBinder;
import oracle.jdbc.driver.PlsqlIbtBinder;
import oracle.jdbc.driver.PlsqlIbtCopyingBinder;
import oracle.jdbc.driver.PlsqlIbtNullBinder;
import oracle.jdbc.driver.ReturnParamBinder;

class OraclePreparedStatementReadOnly {
    static Binder theStaticDBACopyingBinder = new DBACopyingBinder();
    static Binder theStaticLongStreamBinder = new LongStreamBinder();
    static Binder theStaticLongStreamForStringBinder = new LongStreamForStringBinder();
    static Binder theStaticLongStreamForStringCopyingBinder = new LongStreamForStringCopyingBinder();
    static Binder theStaticLongRawStreamBinder = new LongRawStreamBinder();
    static Binder theStaticLongRawStreamForBytesBinder = new LongRawStreamForBytesBinder();
    static Binder theStaticLongRawStreamForBytesCopyingBinder = new LongRawStreamForBytesCopyingBinder();
    static Binder theStaticPlsqlIbtCopyingBinder = new PlsqlIbtCopyingBinder();
    static Binder theStaticPlsqlIbtBinder = new PlsqlIbtBinder();
    static Binder theStaticPlsqlIbtNullBinder = new PlsqlIbtNullBinder();
    static Binder theStaticOutBinder = new OutBinder();
    static Binder theStaticReturnParamBinder = new ReturnParamBinder();

    OraclePreparedStatementReadOnly() {
    }
}

