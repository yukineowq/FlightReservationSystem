/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Local;
import util.exception.AircraftConfigurationNameExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Local
public interface AircraftConfigurationSessionBeanLocal {
    Long createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration, Long aircraftTypeId) throws AircraftConfigurationNameExistException, UnknownPersistenceException;
    
    List<AircraftConfiguration> retrieveAllAircraftConfigurations();
    
    AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException;
}
