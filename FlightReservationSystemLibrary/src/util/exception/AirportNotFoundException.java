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
public class AirportNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AirportNotFoundException</code> without
     * detail message.
     */
    public AirportNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AirportNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AirportNotFoundException(String msg) {
        super(msg);
    }
}
