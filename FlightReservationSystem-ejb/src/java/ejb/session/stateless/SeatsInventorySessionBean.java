/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class SeatsInventorySessionBean implements SeatsInventorySessionBeanRemote, SeatsInventorySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public SeatsInventorySessionBean() {
    }

    @Override
    public List<SeatInventory> viewSeatsInventory(){
        Query query = entityManager.createQuery("SELECT s FROM SeatInventory s");
        List<SeatInventory> seatInventories = query.getResultList();
        seatInventories.size();
        return seatInventories;
    }
    
}
