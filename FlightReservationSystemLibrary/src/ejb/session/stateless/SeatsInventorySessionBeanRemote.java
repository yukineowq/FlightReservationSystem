/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SeatInventory;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UpdateSeatInventoryException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Remote
public interface SeatsInventorySessionBeanRemote {
    public List<SeatInventory> viewSeatsInventory();
    public void updateSeatInventory(SeatInventory seatInventory) throws SeatInventoryNotFoundException, UpdateSeatInventoryException, InputDataValidationException;
}
