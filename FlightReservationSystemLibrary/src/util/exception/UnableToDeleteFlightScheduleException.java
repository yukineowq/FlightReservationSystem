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
public class UnableToDeleteFlightScheduleException extends Exception {

    /**
     * Creates a new instance of
     * <code>UnableToDeleteFlightScheduleException</code> without detail
     * message.
     */
    public UnableToDeleteFlightScheduleException() {
    }

    /**
     * Constructs an instance of
     * <code>UnableToDeleteFlightScheduleException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public UnableToDeleteFlightScheduleException(String msg) {
        super(msg);
    }
}
