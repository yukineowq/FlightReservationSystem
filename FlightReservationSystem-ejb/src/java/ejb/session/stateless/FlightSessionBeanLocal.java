/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNumberExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki
 */
@Local
public interface FlightSessionBeanLocal {
    Long createNewFlight(Flight newFlight) throws FlightNumberExistException, UnknownPersistenceException;
    List<Flight> retrieveAllFlights();
    Flight retrieveFlightByFlightNumber(Long flightNumber) throws FlightNumberExistException;
}
