/*
 * Decompiled with CFR 0.152.
 */
package oracle.core.lmx;

public class LmxRepConversion {
    public static void printInHex(byte by) {
        System.out.print((char)LmxRepConversion.nibbleToHex((byte)((by & 0xF0) >> 4)));
        System.out.print((char)LmxRepConversion.nibbleToHex((byte)(by & 0xF)));
    }

    public static byte nibbleToHex(byte by) {
        return (byte)((by = (byte)(by & 0xF)) < 10 ? by + 48 : by - 10 + 65);
    }

    public static byte asciiHexToNibble(byte by) {
        byte by2 = by >= 97 && by <= 102 ? (byte)(by - 97 + 10) : (by >= 65 && by <= 70 ? (byte)(by - 65 + 10) : (by >= 48 && by <= 57 ? (byte)(by - 48) : by));
        return by2;
    }

    public static void bArray2nibbles(byte[] byArray, byte[] byArray2) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray2[i2 * 2] = LmxRepConversion.nibbleToHex((byte)((byArray[i2] & 0xF0) >> 4));
            byArray2[i2 * 2 + 1] = LmxRepConversion.nibbleToHex((byte)(byArray[i2] & 0xF));
        }
    }

    public static String bArray2String(byte[] byArray) {
        StringBuffer stringBuffer = new StringBuffer(byArray.length * 2);
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            stringBuffer.append((char)LmxRepConversion.nibbleToHex((byte)((byArray[i2] & 0xF0) >> 4)));
            stringBuffer.append((char)LmxRepConversion.nibbleToHex((byte)(byArray[i2] & 0xF)));
        }
        return stringBuffer.toString();
    }

    public static byte[] nibbles2bArray(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length / 2];
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            byArray2[i2] = (byte)(LmxRepConversion.asciiHexToNibble(byArray[i2 * 2]) << 4);
            int n2 = i2;
            byArray2[n2] = (byte)(byArray2[n2] | LmxRepConversion.asciiHexToNibble(byArray[i2 * 2 + 1]));
        }
        return byArray2;
    }

    public static void printInHex(long l2) {
        byte[] byArray = LmxRepConversion.toHex(l2);
        System.out.print(new String(byArray, 0));
    }

    public static void printInHex(int n2) {
        byte[] byArray = LmxRepConversion.toHex(n2);
        System.out.print(new String(byArray, 0));
    }

    public static void printInHex(short s2) {
        byte[] byArray = LmxRepConversion.toHex(s2);
        System.out.print(new String(byArray, 0));
    }

    public static byte[] toHex(long l2) {
        int n2 = 16;
        byte[] byArray = new byte[n2];
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            byArray[i2] = LmxRepConversion.nibbleToHex((byte)(l2 & 0xFL));
            l2 >>= 4;
        }
        return byArray;
    }

    public static byte[] toHex(int n2) {
        int n3 = 8;
        byte[] byArray = new byte[n3];
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            byArray[i2] = LmxRepConversion.nibbleToHex((byte)(n2 & 0xF));
            n2 >>= 4;
        }
        return byArray;
    }

    public static byte[] toHex(short s2) {
        int n2 = 4;
        byte[] byArray = new byte[n2];
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            byArray[i2] = LmxRepConversion.nibbleToHex((byte)(s2 & 0xF));
            s2 = (short)(s2 >> 4);
        }
        return byArray;
    }
}

