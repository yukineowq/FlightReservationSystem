/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightRouteOriginExistException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.SeatNumberNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewFlightReservation(FlightReservation newFlightReservation, Long flightScheduleId) throws FlightReservationNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<FlightReservation>> constraintViolations = validator.validate(newFlightReservation);
        if (constraintViolations.isEmpty()) {
            FlightSchedule flightSchedule = entityManager.find(FlightSchedule.class, flightScheduleId);
            if (flightSchedule != null) {
                newFlightReservation.setFlightSchedule(flightSchedule);
                flightSchedule.getFlightReservations().add(newFlightReservation);
            }
            try {
                entityManager.persist(newFlightReservation);
                entityManager.flush();

                return newFlightReservation.getFlightReservationId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightReservationNotFoundException();
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

    @Override
    public FlightReservation retrieveFlightReservationBySeatNumber(String seatNumber) throws SeatNumberNotFoundException {
        Query query = entityManager.createQuery("SELECT r FROM FlightReservation r WHERE r.seatNumber = :inSeatNumber");
        query.setParameter("inSeatNumber", seatNumber);

        try {
            return (FlightReservation) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new SeatNumberNotFoundException("Seat number: " + seatNumber + " does not exist!");
        }
    }

    @Override
    public List<FlightReservation> viewFlightReservations() {
        Query query = entityManager.createQuery("SELECT r FROM FlightReservation r");
        List<FlightReservation> flightReservations = query.getResultList();
        flightReservations.size();
        return flightReservations;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightReservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
