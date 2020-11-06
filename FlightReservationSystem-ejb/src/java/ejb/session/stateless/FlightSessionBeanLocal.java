/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;

/**
 *
 * @author Yuki
 */
@Local
public interface FlightSessionBeanLocal {
    Long createNewFlight(Flight newFlight, Long aircraftConfigurationId, Long flightRouteId) throws FlightNumberExistException, UnknownPersistenceException;
    List<Flight> retrieveAllFlights();
    Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNumberExistException;
    void updateFlight(Flight updatedFlight) throws FlightNotFoundException, UpdateFlightException;
    void deleteFlight(String flightNumber) throws FlightNumberExistException;
}
