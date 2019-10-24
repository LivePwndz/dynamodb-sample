package com.awamo.demoussdclient.state;

import java.util.Optional;

/**
 * @author 3y3r
 **/
public class StateUtil {

    private static String[] tokenizeStepValues( String value ){
        // TODO Remove navigation values
        return value.split("\\*");
    }

    public static int getStateDepth(String value){
        return tokenizeStepValues(value).length;
    }

    public static Optional< String > extractStepValue( String value, int depth ) {
        String[] tokens = tokenizeStepValues(value);
        if( tokens.length < 1 || depth > tokens.length ){
            return Optional.empty();
        }
        return Optional.of(tokens[depth - 1]);
    }

    public static Optional< String > extractRootStateValue( String value ){
        return extractStepValue(value, getStateDepth(value));
    }


}
