/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AirportCodeExistException;
import util.exception.AirportNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long createNewAirport(Airport newAirport) throws AirportCodeExistException, UnknownPersistenceException{
         try
        {
            entityManager.persist(newAirport);
            entityManager.flush();

            return newAirport.getAirportId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new AirportCodeExistException();
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
    
    @Override
    public Airport retrieveAirportByAirportCode(String airportCode) throws AirportNotFoundException {
        Query query = entityManager.createQuery("SELECT e FROM Airport e WHERE e.airportCode = :inAirportcode");
        query.setParameter("inAirportcode", airportCode);
        
        try
        {
            Airport airport = (Airport)query.getSingleResult();
            airport.getFlightRouteDestinations().size();
            airport.getFlightRouteOrigins().size();
            return airport;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AirportNotFoundException("Airport code " + airportCode + " does not exist!");
        }
    }
}
