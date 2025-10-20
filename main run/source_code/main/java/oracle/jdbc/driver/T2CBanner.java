/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.DBConversion;

class T2CBanner {
    private byte[] rawBanner = new byte[4096];
    private int rawBannerLen = 0;
    private String banner = null;
    private DBConversion conversion;
    static final int T2C_MAX_BANNER_LENGTH = 4096;

    T2CBanner(DBConversion dBConversion) {
        this.conversion = dBConversion;
    }

    String getBanner() throws SQLException {
        if (this.rawBannerLen > 0) {
            this.banner = this.conversion.CharBytesToString(this.rawBanner, this.rawBannerLen);
        }
        return this.banner;
    }
}

