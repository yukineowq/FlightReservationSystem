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
public class FlightNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FlightNotFoundException</code> without
     * detail message.
     */
    public FlightNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlightNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightNotFoundException(String msg) {
        super(msg);
    }
}
