package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/

public class InitUssdState implements UssdState {

    private SelectionSwitch selectionSwitch = new ActivationState();

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
