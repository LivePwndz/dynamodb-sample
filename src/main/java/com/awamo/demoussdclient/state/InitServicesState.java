package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/

public class InitServicesState implements UssdState {

    private SelectionSwitch selectionSwitch = new DepositState();

    @Override
    public void prev(UssdData ussdData) {

    }

    @Override
    public void next(UssdData ussdData) {
        ussdData.setState(getNextUssdState(ussdData));
    }

    private UssdState getNextUssdState(UssdData data) {
        if (!selectionSwitch.isNext(data)) {
            selectionSwitch = selectionSwitch.nextSelectionSwitch();
            return getNextUssdState(data);
        }

        return selectionSwitch.getUssdState();
    }

}
