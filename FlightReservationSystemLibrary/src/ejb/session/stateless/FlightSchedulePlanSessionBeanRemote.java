/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightScheduleContainsReservationException;
import util.exception.FlightSchedulePlanDeleteException;
import util.exception.FlightSchedulePlanDoesNotExistException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {
    Long createNewFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan) throws InputDataValidationException;
    List<FlightSchedulePlan> viewAllFlightSchedulePlans();
    FlightSchedulePlan viewFlightSchedulePlanDetails(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException;
    void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanDoesNotExistException, FlightScheduleContainsReservationException, InputDataValidationException;
    void deleteFlightSchedulePlan(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException, FlightSchedulePlanDeleteException;    
    FlightSchedulePlan retrieveFlightSchedulePlanById(Long fspId);
}
