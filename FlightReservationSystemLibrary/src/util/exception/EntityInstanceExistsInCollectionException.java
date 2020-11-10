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
public class EntityInstanceExistsInCollectionException extends Exception {

    /**
     * Creates a new instance of
     * <code>EntityInstanceExistsInCollectionException</code> without detail
     * message.
     */
    public EntityInstanceExistsInCollectionException() {
    }

    /**
     * Constructs an instance of
     * <code>EntityInstanceExistsInCollectionException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public EntityInstanceExistsInCollectionException(String msg) {
        super(msg);
    }
}
