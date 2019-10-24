package com.awamo.demoussdclient.state;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author 3y3r
 **/

@Component
@Scope( scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS )
public class UssdData {
    private UssdState state = new InitUssdState();
    private String msisdn;
    private String ussdResponse;
    private String value;

    public UssdState getState() {
        return state;
    }

    public void setState(UssdState state) {
        this.state = state;
    }

    public void prevState(){
        state.prev(this);
    }

    public void nextState(){
        state.next(this);
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setUssdResponse(String ussdResponse) {
        this.ussdResponse = ussdResponse;
    }

    public String getUssdResponse() {
        return ussdResponse;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
