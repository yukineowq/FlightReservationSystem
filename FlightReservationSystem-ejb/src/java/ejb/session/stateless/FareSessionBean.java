/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import entity.Fare;
import entity.FlightSchedulePlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author reuben
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FareSessionBean() {
    }

    @Override
    public Long createNewFare(Fare newFare, Long cabinClassConfigurationId, Long flightSchedulePlanId) {
        CabinClassConfiguration cabinClassConfiguration  = entityManager.find(CabinClassConfiguration.class, cabinClassConfigurationId);
        if (cabinClassConfiguration != null) {
            newFare.setCabinClassConfiguration(cabinClassConfiguration);
            cabinClassConfiguration.setFare(newFare);
        }
        FlightSchedulePlan flightSchedulePlan = entityManager.find(FlightSchedulePlan.class, flightSchedulePlanId);
        if (flightSchedulePlan != null) {
            flightSchedulePlan.getFares().add(newFare);
            newFare.setFlightSchedulePlan(flightSchedulePlan);
        }
        entityManager.persist(newFare);
        entityManager.flush();
        
        return newFare.getFareId();
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }
}
