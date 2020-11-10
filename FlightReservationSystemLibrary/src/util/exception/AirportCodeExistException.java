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
public class AirportCodeExistException extends Exception {

    /**
     * Creates a new instance of <code>AirportCodeExistException</code> without
     * detail message.
     */
    public AirportCodeExistException() {
    }

    /**
     * Constructs an instance of <code>AirportCodeExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AirportCodeExistException(String msg) {
        super(msg);
    }
}
