package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/
public class ActivationState implements UssdState, SelectionSwitch {
    @Override
    public void prev(UssdData data) {

    }


    @Override
    public void next(UssdData data) {
        if(valid(data)) {
            data.setState(new SetPinState());
        } else {
            data.setState(new TerminateUssdState(data));
        }
    }


    private boolean valid( UssdData ussdData ) {
        String value = ussdData.getValue();
        if( value == null || value.trim().isEmpty() || !value.equals("1234") ){
            ussdData.setUssdResponse("Invalid activation OTP. \nPlease try again.");
            return false;
        }

        ussdData.setUssdResponse( "CON Set PIN");
        return true;
    }

    @Override
    public SelectionSwitch nextSelectionSwitch() {
        return new TenantListState();
    }

    @Override
    public UssdState getUssdState() {
        return this;
    }

    @Override
    public boolean isNext(UssdData data) {
        boolean isDataValid = "+256777915728".equals(data.getMsisdn());
        if( isDataValid ){
            data.setUssdResponse( "CON Enter OTP to activate.");
        }

        return isDataValid;
    }
}
