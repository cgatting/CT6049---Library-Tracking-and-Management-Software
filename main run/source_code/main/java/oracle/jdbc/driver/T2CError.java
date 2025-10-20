/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

class T2CError {
    int m_errorNumber;
    byte[] m_errorMessage = new byte[1024];
    byte[] m_sqlState = new byte[6];
    short m_errorPosition = (short)-1;

    T2CError() {
    }
}

