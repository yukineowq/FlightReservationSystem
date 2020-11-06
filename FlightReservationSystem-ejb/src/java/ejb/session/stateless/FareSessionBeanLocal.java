/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import javax.ejb.Local;

/**
 *
 * @author reuben
 */
@Local
public interface FareSessionBeanLocal {
    public Long createNewFare(Fare newFare, Long cabinClassConfigurationId, Long flightSchedulePlanId);
}
