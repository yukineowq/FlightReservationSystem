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
public class SeatInventoryNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>SeatInventoryNotFoundException</code>
     * without detail message.
     */
    public SeatInventoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>SeatInventoryNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatInventoryNotFoundException(String msg) {
        super(msg);
    }
}
