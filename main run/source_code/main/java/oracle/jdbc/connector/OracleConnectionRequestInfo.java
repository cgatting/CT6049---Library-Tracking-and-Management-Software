/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.spi.ConnectionRequestInfo
 */
package oracle.jdbc.connector;

import javax.resource.spi.ConnectionRequestInfo;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleConnectionRequestInfo
implements ConnectionRequestInfo {
    private String user = null;
    private String password = null;

    public OracleConnectionRequestInfo(String string, String string2) {
        this.user = string;
        this.password = string2;
    }

    @DisableTrace
    public String getUser() {
        return this.user;
    }

    public void setUser(String string) {
        this.user = string;
    }

    @DisableTrace
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String string) {
        this.password = string;
    }

    @DisableTrace
    public boolean equals(Object object) {
        if (!(object instanceof OracleConnectionRequestInfo)) {
            return false;
        }
        OracleConnectionRequestInfo oracleConnectionRequestInfo = (OracleConnectionRequestInfo)object;
        return this.user.equalsIgnoreCase(oracleConnectionRequestInfo.getUser()) && this.password.equals(oracleConnectionRequestInfo.getPassword());
    }
}

