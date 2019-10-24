package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/
public class TerminateUssdState implements UssdState {
    TerminateUssdState(UssdData ussdData) {
        ussdData.setUssdResponse("END "+ussdData.getUssdResponse());
    }

    @Override
    public void prev(UssdData data) {
    }

    @Override
    public void next(UssdData data) {
    }


}
