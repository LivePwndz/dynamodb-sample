package com.awamo.demoussdclient.controller;

import com.awamo.demoussdclient.state.UssdData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 3y3r
 **/

@RestController
@RequestMapping(path = "/awamo")
public class UssdController {

    private final UssdData ussdData;

    public UssdController(UssdData ussdData) {
        this.ussdData = ussdData;
    }

    @PostMapping(path = "/ussd")
    public String handler( @RequestBody String body, @RequestParam(name = "phoneNumber") String msisdn, @RequestParam(name = "text") String value ) {
        System.out.println("Ussd req. body: "+body);
        System.out.println("Msisdn: "+msisdn);
        System.out.println("Value: "+value);

        ussdData.setMsisdn( msisdn );
        ussdData.setValue(value);
        ussdData.nextState();
        return ussdData.getUssdResponse();
    }

}
