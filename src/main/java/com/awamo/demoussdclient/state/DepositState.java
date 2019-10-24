package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class DepositState implements UssdState, SelectionSwitch {
    @Override
    public SelectionSwitch nextSelectionSwitch() {
        return new WithdrawState();
    }

    @Override
    public UssdState getUssdState() {
        return this;
    }

    @Override
    public boolean isNext(UssdData data) {
        String value = data.getValue();
        Optional<String> selectionO = StateUtil.extractRootStateValue(value);
        if(!selectionO.isPresent()){
            data.setUssdResponse("END No selection data");
            return true;
        }

        if( "1".equals(selectionO.get())){
            data.setUssdResponse("CON Deposit-Select account:\n1.4580008991\n2.1580008998");
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
