/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.nio.ByteBuffer;

class MessageFormatUtils {
    private static final int bytesPerLine = 8;
    private static final String DIVIDER = "     |";
    private static final String BLANK_SPACE = "   ";
    private static final String[] toHex = new String[]{" 00", " 01", " 02", " 03", " 04", " 05", " 06", " 07", " 08", " 09", " 0A", " 0B", " 0C", " 0D", " 0E", " 0F", " 10", " 11", " 12", " 13", " 14", " 15", " 16", " 17", " 18", " 19", " 1A", " 1B", " 1C", " 1D", " 1E", " 1F", " 20", " 21", " 22", " 23", " 24", " 25", " 26", " 27", " 28", " 29", " 2A", " 2B", " 2C", " 2D", " 2E", " 2F", " 30", " 31", " 32", " 33", " 34", " 35", " 36", " 37", " 38", " 39", " 3A", " 3B", " 3C", " 3D", " 3E", " 3F", " 40", " 41", " 42", " 43", " 44", " 45", " 46", " 47", " 48", " 49", " 4A", " 4B", " 4C", " 4D", " 4E", " 4F", " 50", " 51", " 52", " 53", " 54", " 55", " 56", " 57", " 58", " 59", " 5A", " 5B", " 5C", " 5D", " 5E", " 5F", " 60", " 61", " 62", " 63", " 64", " 65", " 66", " 67", " 68", " 69", " 6A", " 6B", " 6C", " 6D", " 6E", " 6F", " 70", " 71", " 72", " 73", " 74", " 75", " 76", " 77", " 78", " 79", " 7A", " 7B", " 7C", " 7D", " 7E", " 7F", " 80", " 81", " 82", " 83", " 84", " 85", " 86", " 87", " 88", " 89", " 8A", " 8B", " 8C", " 8D", " 8E", " 8F", " 90", " 91", " 92", " 93", " 94", " 95", " 96", " 97", " 98", " 99", " 9A", " 9B", " 9C", " 9D", " 9E", " 9F", " A0", " A1", " A2", " A3", " A4", " A5", " A6", " A7", " A8", " A9", " AA", " AB", " AC", " AD", " AE", " AF", " B0", " B1", " B2", " B3", " B4", " B5", " B6", " B7", " B8", " B9", " BA", " BB", " BC", " BD", " BE", " BF", " C0", " C1", " C2", " C3", " C4", " C5", " C6", " C7", " C8", " C9", " CA", " CB", " CC", " CD", " CE", " CF", " D0", " D1", " D2", " D3", " D4", " D5", " D6", " D7", " D8", " D9", " DA", " DB", " DC", " DD", " DE", " DF", " E0", " E1", " E2", " E3", " E4", " E5", " E6", " E7", " E8", " E9", " EA", " EB", " EC", " ED", " EE", " EF", " F0", " F1", " F2", " F3", " F4", " F5", " F6", " F7", " F8", " F9", " FA", " FB", " FC", " FD", " FE", " FF"};
    private static final char[] toChar = new char[]{'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};

    private MessageFormatUtils() {
    }

    public static Object formatLogParam(Object object) {
        Object object2 = object instanceof byte[] ? MessageFormatUtils.formatPacket((byte[])object, 0, ((byte[])object).length) : (object instanceof ByteBuffer ? MessageFormatUtils.formatPacket((ByteBuffer)object, 0, ((ByteBuffer)object).remaining()) : object);
        return object2;
    }

    public static Object[] formatLogParams(Object[] objectArray) {
        Object[] objectArray2 = new Object[objectArray.length];
        for (int i2 = 0; i2 < objectArray.length; ++i2) {
            objectArray2[i2] = objectArray[i2] instanceof byte[] ? MessageFormatUtils.formatPacket((byte[])objectArray[i2], 0, ((byte[])objectArray[i2]).length) : (objectArray[i2] instanceof ByteBuffer ? MessageFormatUtils.formatPacket((ByteBuffer)objectArray[i2], 0, ((ByteBuffer)objectArray[i2]).remaining()) : objectArray[i2]);
        }
        return objectArray2;
    }

    public static String formatPacket(byte[] byArray, int n2, int n3) {
        return MessageFormatUtils.formatPacket(ByteBuffer.wrap(byArray, n2, n3), 0, n3);
    }

    public static String formatPacket(ByteBuffer byteBuffer, int n2, int n3) {
        StringBuilder stringBuilder = new StringBuilder(16384);
        StringBuilder stringBuilder2 = new StringBuilder(80);
        if (byteBuffer == null) {
            return "NULL";
        }
        int n4 = byteBuffer.position();
        int n5 = byteBuffer.limit();
        byteBuffer.position(n2);
        byteBuffer.limit(n3);
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder2.delete(0, stringBuilder2.length());
        int n6 = 0;
        while (byteBuffer.hasRemaining()) {
            int n7 = byteBuffer.get() & 0xFF;
            stringBuilder.append(toHex[n7]);
            stringBuilder2.append(toChar[n7]);
            if (++n6 != 8) continue;
            stringBuilder.append(DIVIDER);
            stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length()));
            stringBuilder.append("|\n");
            stringBuilder2.delete(0, stringBuilder2.length());
            n6 = 0;
        }
        if (n6 > 0) {
            int n8;
            int n9 = 8 - n6 - 1;
            for (n8 = 0; n8 <= n9; ++n8) {
                stringBuilder.append(BLANK_SPACE);
            }
            stringBuilder.append(DIVIDER);
            stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length()));
            for (n8 = 0; n8 <= n9; ++n8) {
                stringBuilder.append(" ");
            }
            stringBuilder.append("|\n");
            stringBuilder2.delete(0, stringBuilder2.length());
        }
        byteBuffer.rewind();
        if (n5 >= 0) {
            byteBuffer.limit(n5);
        }
        if (n4 >= 0 && n4 <= n5) {
            byteBuffer.position(n4);
        }
        return stringBuilder.substring(0, stringBuilder.length());
    }
}

