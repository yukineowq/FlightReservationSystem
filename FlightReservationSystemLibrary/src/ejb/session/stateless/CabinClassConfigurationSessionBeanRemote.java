/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Remote
public interface CabinClassConfigurationSessionBeanRemote {
    public List<CabinClassConfiguration> retrieveCabinClassConfigurationsByFlight(String flightNumber);
    public List<CabinClassConfiguration> retrieveCabinClassConfigurationsByAircraftConfiguration(String airConfigName) ;
    Long createNewCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration, Long aircraftConfigurationId) throws InputDataValidationException;
}
