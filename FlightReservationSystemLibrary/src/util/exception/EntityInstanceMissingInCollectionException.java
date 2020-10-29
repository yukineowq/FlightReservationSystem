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
public class EntityInstanceMissingInCollectionException extends Exception {

    /**
     * Creates a new instance of
     * <code>EntityInstanceMissingInCollectionException</code> without detail
     * message.
     */
    public EntityInstanceMissingInCollectionException() {
    }

    /**
     * Constructs an instance of
     * <code>EntityInstanceMissingInCollectionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EntityInstanceMissingInCollectionException(String msg) {
        super(msg);
    }
}
