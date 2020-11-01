/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.UnknownPersistenceException;

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
        
        return query.getResultList();
    }
    
    public Flight retrieveFlightByFlightNumber(Long flightNumber) throws FlightNumberExistException {
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
}
