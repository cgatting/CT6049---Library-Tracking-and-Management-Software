/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
public class OracleSQLException
extends SQLException {
    private static final long serialVersionUID = -3610529188069835363L;
    private Object[] m_parameters;

    public OracleSQLException() {
        this(null, null, 0);
    }

    public OracleSQLException(String string) {
        this(string, null, 0);
    }

    public OracleSQLException(String string, String string2) {
        this(string, string2, 0);
    }

    public OracleSQLException(String string, String string2, int n2) {
        this(string, string2, n2, (Object[])null);
    }

    public OracleSQLException(String string, String string2, int n2, Object[] objectArray) {
        super(string, string2, n2);
        this.m_parameters = objectArray;
    }

    public Object[] getParameters() {
        if (this.m_parameters == null) {
            this.m_parameters = new Object[0];
        }
        return this.m_parameters;
    }

    public int getNumParameters() {
        if (this.m_parameters == null) {
            this.m_parameters = new Object[0];
        }
        return this.m_parameters.length;
    }

    public void setParameters(Object[] objectArray) {
        this.m_parameters = objectArray;
    }
}

