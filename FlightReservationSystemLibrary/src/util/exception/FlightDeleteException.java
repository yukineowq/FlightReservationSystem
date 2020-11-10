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
public class FlightDeleteException extends Exception {

    /**
     * Creates a new instance of <code>FlightDeleteException</code> without
     * detail message.
     */
    public FlightDeleteException() {
    }

    /**
     * Constructs an instance of <code>FlightDeleteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightDeleteException(String msg) {
        super(msg);
    }
}
