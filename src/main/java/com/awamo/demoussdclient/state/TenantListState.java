package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class TenantListState implements UssdState, SelectionSwitch {
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
        boolean isDataValid = "+256777915729".equals(data.getMsisdn());
        if( isDataValid ){
            data.setUssdResponse("CON Welcome! Please select tenant \n1. North Ankole \n2. PE Integra");
        }
        System.out.println("Is next in tenant list state: "+isDataValid);

        return isDataValid;
    }


    @Override
    public void prev(UssdData data) {

    }

    @Override
    public void next(UssdData data) {
        if( valid( data )) {
            data.setState( new ServicesListState() );
        } else {
            data.setState(new TerminateUssdState(data));
        }
    }

    private boolean valid(UssdData data) {
        String value = data.getValue();
        Optional< String > tenantSelectionO = StateUtil.extractRootStateValue(value);
        if( !tenantSelectionO.isPresent()){
            data.setUssdResponse("No tenant selection data.");
            return false;
        }

        String tenantSelection = tenantSelectionO.get();
        int tenantSelectionNo = Integer.valueOf(tenantSelection);
        if( tenantSelectionNo < 1 || tenantSelectionNo > 3 ){
            data.setUssdResponse("Invalid section. \nTry again.");
            return false;
        }

        data.setUssdResponse("CON Enter PIN");
        return true;

    }
}
