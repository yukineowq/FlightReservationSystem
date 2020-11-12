/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Flight;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class CabinClassConfigurationSessionBean implements CabinClassConfigurationSessionBeanRemote, CabinClassConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CabinClassConfigurationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCabinClassConfiguration(CabinClassConfiguration newCabinClassConfiguration, Long aircraftConfigurationId) throws InputDataValidationException {
        Set<ConstraintViolation<CabinClassConfiguration>> constraintViolations = validator.validate(newCabinClassConfiguration);
        if (constraintViolations.isEmpty()) {
            AircraftConfiguration aircraftConfiguration = entityManager.find(AircraftConfiguration.class, aircraftConfigurationId);
            if (aircraftConfiguration != null) {
                newCabinClassConfiguration.setAircraftConfiguration(aircraftConfiguration);
                aircraftConfiguration.getCabinClassConfigurations().add(newCabinClassConfiguration);
            }
            entityManager.persist(newCabinClassConfiguration);
            entityManager.flush();

            return newCabinClassConfiguration.getCabinClassConfigurationId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<CabinClassConfiguration> retrieveCabinClassConfigurationsByFlight(String flightNumber) {
        Query query = entityManager.createQuery("SELECT mg FROM CabinClassConfiguration mg WHERE mg.flights.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);
        List<CabinClassConfiguration> cabinClassConfigurations = query.getResultList();
        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
            cabinClassConfiguration.getFares().size();
            cabinClassConfiguration.getFlights().size();
            cabinClassConfiguration.getAircraftConfiguration();
        }
        cabinClassConfigurations.size();
        return cabinClassConfigurations;
    }

    @Override
    public List<CabinClassConfiguration> retrieveCabinClassConfigurationsByAircraftConfiguration(String airConfigName) {
        Query query = entityManager.createQuery("SELECT mg FROM CabinClassConfiguration mg WHERE mg.aircraftConfiguration.name = :inAirConfigName");
        query.setParameter("inAirConfigName", airConfigName);
        List<CabinClassConfiguration> cabinClassConfigurations = query.getResultList();

        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
            cabinClassConfiguration.getFares().size();
            cabinClassConfiguration.getFlights().size();
        }
        cabinClassConfigurations.size();
        return cabinClassConfigurations;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CabinClassConfiguration>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
