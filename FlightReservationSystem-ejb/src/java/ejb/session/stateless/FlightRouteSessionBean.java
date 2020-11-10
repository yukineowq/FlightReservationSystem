/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import util.exception.AirportNotFoundException;
import util.exception.EntityInstanceMissingInCollectionException;
import util.exception.FlightRouteDoesNotExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightRouteOriginExistException;
import util.exception.FlightRouteReturnExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public FlightRouteSessionBean() {
    }
    
    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;

    @Override
    public Long createNewFlightRoute(FlightRoute newFlightRoute, String originAirportCode, String destinationAirportCode) throws FlightRouteNotFoundException, UnknownPersistenceException, AirportNotFoundException {
        Airport originAirport = airportSessionBeanLocal.retrieveAirportByAirportCode(originAirportCode);
        Airport destinationAirport = airportSessionBeanLocal.retrieveAirportByAirportCode(destinationAirportCode);
        if (originAirport != null && destinationAirport != null) {
            newFlightRoute.setDestination(destinationAirport);
            newFlightRoute.setOrigin(originAirport);
            originAirport.getFlightRouteDestinations().add(newFlightRoute);
            destinationAirport.getFlightRouteOrigins().add(newFlightRoute);
        }
        try {
            entityManager.persist(newFlightRoute);
            entityManager.flush();
            newFlightRoute.setStatus(StatusEnum.ENABLED);

            return newFlightRoute.getFlightRouteId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new FlightRouteNotFoundException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId, Boolean fetchFlight, Boolean fetchAirport) throws FlightRouteNotFoundException {
        FlightRoute flightRoute = entityManager.find(FlightRoute.class, flightRouteId);

        if (fetchFlight) {
            flightRoute.getFlights().size();
        }

        if (fetchAirport) {
            flightRoute.getOrigin();
            flightRoute.getDestination();
        }

        return flightRoute;
    }

    @Override
    public List<FlightRoute> retrieveAllFlightRoutes() {
        Query query = entityManager.createQuery("SELECT fr FROM FlightRoute fr");
        List<FlightRoute> flightRoutes = query.getResultList();
        flightRoutes.size();
        return flightRoutes;
    }

    @Override
    public FlightRoute retrieveFlightRouteByOD(String OD) throws FlightRouteDoesNotExistException {
        Query query = entityManager.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.OD = :inOD");
        query.setParameter("inOD", OD);

        try {
            FlightRoute flightRoute = (FlightRoute) query.getSingleResult();
            flightRoute.getFlights().size();
            return  flightRoute;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightRouteDoesNotExistException("Flight route  " + OD + " does not exist!");
        }
    }
    
    public void deleteFlightRoute(String OD) throws FlightRouteDoesNotExistException {
        FlightRoute flightRoute = retrieveFlightRouteByOD(OD);
        List<Flight> flights = flightRoute.getFlights();
        if (!flights.isEmpty()) {
            entityManager.remove(flightRoute);
        } else {
            System.out.println("Flight route is currently in use. Flight route set to disabled instead.");
            flightRoute.setStatus(StatusEnum.DISABLED);
        }
    }
}
