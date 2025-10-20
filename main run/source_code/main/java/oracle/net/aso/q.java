/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.util.Random;

public final class q {
    private int cd = 971;
    private int ce = 11113;
    private int cf = 104322;
    private int cg = 4181;
    private boolean initialized = false;

    public final short ab() {
        if (!this.initialized) {
            q q2 = this;
            Random random = new Random();
            q2.cg = random.nextInt();
            q2.initialized = true;
        } else {
            this.cg += 7;
            this.ce += 1907;
            this.cf += 73939;
            if (this.cg >= 9973) {
                this.cg -= 9871;
            }
            if (this.ce >= 99991) {
                this.ce -= 89989;
            }
            if (this.cf >= 224729) {
                this.cf -= 96233;
            }
            this.cg = this.cg * this.cd + this.ce + this.cf;
        }
        return (short)(this.cg >> 16 ^ this.cg & 0xFFFF);
    }
}

