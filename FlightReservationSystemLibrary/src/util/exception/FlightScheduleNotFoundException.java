/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author reuben
 */
public class FlightScheduleNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FlightScheduleNotFoundException</code>
     * without detail message.
     */
    public FlightScheduleNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlightScheduleNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightScheduleNotFoundException(String msg) {
        super(msg);
    }
}
