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
public class AircraftTypeMaxSeatCapacityExceededException extends Exception {

    /**
     * Creates a new instance of
     * <code>AircraftTypeMaxSeatCapacityExceededException</code> without detail
     * message.
     */
    public AircraftTypeMaxSeatCapacityExceededException() {
    }

    /**
     * Constructs an instance of
     * <code>AircraftTypeMaxSeatCapacityExceededException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AircraftTypeMaxSeatCapacityExceededException(String msg) {
        super(msg);
    }
}
