/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemmanagementclient;

import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassConfigurationSessionBeanRemote;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.AircraftConfiguration;
import entity.FlightSchedulePlan;
import entity.FlightSchedule;
import entity.Airport;
import entity.Fare;
import entity.CabinClassConfiguration;
import entity.SeatInventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.EmployeeAccessRightEnum;
import util.enumeration.ScheduleTypeEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.FlightDeleteException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;
import util.exception.FlightNotFoundException;
import util.exception.UpdateFlightException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.stream.Stream;
import util.enumeration.CabinClassEnum;
import util.exception.AirportNotFoundException;
import util.exception.FlightScheduleContainsReservationException;
import util.exception.FlightSchedulePlanDeleteException;
import util.exception.FlightSchedulePlanDoesNotExistException;
import util.exception.UnableToDeleteFlightScheduleException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
public class FlightOperationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private Employee currentEmployee;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote;

    public FlightOperationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public FlightOperationModule(Employee currentEmployee, FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote, CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.cabinClassConfigurationSessionBeanRemote = cabinClassConfigurationSessionBeanRemote;
    }

    public void menuFlightOperation() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != EmployeeAccessRightEnum.SCHEDULEMANAGER) {
            throw new InvalidAccessRightException("You don't have Schedule Manager rights to access the Flight Operation module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("***  FRS :: Flight Operation Module ***\n");
            System.out.println("1: Create Flight");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("4: Create Flight Schedule Plan");
            System.out.println("5: View All Flight Schedule Plan");
            System.out.println("6: View Flight Schedule Plan Details");
            System.out.println("7: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doCreateFlight();
                    } catch (FlightNumberExistException ex) {
                        System.out.println("Flight number already exist in database. Flight number must be unique!");
                    }
                } else if (response == 2) {
                    doViewAllFlights();
                } else if (response == 3) {
                    doViewFlightDetails();
                } else if (response == 4) {
                    doCreateFlightSchedulePlan();
                } else if (response == 5) {
                    doViewAllFlightSchedulePlans();
                } else if (response == 6) {
                    try {
                        doViewFlightSchedulePlanDetails();
                    } catch (Exception ex) {
                        System.out.println("Flight Scheduleplan ID does not exist");
                    }
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
            }
        }
    }

    public void doCreateFlightSchedulePlan() {
        try {
            Scanner scanner = new Scanner(System.in);
            boolean complementary = false;
            //For purpose to get num of days between start and end date
            Calendar cal1 = new GregorianCalendar();
            Calendar cal2 = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat fsd = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date departureDate;
            FlightSchedulePlan flightSchedulePlan = new FlightSchedulePlan();
            FlightSchedulePlan complementaryFSP = new FlightSchedulePlan();
            System.out.println("*** FRS ::  Flight Operation Module :: Create Flight Schedule Plan ***\n");
            System.out.print("Enter Flight Number> ");
            String flightNumber = scanner.nextLine().trim();
            flightSchedulePlan.setFlightNumber(flightNumber);

            Flight flight = new Flight();
            try {
                flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            } catch (FlightNumberExistException ex) {
                Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (flight.getComplementaryFlightNumber().length() > 0) {
                System.out.println("Would you like to create a complementary return flight schedule plan? Y/N >");
                String response = scanner.nextLine().trim();
                if (response.equals("Y")) {
                    complementary = true;
                }
            }
            flightSchedulePlan.setFlight(flight);
            Flight complementaryFlight = new Flight();
            if (flight.getComplementaryFlightNumber().length() > 0) {
                try {
                    complementaryFlight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flight.getComplementaryFlightNumber());
                } catch (FlightNumberExistException ex) {
                    Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
                }
                complementaryFSP.setFlight(complementaryFlight);
                complementaryFSP.setFlightNumber(complementaryFlight.getFlightNumber());
            }
            AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();
            try {
                aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(flight.getAircraftConfiguration().getName());
            } catch (AircraftConfigurationNotFoundException ex) {
                System.out.println("Aircraft configuration not found with flight number provided.");
            }

            List<CabinClassConfiguration> cabinClassConfigurations = flight.getCabinClassConfiguration();
            Airport originAirport = flight.getFlightRoute().getOrigin();
            Airport destinationAirport = flight.getFlightRoute().getDestination();
            int timeDifference = destinationAirport.getGMT() - originAirport.getGMT();

            System.out.println("1: Single schedule");
            System.out.println("2: Multiple schedule");
            System.out.println("3: Recurrent schedules after n day");
            System.out.println("4: Recurrent schedules every week");
            System.out.println("Select flight schedule plan type> ");
            int res = scanner.nextInt();
            scanner.nextLine();
            if (res == 1) {
                Calendar departingCalendar = new GregorianCalendar();
                Calendar arrivingCalendar = new GregorianCalendar();
                Calendar complementaryDepartingCalendar = new GregorianCalendar();
                Calendar complementaryArrivingCalendar = new GregorianCalendar();
                complementaryFSP.setScheduleType(ScheduleTypeEnum.SINGLE);
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.SINGLE);
                FlightSchedule flightSchedule = new FlightSchedule();
                System.out.print("Enter Departure Date (yyyy-mm-dd HH:MM)> ");
                String date = scanner.nextLine().trim();
                departureDate = inputDateFormat.parse(date);
                flightSchedulePlan.setFirstDepartureDate(date);
                complementaryFSP.setFirstDepartureDate(date);
                departingCalendar.setTime(departureDate);
                complementaryArrivingCalendar.setTime(departureDate);
                arrivingCalendar.setTime(departureDate);
                complementaryDepartingCalendar.setTime(departureDate);
                arrivingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                complementaryDepartingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                scanner.nextLine();

                arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                complementaryDepartingCalendar.add(GregorianCalendar.HOUR, hours);
                complementaryArrivingCalendar.add(GregorianCalendar.HOUR, hours);
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                scanner.nextLine();
                arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                complementaryDepartingCalendar.add(GregorianCalendar.MINUTE, mins);
                complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                flightSchedule.setEstimatedFlightMinute(mins);
                flightSchedule.setEstimatedFlightHour(hours);
                flightSchedule.setDepartureTime(departingCalendar);
                String formattedDate = fsd.format(departingCalendar.getTime());
                flightSchedule.setDepartureDate(formattedDate);
                flightSchedule.setArrivalTime(arrivingCalendar);
                flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                flightSchedule.setFlightSchedulePlan(flightSchedulePlan);

                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                    CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                    int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                    SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                    flightSchedule.getSeatInventories().add(seatInventory);
                    seatInventory.setFlightSchedule(flightSchedule);
                    cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                }

                if (complementary) {

                    FlightSchedule complementaryFS = new FlightSchedule();
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, 4); //layover perriod of 4 hours
                    complementaryArrivingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                    complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryFS.setArrivalTime(complementaryArrivingCalendar);
                    complementaryFS.setDepartureTime(complementaryDepartingCalendar);
                    formattedDate = fsd.format(complementaryDepartingCalendar.getTime());
                    complementaryFS.setDepartureDate(formattedDate);
                    complementaryFS.setEstimatedFlightHour(hours);
                    complementaryFS.setEstimatedFlightMinute(mins);
                    complementaryFSP.getFlightSchedules().add(complementaryFS);
                    complementaryFS.setFlightSchedulePlan(complementaryFSP);

                    for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                        CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                        int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                        SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                        complementaryFS.getSeatInventories().add(seatInventory);
                        seatInventory.setFlightSchedule(complementaryFS);
                        cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                    }

                }
            } else if (res == 2) {
                complementaryFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter number of schedules> ");
                int numSchedules = scanner.nextInt();
                scanner.nextLine();
                for (int i = 0; i < numSchedules; i++) {
                    Calendar departingCalendar = new GregorianCalendar();
                    Calendar arrivingCalendar = new GregorianCalendar();
                    Calendar complementaryDepartingCalendar = new GregorianCalendar();
                    Calendar complementaryArrivingCalendar = new GregorianCalendar();
                    FlightSchedule flightSchedule = new FlightSchedule();
                    System.out.println("Creating flight schedule: " + i);
                    System.out.print("Enter Departure Date (yyyy-mm-dd HH:MM)> ");
                    String date = scanner.nextLine().trim();
                    departureDate = inputDateFormat.parse(date);
                    flightSchedulePlan.setFirstDepartureDate(date);
                    complementaryFSP.setFirstDepartureDate(date);
                    departingCalendar.setTime(departureDate);
                    complementaryArrivingCalendar.setTime(departureDate);
                    arrivingCalendar.setTime(departureDate);
                    complementaryDepartingCalendar.setTime(departureDate);
                    arrivingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                    arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, hours);
                    complementaryArrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryDepartingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    flightSchedule.setDepartureTime(departingCalendar);
                    String formattedDate = fsd.format(departingCalendar.getTime());
                    flightSchedule.setDepartureDate(formattedDate);
                    flightSchedule.setArrivalTime(arrivingCalendar);
                    flightSchedule.setEstimatedFlightHour(hours);
                    flightSchedule.setEstimatedFlightMinute(mins);
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                    flightSchedule.setFlightSchedulePlan(flightSchedulePlan);

                    for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                        CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                        int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                        SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                        flightSchedule.getSeatInventories().add(seatInventory);
                        seatInventory.setFlightSchedule(flightSchedule);
                        cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                    }
                    if (complementary) {

                        FlightSchedule complementaryFS = new FlightSchedule();
                        complementaryDepartingCalendar.add(GregorianCalendar.HOUR, 4);
                        complementaryArrivingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalTime(complementaryArrivingCalendar);
                        complementaryFS.setDepartureTime(complementaryDepartingCalendar);
                        formattedDate = fsd.format(complementaryDepartingCalendar.getTime());
                        complementaryFS.setDepartureDate(formattedDate);
                        complementaryFS.setEstimatedFlightHour(hours);
                        complementaryFS.setEstimatedFlightMinute(mins);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFS.setFlightSchedulePlan(complementaryFSP);

                        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                            CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                            int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                            SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                            complementaryFS.getSeatInventories().add(seatInventory);
                            seatInventory.setFlightSchedule(complementaryFS);
                            cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                        }

                    }
                }

            } else if (res == 3) {

                complementaryFSP.setScheduleType(ScheduleTypeEnum.RECURRENT);
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.RECURRENT);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter n> ");
                int n = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter effective date (ddMMyyyy) >");
                Date date = sdf.parse(scanner.nextLine().trim());
                cal1.setTime(date);
                System.out.println("Enter end date (ddMMyyyy) >");
                date = sdf.parse(scanner.nextLine().trim());
                cal2.setTime(date);
                int daysBetween = daysBetween(cal1.getTime(), cal2.getTime()) + 1;
                System.out.print("Enter Departure Date (yyyy-mm-dd HH:MM)> ");
                String date1 = scanner.nextLine().trim();
                departureDate = inputDateFormat.parse(date1);
                flightSchedulePlan.setFirstDepartureDate(date1);
                complementaryFSP.setFirstDepartureDate(date1);
                Calendar counterCal = new GregorianCalendar();
                counterCal.setTime(departureDate);

                for (int i = 0; i < daysBetween; i = i + n) {
                    Calendar departingCalendar = new GregorianCalendar();
                    Calendar arrivingCalendar = new GregorianCalendar();
                    Calendar complementaryDepartingCalendar = new GregorianCalendar();
                    Calendar complementaryArrivingCalendar = new GregorianCalendar();
                    departingCalendar.setTime(departureDate);
                    complementaryArrivingCalendar.setTime(departureDate);
                    arrivingCalendar.setTime(departureDate);
                    complementaryDepartingCalendar.setTime(departureDate);
                    //If n = 4, not sure if it's 1,4,8 or 1,5,9. For the code, it's the latter
                    //, for the former, will be: counterCal.add(Calendar.DATE, n-1); on line 382
                    counterCal.add(Calendar.DATE, n);
                    String str = inputDateFormat.format(counterCal.getTime());
                    departureDate = inputDateFormat.parse(str);
                    arrivingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                    arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, hours);
                    complementaryArrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryDepartingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    FlightSchedule flightSchedule = new FlightSchedule();
                    flightSchedule.setDepartureTime(departingCalendar);
                    String formattedDate = fsd.format(departingCalendar.getTime());
                    flightSchedule.setDepartureDate(formattedDate);
                    flightSchedule.setArrivalTime(arrivingCalendar);
                    flightSchedule.setEstimatedFlightHour(hours);
                    flightSchedule.setEstimatedFlightMinute(mins);
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                    flightSchedule.setFlightSchedulePlan(flightSchedulePlan);

                    for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                        CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                        int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                        SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                        flightSchedule.getSeatInventories().add(seatInventory);
                        seatInventory.setFlightSchedule(flightSchedule);
                        cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                    }

                    if (complementary) {
                        FlightSchedule complementaryFS = new FlightSchedule();
                        complementaryDepartingCalendar.add(GregorianCalendar.HOUR, 4);
                        complementaryArrivingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalTime(complementaryArrivingCalendar);
                        complementaryFS.setDepartureTime(complementaryDepartingCalendar);
                        formattedDate = fsd.format(complementaryDepartingCalendar.getTime());
                        complementaryFS.setDepartureDate(formattedDate);
                        complementaryFS.setEstimatedFlightHour(hours);
                        complementaryFS.setEstimatedFlightMinute(mins);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFS.setFlightSchedulePlan(complementaryFSP);

                        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                            CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                            int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                            SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                            complementaryFS.getSeatInventories().add(seatInventory);
                            seatInventory.setFlightSchedule(complementaryFS);
                            cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                        }
                    }
                }

            } else if (res == 4) {
                complementaryFSP.setScheduleType(ScheduleTypeEnum.RECURRENT);
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.RECURRENT);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter effective date (ddMMyyyy) >");
                Date date = sdf.parse(scanner.nextLine().trim());
                cal1.setTime(date);
                System.out.println("Enter end date (ddMMyyyy) >");
                date = sdf.parse(scanner.nextLine().trim());
                cal2.setTime(date);
                int daysBetween = daysBetween(cal1.getTime(), cal2.getTime()) + 1;
                System.out.print("Enter Departure Date (yyyy-mm-dd HH:MM)> ");
                String date1 = scanner.nextLine().trim();
                departureDate = inputDateFormat.parse(date1);
                flightSchedulePlan.setFirstDepartureDate(date1);
                complementaryFSP.setFirstDepartureDate(date1);
                Calendar counterCal = new GregorianCalendar();
                counterCal.setTime(departureDate);

                for (int i = 0; i < daysBetween; i = i + 7) {
                    Calendar departingCalendar = new GregorianCalendar();
                    Calendar arrivingCalendar = new GregorianCalendar();
                    Calendar complementaryDepartingCalendar = new GregorianCalendar();
                    Calendar complementaryArrivingCalendar = new GregorianCalendar();
                    departingCalendar.setTime(departureDate);
                    complementaryArrivingCalendar.setTime(departureDate);
                    arrivingCalendar.setTime(departureDate);
                    complementaryDepartingCalendar.setTime(departureDate);
                    counterCal.add(Calendar.DATE, 7);
                    String str = inputDateFormat.format(counterCal.getTime());
                    departureDate = inputDateFormat.parse(str);
                    arrivingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                    arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    complementaryDepartingCalendar.add(GregorianCalendar.HOUR, hours);
                    complementaryArrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryDepartingCalendar.add(GregorianCalendar.MINUTE, mins);
                    complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    FlightSchedule flightSchedule = new FlightSchedule();
                    flightSchedule.setDepartureTime(departingCalendar);
                    String formattedDate = fsd.format(departingCalendar.getTime());
                    flightSchedule.setDepartureDate(formattedDate);
                    flightSchedule.setArrivalTime(arrivingCalendar);
                    flightSchedule.setEstimatedFlightHour(hours);
                    flightSchedule.setEstimatedFlightMinute(mins);
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                    flightSchedule.setFlightSchedulePlan(flightSchedulePlan);

                    for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                        CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                        int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                        SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                        flightSchedule.getSeatInventories().add(seatInventory);
                        seatInventory.setFlightSchedule(flightSchedule);
                        cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                    }

                    if (complementary) {
                        FlightSchedule complementaryFS = new FlightSchedule();
                        complementaryDepartingCalendar.add(GregorianCalendar.HOUR, 4);
                        complementaryArrivingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        complementaryArrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalTime(complementaryArrivingCalendar);
                        complementaryFS.setDepartureTime(complementaryDepartingCalendar);
                        formattedDate = fsd.format(complementaryDepartingCalendar.getTime());
                        complementaryFS.setDepartureDate(formattedDate);
                        complementaryFS.setEstimatedFlightHour(hours);
                        complementaryFS.setEstimatedFlightMinute(mins);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFS.setFlightSchedulePlan(complementaryFSP);

                        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                            CabinClassEnum cabinClassEnum = cabinClassConfiguration.getCabinClass();
                            int maxCabinSeatCapacity = cabinClassConfiguration.getMaxCabinSeatCapacity();
                            SeatInventory seatInventory = new SeatInventory(maxCabinSeatCapacity, cabinClassEnum);
                            complementaryFS.getSeatInventories().add(seatInventory);
                            seatInventory.setFlightSchedule(complementaryFS);
                            cabinClassConfiguration.getSeatInventories().add(seatInventory);
                    seatInventory.setCabinClassConfiguration(cabinClassConfiguration);
                        }
                    }
                }

            }

            for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                List<Fare> fares = cabinClassConfiguration.getFares();
                System.out.println("Enter number of fares for " + cabinClassConfiguration.getCabinClass() + " Class >");
                int numFares = scanner.nextInt();
                scanner.nextLine();
                for (int i = 0; i < numFares; i++) {
                    Fare fare = new Fare();
                    fare.setCabinClass(cabinClassConfiguration.getCabinClass());
                    System.out.println("Enter fare basis code for fare " + (i + 1) + " of " + cabinClassConfiguration.getCabinClass() + " > ");
                    String fbc = scanner.nextLine().trim();
                    fare.setFareBasisCode(fbc);
                    System.out.println("Enter fare amount> ");
                    double amt = scanner.nextDouble();
                    scanner.nextLine();
                    fare.setFareAmount(amt);
                    cabinClassConfiguration.getFares().add(fare);
                    flightSchedulePlan.getFares().add(fare);
                    fare.setCabinClassConfiguration(cabinClassConfiguration);
                    fare.setFlightSchedulePlan(flightSchedulePlan);

                }
            }

            if (complementary) {
                flightSchedulePlan.setComplementaryRFSP(true);
                List<CabinClassConfiguration> cabinClassConfigurationsCom = complementaryFSP.getFlight().getCabinClassConfiguration();
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurationsCom) {
                    List<Fare> fares = cabinClassConfiguration.getFares();
                    System.out.println("Enter number of fares for complementary" + cabinClassConfiguration.getCabinClass() + " Class >");
                    int numFares = scanner.nextInt();
                    scanner.nextLine();
                    for (int i = 0; i < numFares; i++) {
                        Fare fare = new Fare();
                        System.out.println("Enter details for fare " + (i + 1));
                        fare.setCabinClass(cabinClassConfiguration.getCabinClass());
                        System.out.println("Enter fare basis code> ");
                        String fbc = scanner.nextLine().trim();
                        fare.setFareBasisCode(fbc);
                        System.out.println("Enter fare amount> ");
                        double amt = scanner.nextDouble();
                        scanner.nextLine();
                        fare.setFareAmount(amt);
                        cabinClassConfiguration.getFares().add(fare);
                        complementaryFSP.getFares().add(fare);
                        fare.setCabinClassConfiguration(cabinClassConfiguration);
                        fare.setFlightSchedulePlan(complementaryFSP);
                    }
                }
            }

            Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(flightSchedulePlan);
            if (constraintViolations.isEmpty()) {
                Long comFspId = -1L;
                if (complementary) {
                    try {
                        comFspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(complementaryFSP);
                        System.out.println("New complementary fsp created successfully!: " + comFspId + "\n");
                        flightSchedulePlan.setComplementaryID(comFspId);
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }
                }

                try {
                    Long fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(flightSchedulePlan);
                    System.out.println("New fsp created successfully!: " + fspId + "\n");

                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else {
                showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void doViewAllFlightSchedulePlans() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS :: Flight Operation Module :: View All Flight Schedule Plans ***\n");

        List<FlightSchedulePlan> flightSchedulePlans = flightSchedulePlanSessionBeanRemote.viewAllFlightSchedulePlans();

        //Comparator<FlightSchedulePlan> comparator = Comparator.comparing(fsp -> fsp.getFlightNumber());
        // comparator = comparator.reversed().thenComparing(Comparator.comparing(fsp -> fsp.getFirstDepartureDate()));
        //Stream<FlightSchedulePlan> fspStream = flightSchedulePlans.stream().sorted(comparator);
        Collections.sort(flightSchedulePlans, Comparator.comparing(FlightSchedulePlan::getFlightNumber)
                .thenComparing(FlightSchedulePlan::getFirstDepartureDate));
        System.out.printf("%30s%30s%30s\n", "Flight Number", "First departure date", "ID");
        int size = flightSchedulePlans.size();
        for (int i = 0; i < size; i++) {
            if (flightSchedulePlans.isEmpty()) {
                break;
            }
            FlightSchedulePlan flightSchedulePlan = flightSchedulePlans.get(0);
            flightSchedulePlans.remove(flightSchedulePlan);

            System.out.printf("%30s%30s%30s\n", flightSchedulePlan.getFlightNumber(), flightSchedulePlan.getFirstDepartureDate(), flightSchedulePlan.getFlightSchedulePlanId());
            /*
            if (flightSchedulePlan.getComplementaryID() > 0) {
                FlightSchedulePlan complementary = new FlightSchedulePlan();

                complementary = flightSchedulePlanSessionBeanRemote.retrieveFlightSchedulePlanById(flightSchedulePlan.getComplementaryID());
                System.out.printf("%30s%30s%30s\n", complementary.getFlightNumber(), complementary.getFirstDepartureDate(), complementary.getFlightSchedulePlanId());
                flightSchedulePlans.remove(complementary);

            }
             */
            System.out.println("");
        }

    }

    public void doViewFlightSchedulePlanDetails() throws FlightSchedulePlanDoesNotExistException, InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** FRS :: Flight Operation Module :: View Flight Schedule Plan Details***\n");
        System.out.print("Enter Flight Schedule Plan ID> ");
        Long flightSchedulePlanId = scanner.nextLong();

   
            FlightSchedulePlan flightSchedulePlan = flightSchedulePlanSessionBeanRemote.viewFlightSchedulePlanDetails(flightSchedulePlanId);
            System.out.printf("%35s%35s\n", "Origin", "Destination");
            System.out.printf("%35s%35s\n", flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getCountry(), flightSchedulePlan.getFlight().getFlightRoute().getDestination().getCountry());
            System.out.println("");
            System.out.printf("%35s%35s%35s\n", "Flight Schedule ID", "Departing date/time", "Arriving date/time");
            for (FlightSchedule flightSchedule : flightSchedulePlan.getFlightSchedules()) {
                System.out.printf("%35s%35s%35s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureTime().getTime(), flightSchedule.getArrivalTime().getTime());
            }
            System.out.println("");
            System.out.printf("%35s%35s%35s\n", "Fare basis code", "Fare amount", "Cabin Class");
            for (Fare fare : flightSchedulePlan.getFares()) {
                System.out.printf("%35s%35s%35s\n", fare.getFareBasisCode(), fare.getFareAmount(), fare.getCabinClass());
            }
            System.out.println("------------------------");
            System.out.println("1: Update flight schedule plan");
            System.out.println("2: Delete flight schedule plan");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateFlightSchdulePlan(flightSchedulePlan);
            } else if (response == 2) {
                doDeleteFlightSchedulePlan(flightSchedulePlan);
            }

        
    }

    public void doUpdateFlightSchdulePlan(FlightSchedulePlan flightSchedulePlan) {
        Scanner scanner = new Scanner(System.in);
        String input;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/mm/yy HH:MM");
        Date departureDate;
        Flight flight = flightSchedulePlan.getFlight();

        Airport originAirport = flight.getFlightRoute().getOrigin();
        Airport destinationAirport = flight.getFlightRoute().getDestination();
        int timeDifference = originAirport.getGMT() - destinationAirport.getGMT();

        System.out.println("*** FRS :: Flight Operation Module :: View Flight Schedule Plan Details :: Update Flight Schedule plan ***\n");
        System.out.println("1: Add flight schedule");
        System.out.println("2: Delete flight schedule");
        System.out.println("3: Update fare");
        System.out.println(">");
        int res = scanner.nextInt();
        if (res == 1) {
            try {
                Calendar departingCalendar = new GregorianCalendar();
                Calendar arrivingCalendar = new GregorianCalendar();
                FlightSchedule flightSchedule = new FlightSchedule();
                System.out.print("Enter Departure Date (dd/mm/yy HH:MM)> ");
                departureDate = inputDateFormat.parse(scanner.nextLine().trim());
                departingCalendar.setTime(departureDate);
                arrivingCalendar.setTime(departureDate);
                arrivingCalendar.add(GregorianCalendar.HOUR, timeDifference);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                departingCalendar.add(GregorianCalendar.HOUR, hours);
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                departingCalendar.add(GregorianCalendar.MINUTE, mins);

                flightSchedule.setEstimatedFlightMinute(mins);
                flightSchedule.setEstimatedFlightHour(hours);
                flightSchedule.setDepartureTime(departingCalendar);
                flightSchedule.setArrivalTime(arrivingCalendar);
                flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            } catch (ParseException ex) {
                System.out.println("Invalid date input!\n");
            }

        } else if (res == 2) {
            List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
            System.out.println("Enter ID of flight schedule to be removed> ");
            long id = scanner.nextLong();
            for (FlightSchedule flightSchedule : flightSchedules) {
                if (flightSchedule.getFlightScheduleId() == id) {
                    if (flightSchedule.getFlightReservations().size() == 0) {
                        flightSchedulePlan.getFlightSchedules().remove(flightSchedule);
                    } else {
                        System.out.println("Flight tickets have been issued for this flight schedule, cannot delete.");
                    }
                }
            }

        } else if (res == 3) {
            List<Fare> fares = new ArrayList<>();
            System.out.println("Select cabin class for fare update: ");
            System.out.println("1: First class");
            System.out.println("2: Business class");
            System.out.println("3: Premium economy");
            System.out.println("4: Economy");
            System.out.println("> ");
            int response = scanner.nextInt();
            System.out.println("Enter number of fares for selected class");
            int numFares = scanner.nextInt();
            for (int i = 0; i < numFares; i++) {
                Fare fare = new Fare();
                System.out.println("Enter fare basis code> ");
                String fbc = scanner.nextLine().trim();
                fare.setFareBasisCode(fbc);
                System.out.println("Enter fare amount> ");
                double amt = scanner.nextDouble();
                fare.setFareAmount(amt);
                fares.add(fare);
            }
            List<CabinClassConfiguration> cabinClassConfigurations = flightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassConfigurations();
            if (res == 1) {
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                    if (cabinClassConfiguration.getCabinClass() == CabinClassEnum.FIRSTCLASS) {
                        cabinClassConfiguration.setFares(fares);
                    }
                }
            } else if (res == 2) {
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                    if (cabinClassConfiguration.getCabinClass() == CabinClassEnum.BUSINESS) {
                        cabinClassConfiguration.setFares(fares);
                    }
                }
            } else if (res == 3) {
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                    if (cabinClassConfiguration.getCabinClass() == CabinClassEnum.PREMIUMECONOMY) {
                        cabinClassConfiguration.setFares(fares);
                    }
                }
            } else if (res == 4) {
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                    if (cabinClassConfiguration.getCabinClass() == CabinClassEnum.ECONOMY) {
                        cabinClassConfiguration.setFares(fares);
                    }
                }
            }
        }

        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(flightSchedulePlan);

        if (constraintViolations.isEmpty()) {
            try {
                flightSchedulePlanSessionBeanRemote.updateFlightSchedulePlan(flightSchedulePlan);
                System.out.println("Flight updated successfully!\n");
            } catch (FlightScheduleContainsReservationException | FlightSchedulePlanDoesNotExistException ex) {
                System.out.println("An error has occurred while updating flight schedule plan: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
        }
    }

    public void doDeleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        Scanner scanner = new Scanner(System.in);
        String input;
        Long fspId = flightSchedulePlan.getFlightSchedulePlanId();

        System.out.println("*** FRS :: Flight Operation Module :: View Flight Schedule plan Details :: Delete Flight Schedule plan***\n");
        System.out.printf("Confirm Delete Flight Schedule plan(Flight Schedule Plan Id: %d) (Enter 'Y' to Delete)> ", fspId);
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                flightSchedulePlanSessionBeanRemote.deleteFlightSchedulePlan(fspId);
                System.out.println("Flight Schedule Plan deleted successfully!\n");
            } catch (FlightSchedulePlanDoesNotExistException ex) {
                System.out.println("An error has occurred while deleting the flight: " + ex.getMessage() + "\n");
            } catch (FlightSchedulePlanDeleteException ex) {
                System.out.println("Flight schdule plan is used, cannot be deleted, marked as disabled instead.");
            } catch (Exception ex) {
                System.out.println("Error occured while deleting flight schedule plan.");
            }
        } else {
            System.out.println("Flight NOT deleted!\n");
        }
    }

    public void doCreateFlight() throws FlightNumberExistException {
        Scanner scanner = new Scanner(System.in);
        int res = 0;
        System.out.println("*** FRS ::  Flight Operation Module :: Create Flight ***\n");
        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                throw new FlightNumberExistException("Flight number already exist in database!");
            }
        }
        Flight flight = new Flight(flightNumber);
        System.out.print("Enter Aircraft Configuration Name> ");
        String aircraftConfigName = scanner.nextLine().trim();
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(aircraftConfigName);
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight.setAircraftConfiguration(aircraftConfiguration);
        aircraftConfiguration.getFlights().add(flight);
        List<CabinClassConfiguration> cabinClassConfigurations = cabinClassConfigurationSessionBeanRemote.retrieveCabinClassConfigurationsByAircraftConfiguration(aircraftConfigName);

        List<CabinClassConfiguration> cabinClassConfigurations1 = new ArrayList<>();
        List<CabinClassEnum> cabinClassEnums = new ArrayList<>();
        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
            if (!cabinClassEnums.contains(cabinClassConfiguration.getCabinClass())) {
                cabinClassConfigurations1.add(cabinClassConfiguration);
            }
            cabinClassEnums.add(cabinClassConfiguration.getCabinClass());
        }
        for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations1) {
            cabinClassConfiguration.getFlights().add(flight);
            flight.getCabinClassConfiguration().add(cabinClassConfiguration);
        }
        System.out.println("Enter Origin Airport Code> ");
        String originAirportCode = scanner.nextLine().trim();
        Airport origin = new Airport();
        try {
            origin = airportSessionBeanRemote.retrieveAirportByAirportCode(originAirportCode);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(FlightPlanningModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Enter destination airport code> ");
        String destinationAirportCode = scanner.nextLine().trim();
        Airport destination = new Airport();
        try {
            destination = airportSessionBeanRemote.retrieveAirportByAirportCode(destinationAirportCode);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(FlightPlanningModule.class.getName()).log(Level.SEVERE, null, ex);
        }

        FlightRoute flightRoute = new FlightRoute();

        try {
            flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOD(origin, destination);
        } catch (FlightRouteDoesNotExistException ex) {
            Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight.setFlightRoute(flightRoute);
        flightRoute.getFlights().add(flight);
        FlightRoute complementaryflightRoute = new FlightRoute();
        Flight complementary = new Flight();
        if (flightRoute.getComplementary()) {
            System.out.println("Create complementary Flight? >  ");
            System.out.println("1: Yes");
            System.out.println("2: No");
            res = scanner.nextInt();
            scanner.nextLine();

            if (res == 1) {
                System.out.println("Enter Complementary flight number> ");
                String fN = scanner.nextLine().trim();
                complementary.setFlightNumber(fN);
                flight.setComplementaryFlightNumber(fN);
                try {
                    complementaryflightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOD(destination, origin);
                } catch (FlightRouteDoesNotExistException ex) {
                    Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
                }
                complementary.setFlightRoute(complementaryflightRoute);
                complementaryflightRoute.getFlights().add(complementary);
                complementary.setAircraftConfiguration(aircraftConfiguration);
                aircraftConfiguration.getFlights().add(complementary);
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations1) {
                    cabinClassConfiguration.getFlights().add(complementary);
                    complementary.getCabinClassConfiguration().add(cabinClassConfiguration);
                }

            }
        }

        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);

        if (constraintViolations.isEmpty()) {
            try {
                Long flightId = flightSessionBeanRemote.createNewFlight(flight);
                System.out.println("New Flight created successfully!: " + flightId + "\n");
            } catch (FlightNumberExistException ex) {
                System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            if (res == 1) {
                try {
                    Long flightId = flightSessionBeanRemote.createNewFlight(complementary);
                    System.out.println("New Flight created successfully!: " + flightId + "\n");
                } catch (FlightNumberExistException ex) {
                    System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
                } catch (UnknownPersistenceException ex) {
                    System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            }
        } else {
            showInputDataValidationErrorsForFlight(constraintViolations);
        }
    }

    private void doViewAllFlights() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS :: Flight Operation Module :: View All Flights ***\n");

        List<Flight> flights = flightSessionBeanRemote.retrieveAllFlights();
        List<String> flightNumbers = new ArrayList<>();
        for (Flight flight : flights) {
            String flightNumber = flight.getFlightNumber();
            flightNumbers.add(flightNumber);
        }
        Collections.sort(flightNumbers);
        System.out.printf("%20s%20s%20s%20s\n", "Flight Number", "Origin", "Destination", "Aircraft Config");
        int size = flightNumbers.size();
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            if (flightNumbers.isEmpty()) {
                break;
            }
            String flightNumber = flightNumbers.remove(0);
            Flight flight = new Flight();
            try {
                flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            } catch (FlightNumberExistException ex) {
                Logger.getLogger(FlightPlanningModule.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            System.out.printf("%20s%20s%20s%20s\n", flight.getFlightNumber(), flight.getFlightRoute().getOrigin().getName(), flight.getFlightRoute().getDestination().getName(), flight.getAircraftConfiguration().getName());
            if (flight.getComplementaryFlightNumber().length() > 0) {
                Flight complementary = new Flight();
                try {
                    complementary = flightSessionBeanRemote.retrieveFlightByFlightNumber(flight.getComplementaryFlightNumber());
                } catch (FlightNumberExistException ex) {
                    System.out.println("Error getting complementary flight");
                }
                System.out.printf("%20s%20s%20s%20s\n", complementary.getFlightNumber(), complementary.getFlightRoute().getOrigin().getName(), complementary.getFlightRoute().getDestination().getName(), complementary.getAircraftConfiguration().getName());
                flightNumbers.remove(flight.getComplementaryFlightNumber());

            }
        }
    }

    private void doViewFlightDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** FRS :: Flight Operation Module :: View Flight Details***\n");
        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();

        try {
            Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            System.out.printf("%40s%40s%40s%40s\n", "Flight Number", "Origin", "Destination", "Aircraft Config");
            System.out.printf("%40s%40s%40s%40s\n", flight.getFlightNumber(), flight.getFlightRoute().getOrigin().getName(), flight.getFlightRoute().getDestination().getName(), flight.getAircraftConfiguration().getName());
            System.out.println("");
            System.out.printf("%40s%40s\n", "Cabin class", "Number of seats");
            AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();
            try {
                aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(flight.getAircraftConfiguration().getName());
            } catch (AircraftConfigurationNotFoundException ex) {
                System.out.println("Aircraft config not found.");
            }
            List<CabinClassConfiguration> cabinClassConfigurations = flight.getCabinClassConfiguration();

            for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {

                System.out.printf("%40s%40s\n", cabinClassConfiguration.getCabinClass(), cabinClassConfiguration.getMaxCabinSeatCapacity());
            }
            System.out.println("------------------------");
            System.out.println("1: Update flight");
            System.out.println("2: Delete flight");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            scanner.nextLine();

            if (response == 1) {
                doUpdateFlight(flight);
            } else if (response == 2) {
                doDeleteFlight(flight);
            }

        } catch (FlightNumberExistException ex) {
            System.out.println("An error has occurred while retrieving flight: " + ex.getMessage() + "\n");
        }
    }

    private void doDeleteFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);
        String input;
        String flightNumber = flight.getFlightNumber();

        System.out.println("*** FRS :: Flight Operation Module :: View Flight Details :: Delete Flight***\n");
        System.out.printf("Confirm Delete Flight %s %s %s (Flight Id: %d) (Enter 'Y' to Delete)> ", flight.getFlightNumber(), flight.getFlightRoute().getOrigin(), flight.getFlightRoute().getDestination(), flight.getFlightId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                flightSessionBeanRemote.deleteFlight(flightNumber);
                System.out.println("Flight deleted successfully!\n");
            } catch (FlightNumberExistException ex) {
                System.out.println("An error has occurred while deleting the flight: " + ex.getMessage() + "\n");
            } catch (FlightDeleteException ex) {
                System.out.println("Flight is used, cannot be deleted. Flight marked as disabled instead.");
            }
        } else {
            System.out.println("Flight NOT deleted!\n");
        }
    }

    private void doUpdateFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("*** FRS :: Flight Operation Module :: View Flight Details :: Update Flight ***\n");
        System.out.print("Enter new Flight Number (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            flight.setFlightNumber(input);
        }

        System.out.print("Enter name of new Aircraft Configuration (blank if no change)> ");
        input = scanner.nextLine().trim();
        String aircraftConfigName = flight.getAircraftConfiguration().getName();

        if (input.length() > 0) {
            AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();
            try {
                aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(aircraftConfigName);
            } catch (AircraftConfigurationNotFoundException ex) {
                System.out.println("Original aircraft configuration not found");
            }
            aircraftConfiguration.getFlights().remove(flight);
            try {
                aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(input);
            } catch (AircraftConfigurationNotFoundException ex) {
                Logger.getLogger(FlightOperationModule.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            flight.setAircraftConfiguration(aircraftConfiguration);
            aircraftConfiguration.getFlights().add(flight);
        }

        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);

        if (constraintViolations.isEmpty()) {
            try {
                flightSessionBeanRemote.updateFlight(flight);
                System.out.println("Flight updated successfully!\n");
            } catch (FlightNotFoundException | UpdateFlightException ex) {
                System.out.println("An error has occurred while updating flight: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForFlight(constraintViolations);
        }

    }

    private void showInputDataValidationErrorsForFlight(Set<ConstraintViolation<Flight>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForFlightSchedulePlan(Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
