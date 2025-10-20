/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.ResourceBundle;
import oracle.jdbc.driver.Message;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
class Message11
implements Message {
    private static ResourceBundle bundle;
    private static final String messageFile = "oracle.jdbc.driver.Messages";

    @Override
    public String msg(String string, Object object) {
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(messageFile);
            }
            catch (Exception exception) {
                return "Message file 'oracle.jdbc.driver.Messages' is missing.";
            }
        }
        try {
            if (object != null) {
                return bundle.getString(string) + ": " + object;
            }
            return bundle.getString(string);
        }
        catch (Exception exception) {
            return "Message [" + string + "] not found in '" + messageFile + "'.";
        }
    }
}

