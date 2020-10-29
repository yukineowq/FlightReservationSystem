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
public class NonUniqueResultException extends Exception {

    /**
     * Creates a new instance of <code>NonUniqueResultException</code> without
     * detail message.
     */
    public NonUniqueResultException() {
    }

    /**
     * Constructs an instance of <code>NonUniqueResultException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NonUniqueResultException(String msg) {
        super(msg);
    }
}
