/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightReservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.SeatNumberNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki
 */
@Local
public interface FlightReservationSessionBeanLocal {
    Long createNewFlightReservation(FlightReservation newFlightReservation, Long flightScheduleId) throws FlightReservationNotFoundException, UnknownPersistenceException, InputDataValidationException;
    FlightReservation retrieveFlightReservationBySeatNumber(String seatNumber) throws SeatNumberNotFoundException;
    List<FlightReservation> viewFlightReservations(Long flightScheduleId) throws FlightScheduleNotFoundException;
}
