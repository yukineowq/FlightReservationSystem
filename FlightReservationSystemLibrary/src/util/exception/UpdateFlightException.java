/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Yuki Neo Wei Qian
 */
public class UpdateFlightException extends Exception {

    /**
     * Creates a new instance of <code>UpdateFlightException</code> without
     * detail message.
     */
    public UpdateFlightException() {
    }

    /**
     * Constructs an instance of <code>UpdateFlightException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFlightException(String msg) {
        super(msg);
    }
}
