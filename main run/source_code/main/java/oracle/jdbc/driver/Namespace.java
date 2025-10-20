/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class Namespace {
    static final int ATTRIBUTE_MAX_LENGTH = 30;
    static final int VALUE_MAX_LENGTH = 4000;
    String name;
    boolean clear;
    String[] keys;
    String[] values;
    int nbPairs;

    Namespace(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        this.name = string;
        this.clear = false;
        this.nbPairs = 0;
        this.keys = new String[5];
        this.values = new String[5];
    }

    void clear() {
        this.clear = true;
        for (int i2 = 0; i2 < this.nbPairs; ++i2) {
            this.keys[i2] = null;
            this.values[i2] = null;
        }
        this.nbPairs = 0;
    }

    void setAttribute(String string, String string2) {
        if (string == null || string2 == null || string.equals("")) {
            throw new NullPointerException();
        }
        if (this.nbPairs == this.keys.length) {
            String[] stringArray = new String[this.keys.length * 2];
            String[] stringArray2 = new String[this.keys.length * 2];
            System.arraycopy(this.keys, 0, stringArray, 0, this.keys.length);
            System.arraycopy(this.values, 0, stringArray2, 0, this.values.length);
            this.keys = stringArray;
            this.values = stringArray2;
        }
        this.keys[this.nbPairs] = string;
        this.values[this.nbPairs] = string2;
        ++this.nbPairs;
    }
}

