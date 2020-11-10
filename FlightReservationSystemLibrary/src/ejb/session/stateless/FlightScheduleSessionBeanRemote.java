/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import javax.ejb.Remote;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Remote
public interface FlightScheduleSessionBeanRemote {
    public Long createNewFlightSchedule(FlightSchedule newFlightSchedule, Long flightSchedulePlanId)throws FlightScheduleNotFoundException, UnknownPersistenceException, InputDataValidationException;
}
