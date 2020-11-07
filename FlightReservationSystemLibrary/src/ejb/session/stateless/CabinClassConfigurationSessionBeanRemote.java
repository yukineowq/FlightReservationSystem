/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;

/**
 *
 * @author reuben
 */
@Remote
public interface CabinClassConfigurationSessionBeanRemote {
    Long createNewCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration, Long aircraftConfigurationId) throws InputDataValidationException;
}
