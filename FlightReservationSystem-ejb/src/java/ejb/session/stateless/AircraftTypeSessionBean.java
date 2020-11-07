/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AircraftTypeNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author reuben
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AircraftTypeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewAircraftType(AircraftType newAircraftType) throws InputDataValidationException {
        Set<ConstraintViolation<AircraftType>> constraintViolations = validator.validate(newAircraftType);

        if (constraintViolations.isEmpty()) {
            entityManager.persist(newAircraftType);
            entityManager.flush();
            return newAircraftType.getAircraftTypeId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public AircraftType retrieveAircraftTypeByName(String name) throws AircraftTypeNotFoundException {
        Query query = entityManager.createQuery("SELECT e FROM AircraftType e WHERE e.name = :inName");
        query.setParameter("inName", name);

        try {
            AircraftType aircraftType = (AircraftType) query.getSingleResult();
            aircraftType.getAircraftConfigurations().size();
            return aircraftType;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AircraftTypeNotFoundException("Aircraft type " + name + " does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AircraftType>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
