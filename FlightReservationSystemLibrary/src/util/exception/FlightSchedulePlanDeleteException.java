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
public class FlightSchedulePlanDeleteException extends Exception {

    /**
     * Creates a new instance of <code>FlightSchedulePlanDeleteException</code>
     * without detail message.
     */
    public FlightSchedulePlanDeleteException() {
    }

    /**
     * Constructs an instance of <code>FlightSchedulePlanDeleteException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightSchedulePlanDeleteException(String msg) {
        super(msg);
    }
}
