package com.procergs.rsp.user.ed;

import javax.ws.rs.FormParam;

/**
 * Created by daniel on 31/05/16.
 */
public class LoginED {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
