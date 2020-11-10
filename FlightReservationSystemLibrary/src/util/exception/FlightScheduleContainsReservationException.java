/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
public class FlightScheduleContainsReservationException extends Exception {

    /**
     * Creates a new instance of
     * <code>FlightScheduleContainsReservationException</code> without detail
     * message.
     */
    public FlightScheduleContainsReservationException() {
    }

    /**
     * Constructs an instance of
     * <code>FlightScheduleContainsReservationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightScheduleContainsReservationException(String msg) {
        super(msg);
    }
}
