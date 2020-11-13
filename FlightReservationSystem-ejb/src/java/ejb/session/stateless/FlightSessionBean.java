/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import util.enumeration.StatusEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteDoesNotExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;
import util.exception.FlightDeleteException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;

    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewFlight(Flight newFlight) throws FlightNumberExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(newFlight);
        if (constraintViolations.isEmpty()) {

            try {
                entityManager.persist(newFlight);
                entityManager.flush();

                return newFlight.getFlightId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightNumberExistException();
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

    public List<Flight> retrieveAllFlights() {
        Query query = entityManager.createQuery("SELECT fl FROM Flight fl");
        List<Flight> flights = query.getResultList();
        for (Flight flight: flights) {
            flight.getAircraftConfiguration();
        }
        flights.size();
        return flights;
    }

    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightNumberExistException {
        Query query = entityManager.createQuery("SELECT fl FROM Flight fl WHERE fl.flightNumber = :inFlightNumber");
        query.setParameter("inFlightNumber", flightNumber);

        try {
            Flight flight = (Flight) query.getSingleResult();
            flight.getAircraftConfiguration().getCabinClassConfigurations().size();
            List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlan();
            flightSchedulePlans.size();
            for(FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
                flightSchedulePlan.getFlightSchedules().size();
                List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
                for(FlightSchedule flightSchedule : flightSchedules) {
                    flightSchedule.getSeatInventories().size();
                    flightSchedule.getFlightReservations().size();
                }
            }
            List<CabinClassConfiguration> cabinClassConfigurations = flight.getCabinClassConfiguration();
            cabinClassConfigurations.size();
            for(CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                cabinClassConfiguration.getFares().size();
            }
            return flight;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightNumberExistException("Flight number:" + flightNumber + " does not exist!");
        }
    }

    @Override
    public void updateFlight(Flight flight) throws FlightNotFoundException, UpdateFlightException, InputDataValidationException {

        if (flight != null && flight.getFlightId() != null) {
            Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);

            if (constraintViolations.isEmpty()) {
                Flight flightToUpdate = new Flight();

                flightToUpdate = entityManager.find(Flight.class, flight.getFlightId());

                flightToUpdate.setFlightNumber(flight.getFlightNumber());
                flightToUpdate.setAircraftConfiguration(flight.getAircraftConfiguration());
                flightToUpdate.setStatus(flight.getStatus());
                flightToUpdate.setFlightSchedulePlan(flight.getFlightSchedulePlan());
                flightToUpdate.setFlightRoute(flight.getFlightRoute());
                if (flight.getComplementaryFlightNumber().length() > 0) {
                    flightToUpdate.setComplementaryFlightNumber(flight.getComplementaryFlightNumber());
                }

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new FlightNotFoundException("Flight not found in records");
        }
    }

    @Override
    public void deleteFlight(String flightNumber) throws FlightNumberExistException, FlightDeleteException {
        Flight flight = retrieveFlightByFlightNumber(flightNumber);
        List<FlightSchedulePlan> flightSchedulePlan = flight.getFlightSchedulePlan();
        if (flightSchedulePlan.isEmpty()) {
            entityManager.remove(flight);
        } else {
            flight.setStatus(StatusEnum.DISABLED);
            throw new FlightDeleteException();
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Flight>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
