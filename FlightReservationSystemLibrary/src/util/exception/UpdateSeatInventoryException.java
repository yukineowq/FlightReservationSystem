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
public class UpdateSeatInventoryException extends Exception {

    /**
     * Creates a new instance of <code>UpdateSeatInventoryException</code>
     * without detail message.
     */
    public UpdateSeatInventoryException() {
    }

    /**
     * Constructs an instance of <code>UpdateSeatInventoryException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateSeatInventoryException(String msg) {
        super(msg);
    }
}
