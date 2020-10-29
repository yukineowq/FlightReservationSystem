/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author reuben
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long createNewAirport(Airport newAirport){
        entityManager.persist(newAirport);
        entityManager.flush();
        
        return newAirport.getAirportId();
    }
}
