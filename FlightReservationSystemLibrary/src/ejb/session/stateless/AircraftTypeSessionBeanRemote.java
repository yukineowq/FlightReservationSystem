/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Remote;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author reuben
 */
@Remote
public interface AircraftTypeSessionBeanRemote {
    Long createNewAircraftType(AircraftType newAircraftType);
    AircraftType retrieveAircraftTypeByName(String name) throws AircraftTypeNotFoundException;
}
