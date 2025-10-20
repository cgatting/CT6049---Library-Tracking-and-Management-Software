/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.internal.JMSConsumerExceptionEvent;

public class ConsumerExceptionEvent
extends JMSConsumerExceptionEvent {
    public ConsumerExceptionEvent(int n2, String string) {
        super(n2, string);
    }

    public ConsumerExceptionEvent() {
    }

    @Override
    public int getType() {
        return this.type;
    }

    public void setType(int n2) {
        this.type = n2;
    }

    @Override
    public String getCause() {
        return this.cause;
    }

    public void setCause(String string) {
        this.cause = string;
    }
}

