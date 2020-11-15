/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CabinClassEnum;
import util.enumeration.PreferenceEnum;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Local
public interface FlightScheduleSessionBeanLocal {

    public Long createNewFlightSchedule(FlightSchedule newFlightSchedule, Long flightSchedulePlanId) throws FlightScheduleNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public FlightSchedule retrieveFlightScheduleById(Long fspId);

    public List<List<FlightSchedule>> searchFlightSchedule(Airport origin, Airport destination, String departureDate, int numPassenger, PreferenceEnum preferenceEnum, CabinClassEnum cabinClassEnum);
}
