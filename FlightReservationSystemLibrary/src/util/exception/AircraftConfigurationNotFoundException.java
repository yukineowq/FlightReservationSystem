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
public class AircraftConfigurationNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>AircraftConfigurationNotFoundException</code> without detail
     * message.
     */
    public AircraftConfigurationNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>AircraftConfigurationNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public AircraftConfigurationNotFoundException(String msg) {
        super(msg);
    }
}
