/*
 * Decompiled with CFR 0.152.
 */
package oracle.core.lvf;

public final class VersionMgr {
    public static final byte ALPHA = 1;
    public static final byte BETA = 2;
    public static final byte PROD = 3;
    public static final byte NONE = 4;
    private final byte MAX_LEN = (byte)64;
    private final byte MAX_PRODLEN = (byte)30;
    private final byte MAX_VERLEN = (byte)15;
    private final byte MAX_DISTLEN = (byte)5;
    private final String alpha = "Alpha";
    private final String beta = "Beta";
    private final String prod = "Production";
    private String version;

    public void setVersion(String string, byte by, byte by2, byte by3, byte by4, byte by5, char c2, String string2, byte by6, int n2) {
        char[] cArray = new char[64];
        String string3 = "";
        int n3 = string.length();
        if (n3 > 30) {
            n3 = 30;
        }
        int n4 = 0;
        while (true) {
            int n5 = n3;
            n3 = (byte)(n3 - 1);
            if (0 >= n5) break;
            cArray[n4] = string.charAt(n4);
            n4 = (byte)(n4 + 1);
        }
        int n6 = n4;
        n4 = (byte)(n4 + 1);
        cArray[n6] = 9;
        if (by < 0) {
            by = 0;
        }
        if (by2 < 0) {
            by2 = 0;
        }
        if (by3 < 0) {
            by3 = 0;
        }
        if (by4 < 0) {
            by4 = 0;
        }
        if (by5 < 0) {
            by5 = 0;
        }
        if (by > 99) {
            by = (byte)99;
        }
        if (by2 > 99) {
            by2 = (byte)99;
        }
        if (by3 > 99) {
            by3 = (byte)99;
        }
        if (by4 > 99) {
            by4 = (byte)99;
        }
        if (by5 > 99) {
            by5 = (byte)99;
        }
        String string4 = c2 != '\u0000' ? by + "." + by2 + "." + by3 + "." + by4 + "." + by5 + c2 : by + "." + by2 + "." + by3 + "." + by4 + "." + by5;
        byte by7 = (byte)string4.length();
        int n7 = 0;
        while (true) {
            byte by8 = by7;
            by7 = (byte)(by7 - 1);
            if (0 >= by8) break;
            int n8 = n4;
            n4 = (byte)(n4 + 1);
            int n9 = n7;
            n7 = (byte)(n7 + 1);
            cArray[n8] = string4.charAt(n9);
        }
        if (by6 != 4) {
            int n10 = n4;
            n4 = (byte)(n4 + 1);
            cArray[n10] = 9;
            if (string2 != null) {
                int n11 = 0;
                n11 = string2.length();
                if (n11 > 5) {
                    n11 = 5;
                }
                n7 = 0;
                while (true) {
                    int n12 = n11;
                    n11 = (byte)(n11 - 1);
                    if (0 >= n12) break;
                    int n13 = n4;
                    n4 = (byte)(n4 + 1);
                    int n14 = n7;
                    n7 = (byte)(n7 + 1);
                    cArray[n13] = string2.charAt(n14);
                }
                int n15 = n4;
                n4 = (byte)(n4 + 1);
                cArray[n15] = 9;
            }
            switch (by6) {
                case 1: {
                    string3 = "Alpha";
                    break;
                }
                case 2: {
                    string3 = "Beta";
                    break;
                }
                case 3: {
                    string3 = "Production";
                }
            }
            n7 = 0;
            byte by9 = (byte)string3.length();
            while (true) {
                byte by10 = by9;
                by9 = (byte)(by9 - 1);
                if (0 >= by10) break;
                int n16 = n4;
                n4 = (byte)(n4 + 1);
                int n17 = n7;
                n7 = (byte)(n7 + 1);
                cArray[n16] = string3.charAt(n17);
            }
        }
        this.version = new String(cArray, 0, n4);
    }

    public String getVersion() {
        return this.version;
    }
}

