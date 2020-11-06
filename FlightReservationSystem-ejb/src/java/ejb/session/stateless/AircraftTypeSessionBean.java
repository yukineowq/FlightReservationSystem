/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author reuben
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long createNewAircraftType(AircraftType newAircraftType){
        
        entityManager.persist(newAircraftType);
        entityManager.flush();
        
        return newAircraftType.getAircraftTypeId();
    }
    
    @Override
    public AircraftType retrieveAircraftTypeByName(String name) throws AircraftTypeNotFoundException {
        Query query = entityManager.createQuery("SELECT e FROM AircraftType e WHERE e.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            AircraftType aircraftType = (AircraftType)query.getSingleResult();
            aircraftType.getAircraftConfigurations().size();
            return aircraftType;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AircraftTypeNotFoundException("Aircraft type " + name + " does not exist!");
        }
    }
}
