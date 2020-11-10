/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import entity.Fare;
import entity.FlightSchedulePlan;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FareSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewFare(Fare newFare, Long cabinClassConfigurationId, Long flightSchedulePlanId) throws InputDataValidationException {
        Set<ConstraintViolation<Fare>> constraintViolations = validator.validate(newFare);
        if (constraintViolations.isEmpty()) {
            CabinClassConfiguration cabinClassConfiguration = entityManager.find(CabinClassConfiguration.class, cabinClassConfigurationId);
            if (cabinClassConfiguration != null) {
                newFare.setCabinClassConfiguration(cabinClassConfiguration);
                cabinClassConfiguration.getFares().add(newFare);
            }
            FlightSchedulePlan flightSchedulePlan = entityManager.find(FlightSchedulePlan.class, flightSchedulePlanId);
            if (flightSchedulePlan != null) {
                flightSchedulePlan.getFares().add(newFare);
                newFare.setFlightSchedulePlan(flightSchedulePlan);
            }
            entityManager.persist(newFare);
            entityManager.flush();

            return newFare.getFareId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Fare>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
