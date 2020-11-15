/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
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
 * @author Reuben Ang Wen Zheng
 */
@Local
public interface FlightReservationSessionBeanLocal {
    Long createNewFlightReservation(FlightReservation newFlightReservation) throws FlightReservationNotFoundException, UnknownPersistenceException, InputDataValidationException;
    FlightReservation retrieveFlightReservationBySeatNumber(String seatNumber) throws SeatNumberNotFoundException;
    List<FlightReservation> viewFlightReservations(Long customerId);
    List<FlightReservation> viewFlightReservationDetails(Long customerId, Long reservationId);
}
