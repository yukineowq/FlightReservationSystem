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
public class AircraftConfigurationNameExistException extends Exception {

    /**
     * Creates a new instance of
     * <code>AircraftConfigurationNameExistException</code> without detail
     * message.
     */
    public AircraftConfigurationNameExistException() {
    }

    /**
     * Constructs an instance of
     * <code>AircraftConfigurationNameExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public AircraftConfigurationNameExistException(String msg) {
        super(msg);
    }
}
