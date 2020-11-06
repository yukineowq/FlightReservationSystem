/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClassConfiguration;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftConfigurationNameExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long createNewAircraftConfiguration(AircraftConfiguration newAircraftConfiguration, Long aircraftTypeId) throws AircraftConfigurationNameExistException, UnknownPersistenceException {
        AircraftType aircraftType = entityManager.find(AircraftType.class, aircraftTypeId);
       
        if (aircraftType != null) {
            newAircraftConfiguration.setAircraftType(aircraftType);
            aircraftType.getAircraftConfigurations().add(newAircraftConfiguration);
        }
        try
        {
            entityManager.persist(newAircraftConfiguration);
            entityManager.flush();

            return newAircraftConfiguration.getAircraftConfigurationId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new AircraftConfigurationNameExistException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public List<AircraftConfiguration> retrieveAllAircraftConfigurations() {
        Query query = entityManager.createQuery("SELECT a FROM AircraftConfiguration a");
        List<AircraftConfiguration> aircraftConfigurations = query.getResultList();
        aircraftConfigurations.size();
        return aircraftConfigurations;
    }
    
    @Override
    public AircraftConfiguration retrieveAircraftConfigurationByName(String name) throws AircraftConfigurationNotFoundException {
        Query query = entityManager.createQuery("SELECT a FROM AircraftConfiguration a WHERE a.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            AircraftConfiguration aircraftConfiguration = (AircraftConfiguration)query.getSingleResult();
            aircraftConfiguration.getFlights().size();
            return aircraftConfiguration;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AircraftConfigurationNotFoundException("Aircraft Configuration name:" + name + " does not exist!");
        }
    }
}

