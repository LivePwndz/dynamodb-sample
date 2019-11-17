package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class ConfirmPinState implements UssdState {
    @Override
    public void prev(UssdData data) {

    }

    @Override
    public void next(UssdData data) {

        if( validAndSetNewMenu(data)){
            data.setState( new ConfirmPinState());
        } else {
            data.setState( new TerminateUssdState(data));
        }

    }

    private boolean validAndSetNewMenu(UssdData data) {
        String value = data.getValue();

        Optional<String> confirmPinO = StateUtil.extractRootStateValue(value);
        Optional<String> pinO = StateUtil.extractStateValue( value, StateUtil.getStateDepth(value) - 1);

        if(!confirmPinO.isPresent() || !pinO.isPresent()){
            data.setUssdResponse("Invalid PIN data. \n Try again.");
            return false;
        }

        if(!confirmPinO.get().equals(pinO.get())){
            data.setUssdResponse("PIN mismatch. \n Try again.");
            return false;
        }

        data.setUssdResponse("PIN set successful. Thank you!");
        return false;
    }

}
