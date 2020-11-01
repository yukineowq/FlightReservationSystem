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
public class FlightRouteDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteDoesNotExistException</code>
     * without detail message.
     */
    public FlightRouteDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>FlightRouteDoesNotExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteDoesNotExistException(String msg) {
        super(msg);
    }
}
