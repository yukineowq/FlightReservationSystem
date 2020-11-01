/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;

/**
 *
 * @author Yuki
 */
@Remote
public interface FlightSessionBeanRemote {
    Long createNewFlight(Flight newFlight) throws FlightNumberExistException, UnknownPersistenceException;
    List<Flight> retrieveAllFlights();
    Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNumberExistException;
    void updateFlight(Flight updatedFlight) throws FlightNotFoundException, UpdateFlightException;
}
