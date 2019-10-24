package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/
public class NoSelectionSwitch implements UssdState, SelectionSwitch {
    @Override
    public SelectionSwitch nextSelectionSwitch() {
        return this;
    }

    @Override
    public UssdState getUssdState() {
        return this;
    }

    @Override
    public boolean isNext(UssdData data) {
        data.setUssdResponse("END Dear customer, you are not registered to use this service.");
        return true;
    }

    @Override
    public void prev(UssdData data) {

    }

    @Override
    public void next(UssdData data) {

    }
}
