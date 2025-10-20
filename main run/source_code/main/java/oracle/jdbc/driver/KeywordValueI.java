/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.SQLException;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.T4CMAREngine;
import oracle.jdbc.internal.KeywordValue;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
final class KeywordValueI
extends KeywordValue {
    private int keyword;
    private byte[] binaryValue;
    private String textValue;
    private byte[] textValueArr;

    KeywordValueI(int n2, String string, byte[] byArray) {
        this.keyword = n2;
        this.textValue = string;
        this.binaryValue = byArray;
        this.textValueArr = null;
    }

    void doCharConversion(DBConversion dBConversion) throws SQLException {
        this.textValueArr = (byte[])(this.textValue != null ? dBConversion.StringToCharBytes(this.textValue) : null);
    }

    @Override
    public byte[] getBinaryValue() throws SQLException {
        return this.binaryValue;
    }

    @Override
    public String getTextValue() throws SQLException {
        return this.textValue;
    }

    @Override
    public int getKeyword() throws SQLException {
        return this.keyword;
    }

    void marshal(T4CMAREngine t4CMAREngine) throws IOException {
        if (this.textValueArr != null) {
            t4CMAREngine.marshalUB2(this.textValueArr.length);
            t4CMAREngine.marshalCLR(this.textValueArr, this.textValueArr.length);
            t4CMAREngine.marshalUB2(0);
        } else {
            t4CMAREngine.marshalUB2(0);
            if (this.binaryValue != null) {
                t4CMAREngine.marshalUB2(this.binaryValue.length);
                t4CMAREngine.marshalCLR(this.binaryValue, this.binaryValue.length);
            } else {
                t4CMAREngine.marshalUB2(0);
            }
        }
        t4CMAREngine.marshalUB2(this.keyword);
    }

    static KeywordValueI unmarshal(T4CMAREngine t4CMAREngine) throws SQLException, IOException {
        int n2;
        int[] nArray = new int[1];
        String string = null;
        byte[] byArray = null;
        int n3 = t4CMAREngine.unmarshalUB2();
        if (n3 != 0) {
            byte[] byArray2 = new byte[n3];
            t4CMAREngine.unmarshalCLR(byArray2, 0, nArray);
            string = t4CMAREngine.conv.CharBytesToString(byArray2, byArray2.length);
        }
        if ((n2 = t4CMAREngine.unmarshalUB2()) != 0) {
            byArray = new byte[n2];
            t4CMAREngine.unmarshalCLR(byArray, 0, nArray);
        }
        int n4 = t4CMAREngine.unmarshalUB2();
        return new KeywordValueI(n4, string, byArray);
    }
}

