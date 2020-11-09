/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightScheduleContainsReservationException;
import util.exception.FlightSchedulePlanDeleteException;
import util.exception.FlightSchedulePlanDoesNotExistException;
import util.exception.InputDataValidationException;


/**
 *
 * @author Yuki
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {
    Long createNewFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan, Long flightId) throws InputDataValidationException; 
    List<FlightSchedulePlan> viewAllFlightSchedulePlans();
    FlightSchedulePlan viewFlightSchedulePlanDetails(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException;
    void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanDoesNotExistException, FlightScheduleContainsReservationException, InputDataValidationException;
    void deleteFlightSchedulePlan(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException, FlightSchedulePlanDeleteException;
    
}
