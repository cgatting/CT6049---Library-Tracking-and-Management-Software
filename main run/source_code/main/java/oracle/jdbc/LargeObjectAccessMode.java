/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

public enum LargeObjectAccessMode {
    MODE_READONLY(0),
    MODE_READWRITE(1);

    private final int code;

    private LargeObjectAccessMode(int n3) {
        this.code = n3;
    }

    public int getCode() {
        return this.code;
    }
}

