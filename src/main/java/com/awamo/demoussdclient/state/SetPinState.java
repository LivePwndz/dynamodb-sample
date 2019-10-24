package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class SetPinState implements UssdState {
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
        Optional<String> pinO = extractPin( value );
        if(!pinO.isPresent() || (pinO.get().trim().length() != 4)){
            data.setUssdResponse("Invalid PIN format. \n Try again.");
            return false;
        }

        data.setUssdResponse("CON Confirm New PIN");
        return true;
    }

    private Optional< String > extractPin(String value) {
        if( value == null ){
            return Optional.empty();
        }

        String[] stepValues = value.split("\\*");
        return Optional.of(stepValues[stepValues.length - 1]);
    }
}
