/*
 * Decompiled with CFR 0.152.
 */
package oracle.core.lmx;

public class CoreException
extends Exception {
    public static final byte UNIMPLEMENTED = 1;
    public static final byte UNDERFLOW = 2;
    public static final byte OVERFLOW = 3;
    public static final byte INVALIDORLN = 4;
    public static final byte BADFORMATORLN = 5;
    public static final byte INVALIDORLD = 6;
    public static final byte BADFORMATORLD = 7;
    public static final byte BADYEAR = 8;
    public static final byte BADDAYYEAR = 9;
    public static final byte BADJULIANDATE = 10;
    public static final byte INVALIDINPUTN = 11;
    public static final byte NLSNOTSUPPORTED = 12;
    public static final byte INVALIDINPUT = 13;
    public static final byte CONVERSIONERROR = 14;
    private static final String[] _errmsgs = new String[]{"Unknown Exception", "Unimplemented method called", "Underflow Exception", "Overflow Exception", "Invalid Oracle Number", "Bad Oracle Number format", "Invalid Oracle Date", "Bad Oracle Date format", "Year Not in Range", "Day of Year Not in Range", "Julian Date Not in Range", "Invalid Input Number", "NLS Not Supported", "Invalid Input", "Conversion Error"};
    private byte ecode;

    public CoreException() {
    }

    public CoreException(String string) {
        super(string);
    }

    public CoreException(byte by) {
        this.ecode = by;
    }

    public void setErrorCode(byte by) {
        this.ecode = by;
    }

    public byte getErrorCode() {
        return this.ecode;
    }

    @Override
    public String getMessage() {
        if (this.ecode == 0) {
            return super.getMessage();
        }
        return CoreException.getMessage(this.ecode);
    }

    public static String getMessage(byte by) {
        if (by < 1 || by > 14) {
            return "Unknown exception";
        }
        return _errmsgs[by];
    }
}

