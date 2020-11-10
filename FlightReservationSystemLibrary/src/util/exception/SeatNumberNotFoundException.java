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
public class SeatNumberNotFoundException extends Exception {

    public SeatNumberNotFoundException() {
    }

    public SeatNumberNotFoundException(String msg) {
        super(msg);
    }
}
