/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightScheduleContainsReservationException;
import util.exception.FlightSchedulePlanDoesNotExistException;

/**
 *
 * @author Yuki
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public Long createNewFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Long flightId) {
        Flight flight = entityManager.find(Flight.class, flightId);
        if (flight != null) {
            flight.getFlightSchedulePlan().add(newFlightSchedulePlan);
            newFlightSchedulePlan.setFlight(flight);
        }
        entityManager.persist(newFlightSchedulePlan);
        entityManager.flush();

        return newFlightSchedulePlan.getFlightSchedulePlanId();
    }

    @Override
    public List<FlightSchedulePlan> viewAllFlightSchedulePlans() {
        Query query = entityManager.createQuery("SELECT fl FROM FlightSchedulePlan fl");
        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();
        flightSchedulePlans.size();
        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
            flightSchedulePlan.getFlightSchedules().size();
            flightSchedulePlan.getFlight();
            flightSchedulePlan.getFares().size();
        }
        return flightSchedulePlans;
    }

    @Override
    public FlightSchedulePlan viewFlightSchedulePlanDetails(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException {
        FlightSchedulePlan flightSchedulePlan = entityManager.find(FlightSchedulePlan.class, flightSchedulePlanId);
        flightSchedulePlan.getFares().size();
        flightSchedulePlan.getFlightSchedules().size();
        return flightSchedulePlan;
    }

    @Override
    public void updateFlightSchedulePlan(FlightSchedulePlan updatedFlightSchedulePlan, Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException, FlightScheduleContainsReservationException {
        if (updatedFlightSchedulePlan != null && updatedFlightSchedulePlan.getFlightSchedulePlanId() != null) {
            FlightSchedulePlan flightSchedulePlanToUpdate = new FlightSchedulePlan();
            try {
                flightSchedulePlanToUpdate = viewFlightSchedulePlanDetails(flightSchedulePlanId);
            } catch (Exception ex) {
                throw new FlightSchedulePlanDoesNotExistException();
            }

            List<FlightSchedule> originalFlightSchedules = flightSchedulePlanToUpdate.getFlightSchedules();
            List<FlightSchedule> updatedFlightSchedules = updatedFlightSchedulePlan.getFlightSchedules();

            for (FlightSchedule flightSchedule : originalFlightSchedules) {
                if (!updatedFlightSchedules.contains(flightSchedule)) {
                    if (!flightSchedule.getFlightReservations().isEmpty()) {
                        throw new FlightScheduleContainsReservationException();
                    }
                }
            }

            flightSchedulePlanToUpdate.setFares(updatedFlightSchedulePlan.getFares());
            flightSchedulePlanToUpdate.setFlightSchedules(updatedFlightSchedulePlan.getFlightSchedules());
        } else {
            throw new FlightSchedulePlanDoesNotExistException();
        }
    }
    
    @Override
    public void deleteFlightSchedulePlan(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException {
        
    }

}
