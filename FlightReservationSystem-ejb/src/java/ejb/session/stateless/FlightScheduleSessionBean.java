/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassEnum;
import util.enumeration.PreferenceEnum;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightScheduleSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewFlightSchedule(FlightSchedule newFlightSchedule, Long flightSchedulePlanId) throws FlightScheduleNotFoundException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<FlightSchedule>> constraintViolations = validator.validate(newFlightSchedule);
        if (constraintViolations.isEmpty()) {
            FlightSchedulePlan flightSchedulePlan = entityManager.find(FlightSchedulePlan.class, flightSchedulePlanId);
            if (flightSchedulePlan != null) {
                flightSchedulePlan.getFlightSchedules().add(newFlightSchedule);
                newFlightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            }
            try {
                entityManager.persist(newFlightSchedule);
                entityManager.flush();

                return newFlightSchedule.getFlightScheduleId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightScheduleNotFoundException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    public List<List<FlightSchedule>> searchFlightSchedule(Airport origin, Airport destination, Date departureDate, int numPassenger, PreferenceEnum preferenceEnum, CabinClassEnum cabinClassEnum) {
        List<List<FlightSchedule>> flightScheduleList = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT fs FROM FlightSchedule fs");
        List<FlightSchedule> allFlightSchedules = query.getResultList();
        List<Date> dates = new ArrayList<>();
        dates.add(departureDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(departureDate);
        cal.add(Calendar.DATE, -1);
        Date dateBefore1Days = cal.getTime();
        dates.add(dateBefore1Days);
        cal.add(Calendar.DATE, -1);
        Date dateBefore2Days = cal.getTime();
        dates.add(dateBefore2Days);
        cal.add(Calendar.DATE, -1);
        Date dateBefore3Days = cal.getTime();
        dates.add(dateBefore3Days);
        cal2.add(Calendar.DATE, 1);
        Date dateAfter1Days = cal2.getTime();
        dates.add(dateAfter1Days);
        cal2.add(Calendar.DATE, 1);
        Date dateAfter2Days = cal2.getTime();
        dates.add(dateAfter2Days);
        cal2.add(Calendar.DATE, 1);
        Date dateAfter3Days = cal2.getTime();
        dates.add(dateAfter3Days);
        if (preferenceEnum.equals(PreferenceEnum.NA)) {
            for (Date date : dates) {
                 query = entityManager.createQuery("SELECT f FROM Flight f");
                List<FlightSchedule> flightSchedules = query.getResultList();
            }
        } else if (preferenceEnum.equals(PreferenceEnum.DIRECT)) {

        } else {

        }
        return flightScheduleList;

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedule>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
