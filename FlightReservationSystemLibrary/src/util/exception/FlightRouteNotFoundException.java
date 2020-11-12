/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Yuki Neo Wei Qian
 */
public class FlightRouteNotFoundException extends Exception {

    public FlightRouteNotFoundException() {
    }

    public FlightRouteNotFoundException(String msg) {
        super(msg);
    }
}
