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
public class FlightRouteAlreadyExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteAlreadyExistException</code>
     * without detail message.
     */
    public FlightRouteAlreadyExistException() {
    }

    /**
     * Constructs an instance of <code>FlightRouteAlreadyExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteAlreadyExistException(String msg) {
        super(msg);
    }
}
