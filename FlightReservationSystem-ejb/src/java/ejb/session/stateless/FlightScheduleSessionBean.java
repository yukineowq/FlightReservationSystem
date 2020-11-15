/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
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

    @Override
    public List<List<FlightSchedule>> searchFlightSchedule(Airport origin, Airport destination, String departureDate, int numPassenger, PreferenceEnum preferenceEnum, CabinClassEnum cabinClassEnum) {

        List<List<FlightSchedule>> flightScheduleList = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT fs FROM FlightSchedule fs");
        List<FlightSchedule> allFlightSchedules = query.getResultList();
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat fsd = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String originCode = origin.getAirportCode();
        String destinationCode = destination.getAirportCode();
        try {
            date1 = fsd.parse(departureDate);
        } catch (ParseException ex) {
            Logger.getLogger(FlightScheduleSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        dates.add(date1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date1);
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

        if (preferenceEnum.equals(PreferenceEnum.DIRECT)) {
            for (Date date : dates) {
                String dateString = fsd.format(date);
                query = entityManager.createQuery("SELECT f FROM FlightSchedule f WHERE f.flightSchedulePlan.flight.flightRoute.origin.airportCode = :inOrigin AND f.flightSchedulePlan.flight.flightRoute.destination.airportCode = :inDestination AND f.departureDate= :inDepartureDate");
                query.setParameter("inOrigin", originCode);
                query.setParameter("inDestination", destinationCode);
                query.setParameter("inDepartureDate", dateString);
                List<FlightSchedule> flightSchedules = query.getResultList();
                List<FlightSchedule> flightSchedulesToBeAdded = new ArrayList<>();
                for (FlightSchedule flightSchedule : flightSchedules) {
                    flightSchedule.getFlightSchedulePlan().getFares().size();
                    List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                    if (cabinClassEnum.equals(CabinClassEnum.NA)) {
                        for (SeatInventory seatInventory : seatInventories) {
                            if (seatInventory.getAvailable() > numPassenger) {
                                flightSchedulesToBeAdded.add(flightSchedule);
                            }
                            break;
                        }
                    } else {
                        for (SeatInventory seatInventory : seatInventories) {
                            if (seatInventory.getCabinClass().equals(cabinClassEnum)) {
                                if (seatInventory.getAvailable() > numPassenger) {
                                    flightSchedulesToBeAdded.add(flightSchedule);
                                }
                            }
                        }
                    }
                }
                flightScheduleList.add(flightSchedulesToBeAdded);
            }
        } else if (preferenceEnum.equals(PreferenceEnum.CONNECTING)) {
            for (Date date : dates) {
                String dateString = fsd.format(date);
                List<FlightSchedule> flightSchedulesToBeAdded = new ArrayList<>();
                query = entityManager.createQuery("SELECT f FROM FlightSchedule f WHERE f.flightSchedulePlan.flight.flightRoute.origin.airportCode = :inOrigin AND f.departureDate = :inDepartureDate");
                query.setParameter("inOrigin", originCode);
                query.setParameter("inDepartureDate", dateString);
                List<FlightSchedule> flightSchedulesOrigins = query.getResultList();
                query = entityManager.createQuery("SELECT f FROM FlightSchedule f WHERE f.flightSchedulePlan.flight.flightRoute.destination.airportCode = :inDestination AND f.departureDate = :inDepartureDate");
                query.setParameter("inDestination", destinationCode);
                query.setParameter("inDepartureDate", dateString);
                List<FlightSchedule> flightSchedulesDestinations = query.getResultList();

                for (FlightSchedule flightScheduleOrigin : flightSchedulesOrigins) {
                    flightScheduleOrigin.getFlightSchedulePlan().getFares().size();
                    Airport originDestination = flightScheduleOrigin.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination();
                    Calendar arrivingCalendar = flightScheduleOrigin.getArrivalTime();
                    arrivingCalendar.add(Calendar.HOUR_OF_DAY, 2); //Assumption that 2 hours is needed to transit from one flight to another

                    for (FlightSchedule flightScheduleDestination : flightSchedulesDestinations) {
                        flightScheduleDestination.getFlightSchedulePlan().getFares().size();
                        Airport destinationOrigin = flightScheduleDestination.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin();
                        Calendar departingCalendar = flightScheduleDestination.getDepartureTime();

                        if ((originDestination.getAirportCode().equals(destinationOrigin.getAirportCode())) && departingCalendar.after(arrivingCalendar)) {
                            boolean cabinClassPreference = true;
                            int counter = 0;
                            List<SeatInventory> seatInventoriesOrigin = flightScheduleOrigin.getSeatInventories();
                            if (cabinClassEnum.equals(CabinClassEnum.NA)) {
                                boolean unvisited = true;
                                for (SeatInventory seatInventory : seatInventoriesOrigin) {
                                    if ((seatInventory.getAvailable() > numPassenger) && unvisited) {
                                        counter++;
                                        unvisited = false;
                                    }
                                }
                            } else {

                                for (SeatInventory seatInventory : seatInventoriesOrigin) {
                                    if (seatInventory.getCabinClass().equals(cabinClassEnum)) {
                                        if (seatInventory.getAvailable() < numPassenger) {
                                            cabinClassPreference = false;
                                        } else {
                                            counter++;
                                            break;
                                        }
                                    }
                                }
                            }
                            List<SeatInventory> seatInventoriesDestination = flightScheduleDestination.getSeatInventories();
                            if (cabinClassEnum.equals(CabinClassEnum.NA)) {
                                for (SeatInventory seatInventory : seatInventoriesDestination) {
                                    boolean unvisited = true;
                                    if ((seatInventory.getAvailable() > numPassenger) && unvisited) {
                                        counter++;
                                        unvisited = false;
                                    }
                                }
                            } else {
                                for (SeatInventory seatInventory : seatInventoriesDestination) {
                                    if (seatInventory.getCabinClass().equals(cabinClassEnum)) {
                                        if (seatInventory.getAvailable() < numPassenger) {
                                            cabinClassPreference = false;
                                        } else {
                                            counter++;
                                            break;
                                        }
                                    }

                                }
                            }
                            if (cabinClassPreference && counter == 2) {
                                flightSchedulesToBeAdded.add(flightScheduleOrigin);
                                flightSchedulesToBeAdded.add(flightScheduleDestination);

                            }
                        }
                    }
                }
                flightScheduleList.add(flightSchedulesToBeAdded);
            }

        } else {
            for (Date date : dates) {
                String dateString = fsd.format(date);
                query = entityManager.createQuery("SELECT f FROM FlightSchedule f WHERE f.flightSchedulePlan.flight.flightRoute.origin = :inOrigin AND f.flightSchedulePlan.flight.flightRoute.destination = :inDestination AND f.departureDate = :inDepartureDate");
                query.setParameter("inOrigin", origin);
                query.setParameter("inDestination", destination);
                query.setParameter("inDepartureDate", dateString);
                List<FlightSchedule> flightSchedules = query.getResultList();
                List<FlightSchedule> flightSchedulesToBeAdded = new ArrayList<>();
                for (FlightSchedule flightSchedule : flightSchedules) {
                    flightSchedule.getFlightSchedulePlan().getFares().size();
                    List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                    for (SeatInventory seatInventory : seatInventories) {
                        if (seatInventory.getCabinClass().equals(cabinClassEnum)) {
                            if (seatInventory.getAvailable() > numPassenger) {
                                flightSchedulesToBeAdded.add(flightSchedule);
                            }
                        }
                    }
                }
                flightScheduleList.add(flightSchedulesToBeAdded);
                query = entityManager.createQuery("SELECT f FROM FlightSchedule f WHERE f.flightSchedulePlan.flight.flightRoute.origin = :inOrigin AND f.departureDate = :inDepartureDate");
                query.setParameter("inOrigin", origin);
                query.setParameter("inDepartureDate", dateString);
                List<FlightSchedule> flightSchedulesOrigins = query.getResultList();
                query = entityManager.createQuery("SELECT f FROM FlightSchedule f WHERE f.flightSchedulePlan.flight.flightRoute.destination = :inDestination AND f.departureDate = :inDepartureDate");
                query.setParameter("inDestination", destination);
                query.setParameter("inDepartureDate", dateString);
                List<FlightSchedule> flightSchedulesDestinations = query.getResultList();
                for (FlightSchedule flightScheduleOrigin : flightSchedulesOrigins) {
                    flightScheduleOrigin.getFlightSchedulePlan().getFares().size();
                    Airport originDestination = flightScheduleOrigin.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination();
                    Calendar arrivingCalendar = flightScheduleOrigin.getArrivalTime();
                    arrivingCalendar.add(Calendar.HOUR_OF_DAY, 2); //Assumption that 2 hours is needed to transit from one flight to another
                    for (FlightSchedule flightScheduleDestination : flightSchedulesDestinations) {
                        flightScheduleDestination.getFlightSchedulePlan().getFares().size();
                        Airport destinationOrigin = flightScheduleDestination.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin();
                        Calendar departingCalendar = flightScheduleDestination.getDepartureTime();
                        if ((originDestination.getAirportCode().equals(destinationOrigin.getAirportCode())) && departingCalendar.after(arrivingCalendar)) {
                            boolean cabinClassPreference = true;
                            int counter = 0;
                            List<SeatInventory> seatInventoriesOrigin = flightScheduleOrigin.getSeatInventories();
                            for (SeatInventory seatInventory : seatInventoriesOrigin) {
                                if (seatInventory.getCabinClass().equals(cabinClassEnum)) {
                                    if (seatInventory.getAvailable() < numPassenger) {
                                        cabinClassPreference = false;
                                    } else {
                                        counter++;
                                    }
                                }
                            }
                            List<SeatInventory> seatInventoriesDestination = flightScheduleDestination.getSeatInventories();
                            for (SeatInventory seatInventory : seatInventoriesDestination) {
                                if (seatInventory.getCabinClass().equals(cabinClassEnum)) {
                                    if (seatInventory.getAvailable() < numPassenger) {
                                        cabinClassPreference = false;
                                    } else {
                                        counter++;
                                    }
                                }
                            }
                            if (cabinClassPreference && counter == 2) {
                                flightSchedulesToBeAdded.add(flightScheduleOrigin);
                                flightSchedulesToBeAdded.add(flightScheduleDestination);

                            }
                        }
                    }
                }
                flightScheduleList.add(flightSchedulesToBeAdded);
            }
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
