package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class WithdrawState implements UssdState, SelectionSwitch {
    @Override
    public SelectionSwitch nextSelectionSwitch() {
        return new NoSelectionSwitch();
    }

    @Override
    public UssdState getUssdState() {
        return this;
    }

    @Override
    public boolean isNext(UssdData data) {
        String value = data.getValue();
        Optional<String> selectionO = StateUtil.extractRootStateValue(value);
        if (!selectionO.isPresent()) {
            data.setUssdResponse("END No selection data");
            return true;
        }

        if ("2".equals(selectionO.get())) {
            data.setUssdResponse("CON Withdraw-Select account:\n1.4580008991\n2.1580008998");
            return true;
        }

        return false;
    }

    @Override
    public void prev(UssdData data) {

    }

    @Override
    public void next(UssdData data) {

    }
}
