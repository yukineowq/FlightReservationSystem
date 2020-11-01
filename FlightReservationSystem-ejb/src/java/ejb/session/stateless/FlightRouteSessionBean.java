/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightRouteOriginExistException;
import util.exception.FlightRouteReturnExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    @Override
    public Long createNewFlightRoute(FlightRoute newFlightRoute) throws FlightRouteNotFoundException, UnknownPersistenceException {
        try
        {
            entityManager.persist(newFlightRoute);
            entityManager.flush();
            newFlightRoute.setStatus(StatusEnum.ENABLED);
            
            return newFlightRoute.getFlightRouteId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new FlightRouteNotFoundException();
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
    public FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId, Boolean fetchFlight, Boolean fetchAirport) throws FlightRouteNotFoundException {
        FlightRoute flightRoute = entityManager.find(FlightRoute.class, flightRouteId);
        
        if (fetchFlight) {
            flightRoute.getFlight();
        }
        
        if (fetchAirport) {
            flightRoute.getAirport();
        }
        
        return flightRoute;
    }
    
    @Override
    public List<FlightRoute> retrieveAllFlightRoutes() {
        Query query = entityManager.createQuery("SELECT fr FROM FlightRoute fr");
        
        return query.getResultList();
    }
    
    @Override
    public FlightRoute retrieveFlightRouteByOriginAirport(String origin) throws FlightRouteOriginExistException {
        Query query = entityManager.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.origin = :inOrigin");
        query.setParameter("inOrigin", origin);
        
        try 
        {
            return (FlightRoute)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightRouteOriginExistException("Flight route embarking from " + origin + " does not exist!");
        }
    }
    
    @Override
    public FlightRoute retrieveFlightRouteByDestinationAirport(String destination) throws FlightRouteReturnExistException {
        Query query = entityManager.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.destination = :inDestination");
        query.setParameter("inDestination", destination);
        
        try
        {
            return (FlightRoute)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightRouteReturnExistException("Flight route returninf from " + destination + " does not exist!");
        }
    }
    
    @Override
    public void deleteFlightRoute(Long flightRouteId) throws FlightRouteNotFoundException {
        FlightRoute flightRoute = retrieveFlightRouteByFlightRouteId(flightRouteId, false, false);
        
        flightRoute.getFlight().setFlightRoute(null);
        
        flightRoute.getAirport().removeFlightRouteOrigin(flightRoute);
        flightRoute.getAirport().removeFlightRouteDestination(flightRoute);
        
        flightRoute.setStatus(StatusEnum.DISABLED);
        entityManager.remove(flightRoute);
    }
}
