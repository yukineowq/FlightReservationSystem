/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatInventory;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UpdateSeatInventoryException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Local
public interface SeatsInventorySessionBeanLocal {
    public List<SeatInventory> viewSeatsInventory();
    public void updateSeatInventory(SeatInventory seatInventory) throws SeatInventoryNotFoundException, UpdateSeatInventoryException, InputDataValidationException;
}
