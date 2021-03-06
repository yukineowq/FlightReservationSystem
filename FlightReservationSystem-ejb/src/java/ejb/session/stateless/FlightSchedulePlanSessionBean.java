/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import util.exception.FlightScheduleContainsReservationException;
import util.exception.FlightSchedulePlanDoesNotExistException;
import util.exception.InputDataValidationException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import util.exception.FlightSchedulePlanDeleteException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightSchedulePlanSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan) throws InputDataValidationException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(newFlightSchedulePlan);

        if (constraintViolations.isEmpty()) {

            entityManager.persist(newFlightSchedulePlan);
            entityManager.flush();

            return newFlightSchedulePlan.getFlightSchedulePlanId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<FlightSchedulePlan> viewAllFlightSchedulePlans() {
        Query query = entityManager.createQuery("SELECT fl FROM FlightSchedulePlan fl");
        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();
        flightSchedulePlans.size();
        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
            flightSchedulePlan.getFlightSchedules().size();
            List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
            for (FlightSchedule flightSchedule : flightSchedules) {
                flightSchedule.getFlightReservations().size();
            }
            flightSchedulePlan.getFlight();
            flightSchedulePlan.getFares().size();
        }
        return flightSchedulePlans;
    }

    @Override
    public FlightSchedulePlan viewFlightSchedulePlanDetails(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException {
        FlightSchedulePlan flightSchedulePlan = entityManager.find(FlightSchedulePlan.class, flightSchedulePlanId);
        flightSchedulePlan.getFares().size();
        flightSchedulePlan.getFlightSchedules().size();
        return flightSchedulePlan;
    }

    @Override
    public void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws FlightSchedulePlanDoesNotExistException, FlightScheduleContainsReservationException, InputDataValidationException {
        if (flightSchedulePlan != null && flightSchedulePlan.getFlightSchedulePlanId() != null) {
            Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(flightSchedulePlan);
            if (constraintViolations.isEmpty()) {
                FlightSchedulePlan flightSchedulePlanToUpdate = entityManager.find(FlightSchedulePlan.class, flightSchedulePlan.getFlightSchedulePlanId());

                List<FlightSchedule> originalFlightSchedules = flightSchedulePlanToUpdate.getFlightSchedules();
                List<FlightSchedule> updatedFlightSchedules = flightSchedulePlan.getFlightSchedules();

                for (FlightSchedule flightSchedule : originalFlightSchedules) {
                    if (!updatedFlightSchedules.contains(flightSchedule)) {
                        if (!flightSchedule.getFlightReservations().isEmpty()) {
                            throw new FlightScheduleContainsReservationException();
                        }
                    }
                }

                flightSchedulePlanToUpdate.setFares(flightSchedulePlan.getFares());
                flightSchedulePlanToUpdate.setFlightSchedules(flightSchedulePlan.getFlightSchedules());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new FlightSchedulePlanDoesNotExistException();
        }
    }

    //Assuming that not used means there's no flight reservation associated with any of the flight schedules in the flight schedule plan
    @Override
    public void deleteFlightSchedulePlan(Long flightSchedulePlanId) throws FlightSchedulePlanDoesNotExistException, FlightSchedulePlanDeleteException {
        /*
        FlightSchedulePlan flightSchedulePlan = viewFlightSchedulePlanDetails(flightSchedulePlanId);
        boolean notUsed = true;
        List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
        for (FlightSchedule flightSchedule : flightSchedules) {
            if (!flightSchedule.getFlightReservations().isEmpty()) {
                notUsed = false;
            }
        }
        if (notUsed) {
            try {
                for (FlightSchedule flightSchedule : flightSchedules) {
                    flightSchedule.setFlightSchedulePlan(null);
                    flightSchedulePlan.getFlightSchedules().remove(flightSchedule);
                }
                List<Fare> fares = flightSchedulePlan.getFares();
                for (Fare fare : fares) {
                    fare.setFlightSchedulePlan(null);
                    flightSchedulePlan.getFares().remove(fare);
                }
                Flight flight = flightSchedulePlan.getFlight();
                flight.getFlightSchedulePlan().remove(flightSchedulePlan);
                flightSchedulePlan.setFlight(null);
                entityManager.remove(flightSchedulePlan);
            } catch (Exception ex) {
                System.out.println("Flight is associated with a complementary flight, cannot be removed. Flight schedule plan set to disabled instead.");
                flightSchedulePlan.setStatus(StatusEnum.DISABLED);
                throw new FlightSchedulePlanDeleteException();
            }
        } else {
            flightSchedulePlan.setStatus(StatusEnum.DISABLED);
            throw new FlightSchedulePlanDeleteException();
        }
         */
        FlightSchedulePlan flightSchedulePlan = viewFlightSchedulePlanDetails(flightSchedulePlanId);
        List<Fare> fares = flightSchedulePlan.getFares();
        List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
        if (fares.isEmpty() && flightSchedules.isEmpty()) {
            entityManager.remove(flightSchedulePlan);
        } else {
            flightSchedulePlan.setStatus(StatusEnum.DISABLED);
            throw new FlightSchedulePlanDeleteException();
        }
    }

    @Override
    public FlightSchedulePlan retrieveFlightSchedulePlanById(Long fspId) {
        FlightSchedulePlan flightSchedulePlan = entityManager.find(FlightSchedulePlan.class, fspId);

        flightSchedulePlan.getFares().size();
        flightSchedulePlan.getFlightSchedules().size();
        return flightSchedulePlan;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
