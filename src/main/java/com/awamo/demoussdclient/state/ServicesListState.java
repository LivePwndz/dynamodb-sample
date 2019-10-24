package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class ServicesListState implements UssdState {

    @Override
    public void prev(UssdData data) {

    }

    @Override
    public void next(UssdData data) {
        if( valid( data )){
            data.setState( new InitServicesState());
        }else{
            data.setState(new TerminateUssdState(data));
        }

    }


    private boolean valid(UssdData data) {
        String value = data.getValue();
        boolean isPinValid = validatePIN(value);
        System.out.println("Is PIN valid: "+isPinValid);
        if(!isPinValid){
            data.setUssdResponse("PIN not correct. \nPlease try again.");
            return false;
        }

        data.setUssdResponse("CON Select Service:\n1. Deposit\n2. Withdraw\n3.Loan Repayment\n4. My Account");
        return true;
    }

    private boolean validatePIN(String value) {
        Optional< String > pinO = StateUtil.extractRootStateValue(value);
        if( !pinO.isPresent() ){
            return false;
        }

        String pin = pinO.get().trim();
        return "1232".equals(pinO.get().trim());
    }
}
