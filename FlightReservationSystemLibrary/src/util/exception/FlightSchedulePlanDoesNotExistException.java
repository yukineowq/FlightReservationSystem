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
public class FlightSchedulePlanDoesNotExistException extends Exception {

    /**
     * Creates a new instance of
     * <code>FlightSchedulePlanDoesNotExistException</code> without detail
     * message.
     */
    public FlightSchedulePlanDoesNotExistException() {
    }

    /**
     * Constructs an instance of
     * <code>FlightSchedulePlanDoesNotExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FlightSchedulePlanDoesNotExistException(String msg) {
        super(msg);
    }
}
