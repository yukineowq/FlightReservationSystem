/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteDoesNotExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Local
public interface FlightRouteSessionBeanLocal {
     public Long createNewFlightRoute(FlightRoute newFlightRoute, String originAirportCode, String destinationAirportCode) throws FlightRouteNotFoundException, UnknownPersistenceException, AirportNotFoundException;
    FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId, Boolean fetchFlight, Boolean fetchAirport) throws FlightRouteNotFoundException;
    List<FlightRoute> retrieveAllFlightRoutes();
    void deleteFlightRoute(String OD) throws FlightRouteDoesNotExistException;
    FlightRoute retrieveFlightRouteByOD(String OD) throws FlightRouteDoesNotExistException;
}
