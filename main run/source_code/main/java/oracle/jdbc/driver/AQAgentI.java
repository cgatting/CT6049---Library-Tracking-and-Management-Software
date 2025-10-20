/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.aq.AQAgent;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class AQAgentI
implements AQAgent {
    private String attrAgentAddress = null;
    private String attrAgentName = null;
    private int attrAgentProtocol = 0;

    AQAgentI() throws SQLException {
    }

    @Override
    public void setAddress(String string) throws SQLException {
        this.attrAgentAddress = string;
    }

    @Override
    public String getAddress() {
        return this.attrAgentAddress;
    }

    @Override
    public void setName(String string) throws SQLException {
        this.attrAgentName = string;
    }

    @Override
    public String getName() {
        return this.attrAgentName;
    }

    @Override
    public void setProtocol(int n2) throws SQLException {
        this.attrAgentProtocol = n2;
    }

    @Override
    public int getProtocol() {
        return this.attrAgentProtocol;
    }

    @Override
    @DisableTrace
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Name=\"");
        stringBuffer.append(this.getName());
        stringBuffer.append("\" ");
        stringBuffer.append("Address=\"");
        stringBuffer.append(this.getAddress());
        stringBuffer.append("\" ");
        stringBuffer.append("Protocol=\"");
        stringBuffer.append(this.getProtocol());
        stringBuffer.append("\"");
        return stringBuffer.toString();
    }
}

