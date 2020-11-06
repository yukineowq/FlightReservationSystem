/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author reuben
 */
@Stateless
public class CabinClassConfigurationSessionBean implements CabinClassConfigurationSessionBeanRemote, CabinClassConfigurationSessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
     @Override
    public Long createNewCabinClassConfiguration(CabinClassConfiguration newCabinClassConfiguration, Long aircraftConfigurationId){
        AircraftConfiguration aircraftConfiguration = entityManager.find(AircraftConfiguration.class, aircraftConfigurationId);
        if (aircraftConfiguration != null) {
            newCabinClassConfiguration.setAircraftConfiguration(aircraftConfiguration);
            aircraftConfiguration.getCabinClassConfigurations().add(newCabinClassConfiguration);
        }
        entityManager.persist(newCabinClassConfiguration);
        entityManager.flush();
        
        return newCabinClassConfiguration.getCabinClassConfigurationId();
    }
}
