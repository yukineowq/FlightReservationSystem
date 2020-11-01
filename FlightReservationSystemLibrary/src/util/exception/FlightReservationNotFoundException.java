/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Yuki
 */
public class FlightReservationNotFoundException extends Exception {

    public FlightReservationNotFoundException() {
    }

    public FlightReservationNotFoundException(String msg) {
        super(msg);
    }
}
