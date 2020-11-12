/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightDeleteException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Local
public interface FlightSessionBeanLocal {
    Long createNewFlight(Flight newFlight) throws FlightNumberExistException, UnknownPersistenceException, InputDataValidationException;
    List<Flight> retrieveAllFlights();
    Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNumberExistException;
    void updateFlight(Flight flight) throws FlightNotFoundException, UpdateFlightException,InputDataValidationException ;
    void deleteFlight(String flightNumber) throws FlightNumberExistException, FlightDeleteException;
}
