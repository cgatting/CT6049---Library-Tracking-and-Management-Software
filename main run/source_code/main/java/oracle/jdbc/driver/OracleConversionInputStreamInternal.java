/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.OracleConversionInputStream;

public class OracleConversionInputStreamInternal
extends OracleConversionInputStream {
    boolean needReset = false;

    public OracleConversionInputStreamInternal(DBConversion dBConversion, InputStream inputStream, int n2, int n3) {
        super(dBConversion, inputStream, n2, n3);
    }

    public OracleConversionInputStreamInternal(DBConversion dBConversion, Reader reader, int n2, int n3, short s2) {
        super(dBConversion, reader, n2, n3, s2);
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        int n4;
        if (this.needReset) {
            if (this.istream != null && this.istream.markSupported()) {
                this.istream.reset();
                this.endOfStream = false;
                this.totalSize = 0;
                this.needReset = false;
            } else if (this.reader != null && this.reader.markSupported()) {
                this.reader.reset();
                this.endOfStream = false;
                this.totalSize = 0;
                this.needReset = false;
            }
        }
        if ((n4 = super.read(byArray, n2, n3)) == -1) {
            this.needReset = true;
        }
        return n4;
    }
}

