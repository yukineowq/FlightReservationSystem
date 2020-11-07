/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import util.exception.InputDataValidationException;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigurationNameExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {
    Long createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration, String aircraftTypeName) throws AircraftConfigurationNameExistException, UnknownPersistenceException, AircraftTypeNotFoundException, InputDataValidationException;
    
    List<AircraftConfiguration> retrieveAllAircraftConfigurations();
    
    AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException;
}
