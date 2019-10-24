package com.awamo.demoussdclient.state;

/**
 * @author 3y3r
 **/
public interface UssdState {
    void prev( UssdData data );
    void next(UssdData data );
}
