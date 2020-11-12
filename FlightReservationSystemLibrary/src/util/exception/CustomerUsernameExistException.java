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
public class CustomerUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>CustomerUsernameExistException</code>
     * without detail message.
     */
    public CustomerUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>CustomerUsernameExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerUsernameExistException(String msg) {
        super(msg);
    }
}
