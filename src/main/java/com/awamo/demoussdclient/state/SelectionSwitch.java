package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/
public interface SelectionSwitch {
    SelectionSwitch nextSelectionSwitch();
    UssdState getUssdState();
    boolean isNext( UssdData data );
}
