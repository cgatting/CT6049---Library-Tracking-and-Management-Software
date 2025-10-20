/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import oracle.net.ano.AuthenticationService;

class AuthenticationService$1
extends Configuration {
    private /* synthetic */ AuthenticationService A;

    AuthenticationService$1(AuthenticationService authenticationService) {
        this.A = authenticationService;
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String object) {
        object = new HashMap<String, String>();
        ((HashMap)object).put("useTicketCache", "true");
        ((HashMap)object).put("doNotPrompt", "true");
        String string = (String)this.A.sAtts.profile.get("oracle.net.kerberos5_cc_name");
        if (string != null && !string.isEmpty()) {
            ((HashMap)object).put("ticketCache", string);
        }
        return new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, (Map<String, ?>)object)};
    }
}

