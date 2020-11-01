/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;

/**
 *
 * @author Yuki
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    public Long createNewFlight(Flight newFlight) throws FlightNumberExistException, UnknownPersistenceException {
    
        try 
        {
          entityManager.persist(newFlight);
          entityManager.flush();
        
          return newFlight.getFlightId();  
        }
                catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new FlightNumberExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    public List<Flight> retrieveAllFlights() {
        Query query = entityManager.createQuery("SELECT fl FROM Flight fl");
        List<Flight> flights = query.getResultList();
        flights.size();
        return flights;
    }
    
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNumberExistException {
        Query query = entityManager.createQuery("SELECT fl FROM Flight fl WHERE fl.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);
        
        try
        {
            return (Flight)query.getResultList();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightNumberExistException("Flight number:" + flightNumber + " does not exist!");
        }
    }
    
    @Override
    public void updateFlight(Flight updatedFlight) throws FlightNotFoundException, UpdateFlightException {
        if(updatedFlight != null && updatedFlight.getFlightId()!= null)
        {
            Flight flightToUpdate = new Flight();
            try {
                flightToUpdate = retrieveFlightByFlightNumber(updatedFlight.getFlightNumber());
            } catch (FlightNumberExistException ex) {
                Logger.getLogger(FlightSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(flightToUpdate.getFlightNumber().equals(updatedFlight.getFlightNumber()))
            {
                flightToUpdate.setFlightNumber(updatedFlight.getFlightNumber());
                flightToUpdate.setAircraftConfiguration(updatedFlight.getAircraftConfiguration());
                flightToUpdate.setStatus(updatedFlight.getStatus());
                flightToUpdate.setReturnFlight(updatedFlight.getReturnFlight());
                flightToUpdate.setFlightSchedulePlan(updatedFlight.getFlightSchedulePlan());
                flightToUpdate.setFlightRoute(updatedFlight.getFlightRoute());
            }
            else
            {
                throw new UpdateFlightException("Flight number of flight record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new FlightNotFoundException("Flight ID not provided for flight to be updated");
        }
    }
    
    public void deleteFlight(String flightNumber) throws FlightNumberExistException {
        Flight flight = retrieveFlightByFlightNumber(flightNumber);
        List<FlightSchedulePlan> flightSchedulePlan = flight.getFlightSchedulePlan();
        FlightRoute flightRoute = flight.getFlightRoute();
        AircraftConfiguration aircraftConfiguration = flight.getAircraftConfiguration();
        if (flightSchedulePlan.isEmpty() && flightRoute == null && aircraftConfiguration == null) {
            entityManager.remove(flightRoute);
        } else {
            flight.setStatus(StatusEnum.DISABLED);
        }
    }
}
