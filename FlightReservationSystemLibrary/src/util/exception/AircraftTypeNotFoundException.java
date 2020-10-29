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
public class AircraftTypeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AircraftTypeNotFoundException</code>
     * without detail message.
     */
    public AircraftTypeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AircraftTypeNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AircraftTypeNotFoundException(String msg) {
        super(msg);
    }
}
