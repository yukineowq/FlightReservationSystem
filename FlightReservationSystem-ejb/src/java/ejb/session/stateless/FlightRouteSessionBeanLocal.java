/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightRouteOriginExistException;
import util.exception.FlightRouteReturnExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki
 */
@Local
public interface FlightRouteSessionBeanLocal {
    public Long createNewFlightRoute(FlightRoute flightRoute) throws FlightRouteNotFoundException, UnknownPersistenceException;
    FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId, Boolean fetchFlight, Boolean fetchAirport) throws FlightRouteNotFoundException;
    List<FlightRoute> retrieveAllFlightRoutes();
    FlightRoute retrieveFlightRouteByOriginAirport(String origin) throws FlightRouteOriginExistException;
    FlightRoute retrieveFlightRouteByDestinationAirport(String destination) throws FlightRouteReturnExistException;
    void deleteFlightRoute(Long flightRouteId) throws FlightRouteNotFoundException;
}
