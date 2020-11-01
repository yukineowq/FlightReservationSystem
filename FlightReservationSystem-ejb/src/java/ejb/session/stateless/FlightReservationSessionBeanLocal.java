/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightReservation;
import javax.ejb.Local;
import util.exception.FlightReservationNotFoundException;
import util.exception.SeatNumberNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki
 */
@Local
public interface FlightReservationSessionBeanLocal {
    Long createNewFlightReservation(FlightReservation newFlightReservation) throws FlightReservationNotFoundException, UnknownPersistenceException;
    FlightReservation retrieveFlightReservationBySeatNumber(String seatNumber) throws SeatNumberNotFoundException;
    
}
