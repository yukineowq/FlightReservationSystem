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
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.AircraftConfiguration;
import entity.FlightSchedulePlan;
import entity.FlightSchedule;
import entity.Airport;
import entity.Fare;
import entity.CabinClassConfiguration;
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
import java.util.TimeZone;

/**
 *
 * @author reuben
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

    public FlightOperationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public FlightOperationModule(Employee currentEmployee, FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
    }

    public void menuFlightPlanningFlightRoute() throws InvalidAccessRightException {
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
                    doCreateFlight();
                } else if (response == 2) {
                    doViewAllFlights();
                } else if (response == 3) {
                    doViewFlightDetails();
                } else if (response == 4) {
                    doCreateFlightSchedulePlan();
                } else if (response == 5) {
                    doViewAllFlightSchedulePlans();
                } else if (response == 6) {
                    doViewFlightSchedulePlanDetails();
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

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/mm/yy HH:MM");
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
            Airport originAirport = flight.getFlightRoute().getOrigin();
            Airport destinationAirport = flight.getFlightRoute().getDestination();

            System.out.println("1: Single schedule");
            System.out.println("2: Multiple schedule");
            System.out.println("3: Recurrent schedules after n day");
            System.out.println("4: Recurrent schedules every week");
            System.out.println("Select flight schedule plan type> ");
            int res = scanner.nextInt();
            if (res == 1) {
                GregorianCalendar departingCalendar = new GregorianCalendar(TimeZone.getTimeZone(originAirport.getGMT()));
                GregorianCalendar arrivingCalendar = new GregorianCalendar(TimeZone.getTimeZone(destinationAirport.getGMT()));
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.SINGLE);
                FlightSchedule flightSchedule = new FlightSchedule();
                System.out.print("Enter Departure Date (dd/mm/yy HH:MM)> ");
                departureDate = inputDateFormat.parse(scanner.nextLine().trim());
                departingCalendar.setTime(departureDate);
                arrivingCalendar.setTime(departureDate);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                departingCalendar.add(GregorianCalendar.HOUR, hours);
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                departingCalendar.add(GregorianCalendar.MINUTE, mins);
                flightSchedule.setArrivalDate(arrivingCalendar.getTime());
                flightSchedule.setDepartureDate(departingCalendar.getTime());
                flightSchedule.setArrivalTime(arrivingCalendar);
                flightSchedulePlan.getFlightSchedules().add(flightSchedule);

                if (flight.getComplementary() != null) {
                    System.out.println("Would you like to create a complementary return flight schedule plan? Y/N >");
                    String response = scanner.nextLine().trim();
                    if (response.equals("Y")) {
                        complementary = true;
                        FlightSchedule complementaryFS = new FlightSchedule();
                        arrivingCalendar.add(GregorianCalendar.HOUR, 4);
                        departingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        departingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalDate(departingCalendar.getTime());
                        complementaryFS.setDepartureDate(arrivingCalendar.getTime());
                        complementaryFS.setArrivalTime(departingCalendar);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFSP.setFlight(flight.getComplementary());
                    }
                }
            } else if (res == 2) {
                GregorianCalendar departingCalendar = new GregorianCalendar(TimeZone.getTimeZone(originAirport.getGMT()));
                GregorianCalendar arrivingCalendar = new GregorianCalendar(TimeZone.getTimeZone(destinationAirport.getGMT()));
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                System.out.println("Enter number of schedules> ");
                int numSchedules = scanner.nextInt();
                for (int i = 0; i < numSchedules; i++) {
                    FlightSchedule flightSchedule = new FlightSchedule();
                    System.out.println("Creating flight schedule: " + i);
                    System.out.print("Enter Departure Date (dd/mm/yy HH:MM)> ");
                    departureDate = inputDateFormat.parse(scanner.nextLine().trim());
                    departingCalendar.setTime(departureDate);
                    arrivingCalendar.setTime(departureDate);
                    arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                    departingCalendar.add(GregorianCalendar.HOUR, hours);
                    arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                    departingCalendar.add(GregorianCalendar.MINUTE, mins);
                    flightSchedule.setArrivalDate(arrivingCalendar.getTime());
                    flightSchedule.setDepartureDate(departingCalendar.getTime());
                    flightSchedule.setArrivalTime(arrivingCalendar);
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                }
                if (flight.getComplementary() != null) {
                    System.out.println("Would you like to create a complementary return flight schedule plan? Y/N >");
                    String response = scanner.nextLine().trim();
                    if (response.equals("Y")) {
                        complementary = true;
                        FlightSchedule complementaryFS = new FlightSchedule();
                        arrivingCalendar.add(GregorianCalendar.HOUR, 4);
                        departingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        departingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalDate(departingCalendar.getTime());
                        complementaryFS.setDepartureDate(arrivingCalendar.getTime());
                        complementaryFS.setArrivalTime(departingCalendar);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFSP.setFlight(flight.getComplementary());
                    }
                }
            } else if (res == 3) {
                GregorianCalendar departingCalendar = new GregorianCalendar(TimeZone.getTimeZone(originAirport.getGMT()));
                GregorianCalendar arrivingCalendar = new GregorianCalendar(TimeZone.getTimeZone(destinationAirport.getGMT()));
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                System.out.println("Enter n> ");
                int n = scanner.nextInt();
                System.out.println("Enter effective date (ddMMyyyy) >");
                Date date = sdf.parse(scanner.nextLine().trim());
                cal1.setTime(date);
                System.out.println("Enter end date (ddMMyyyy) >");
                date = sdf.parse(scanner.nextLine().trim());
                cal2.setTime(date);
                int daysBetween = daysBetween(cal1.getTime(), cal2.getTime()) + 1;
                System.out.print("Enter Departure Date (dd/mm/yy HH:MM)> ");
                departureDate = inputDateFormat.parse(scanner.nextLine().trim());
                departingCalendar.setTime(departureDate);
                arrivingCalendar.setTime(departureDate);
                arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                departingCalendar.add(GregorianCalendar.HOUR, hours);
                arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                departingCalendar.add(GregorianCalendar.MINUTE, mins);
                if (flight.getComplementary() != null) {
                    System.out.println("Would you like to create a complementary return flight schedule plan? Y/N >");
                    String response = scanner.nextLine().trim();
                    if (response.equals("Y")) {
                        complementary = true;
                    }
                }
                for (int i = 0; i < daysBetween; i = i + n) {
                    FlightSchedule flightSchedule = new FlightSchedule();
                    arrivingCalendar.add(GregorianCalendar.DAY_OF_MONTH, i);
                    departingCalendar.add(GregorianCalendar.DAY_OF_MONTH, i);
                    flightSchedule.setArrivalDate(arrivingCalendar.getTime());
                    flightSchedule.setDepartureDate(departingCalendar.getTime());
                    flightSchedule.setArrivalTime(arrivingCalendar);
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                    if (complementary) {
                        FlightSchedule complementaryFS = new FlightSchedule();
                        arrivingCalendar.add(GregorianCalendar.HOUR, 4);
                        departingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        departingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalDate(departingCalendar.getTime());
                        complementaryFS.setDepartureDate(arrivingCalendar.getTime());
                        complementaryFS.setArrivalTime(departingCalendar);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFSP.setFlight(flight.getComplementary());
                    }
                }
            } else if (res == 4) {
                GregorianCalendar departingCalendar = new GregorianCalendar(TimeZone.getTimeZone(originAirport.getGMT()));
                GregorianCalendar arrivingCalendar = new GregorianCalendar(TimeZone.getTimeZone(destinationAirport.getGMT()));
                flightSchedulePlan.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                System.out.println("Enter estimated flight hours> ");
                int hours = scanner.nextInt();
                System.out.println("Enter estimated flight minutes> ");
                int mins = scanner.nextInt();
                System.out.println("Enter effective date (ddMMyyyy) >");
                Date date = sdf.parse(scanner.nextLine().trim());
                cal1.setTime(date);
                System.out.println("Enter end date (ddMMyyyy) >");
                date = sdf.parse(scanner.nextLine().trim());
                cal2.setTime(date);
                int daysBetween = daysBetween(cal1.getTime(), cal2.getTime()) + 1;
                System.out.print("Enter Departure Date (dd/mm/yy HH:MM)> ");
                departureDate = inputDateFormat.parse(scanner.nextLine().trim());
                departingCalendar.setTime(departureDate);
                arrivingCalendar.setTime(departureDate);
                arrivingCalendar.add(GregorianCalendar.HOUR, hours);
                departingCalendar.add(GregorianCalendar.HOUR, hours);
                arrivingCalendar.add(GregorianCalendar.MINUTE, mins);
                departingCalendar.add(GregorianCalendar.MINUTE, mins);
                if (flight.getComplementary() != null) {
                    System.out.println("Would you like to create a complementary return flight schedule plan? Y/N >");
                    String response = scanner.nextLine().trim();
                    if (response.equals("Y")) {
                        complementary = true;
                    }
                }
                for (int i = 0; i < daysBetween; i = i + 7) {
                    FlightSchedule flightSchedule = new FlightSchedule();
                    arrivingCalendar.add(GregorianCalendar.DAY_OF_MONTH, i);
                    departingCalendar.add(GregorianCalendar.DAY_OF_MONTH, i);
                    flightSchedule.setArrivalDate(arrivingCalendar.getTime());
                    flightSchedule.setDepartureDate(departingCalendar.getTime());
                    flightSchedule.setArrivalTime(arrivingCalendar);
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                    if (complementary) {
                        FlightSchedule complementaryFS = new FlightSchedule();
                        arrivingCalendar.add(GregorianCalendar.HOUR, 4);
                        departingCalendar.add(GregorianCalendar.HOUR, 4 + hours);
                        departingCalendar.add(GregorianCalendar.MINUTE, mins);
                        complementaryFS.setArrivalDate(departingCalendar.getTime());
                        complementaryFS.setDepartureDate(arrivingCalendar.getTime());
                        complementaryFS.setArrivalTime(departingCalendar);
                        complementaryFSP.getFlightSchedules().add(complementaryFS);
                        complementaryFSP.setFlight(flight.getComplementary());
                    }
                }
            }
            List<CabinClassConfiguration> cabinClassConfigurations = flightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassConfigurations();
            for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                List<Fare> fares = cabinClassConfiguration.getFares();
                System.out.println("Enter number of fares for " + cabinClassConfiguration.getCabinClass() + " Class >");
                int numFares = scanner.nextInt();
                for (int i = 0; i < numFares; i++) {
                    Fare fare = new Fare();
                    fare.setCabinClass(cabinClassConfiguration.getCabinClass());
                    System.out.println("Enter fare basis code> ");
                    String fbc = scanner.nextLine().trim();
                    fare.setFareBasisCode(fbc);
                    System.out.println("Enter fare amount> ");
                    double amt = scanner.nextDouble();
                    fare.setFareAmount(amt);
                    cabinClassConfiguration.getFares().add(fare);
                    flightSchedulePlan.getFares().add(fare);
                }
            }
            if (complementary) {
                List<CabinClassConfiguration> cabinClassConfigurationsCom = complementaryFSP.getFlight().getAircraftConfiguration().getCabinClassConfigurations();
                for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurationsCom) {
                    List<Fare> fares = cabinClassConfiguration.getFares();
                    System.out.println("Enter number of fares for " + cabinClassConfiguration.getCabinClass() + " Class >");
                    int numFares = scanner.nextInt();
                    for (int i = 0; i < numFares; i++) {
                        Fare fare = new Fare();
                        fare.setCabinClass(cabinClassConfiguration.getCabinClass());
                        System.out.println("Enter fare basis code> ");
                        String fbc = scanner.nextLine().trim();
                        fare.setFareBasisCode(fbc);
                        System.out.println("Enter fare amount> ");
                        double amt = scanner.nextDouble();
                        fare.setFareAmount(amt);
                        cabinClassConfiguration.getFares().add(fare);
                        complementaryFSP.getFares().add(fare);
                        flightSchedulePlan.setComplementary(complementaryFSP);
                    }
                }
            }
            Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(flightSchedulePlan);
            if (constraintViolations.isEmpty()) {
                try {
                    Long fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(flightSchedulePlan, flight.getFlightId());
                    System.out.println("New Aircraft configuration created successfully!: " + fspId + "\n");
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

    }

    public void doViewFlightSchedulePlanDetails() {

    }

    public void doCreateFlight() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS ::  Flight Operation Module :: Create Flight ***\n");
        System.out.print("Enter Flight Number> ");
        String flightNumber = scanner.nextLine().trim();
        System.out.print("Enter Aircraft Configuration Name> ");
        String aircraftConfigName = scanner.nextLine().trim();
        System.out.println("Enter OD Pair> ");
        String od = scanner.nextLine().trim();

        Flight flight = new Flight(flightNumber);

        System.out.println("Create complementary Flight? >  ");
        System.out.println("1: Yes");
        System.out.println("2: No");
        int res = scanner.nextInt();
        Flight complementary = new Flight();
        if (res == 1) {
            System.out.println("Enter Complementary flight number> ");
            complementary.setFlightNumber(scanner.nextLine().trim());
            flight.setComplementary(complementary);
        }

        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);

        if (constraintViolations.isEmpty()) {
            try {
                Long flightId = flightSessionBeanRemote.createNewFlight(flight, aircraftConfigName, od);
                System.out.println("New Flight created successfully!: " + flightId + "\n");

            } catch (FlightNumberExistException ex) {
                System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForStaffEntity(constraintViolations);
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
        System.out.printf("%20s%20s%20s\n", "Flight Number", "Flight Route", "Aircraft Config");
        for (String flightNumber : flightNumbers) {
            Flight flight = new Flight();
            try {
                flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
            } catch (FlightNumberExistException ex) {
                Logger.getLogger(FlightPlanningModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.printf("%20s%20s%20s\n", flight.getFlightNumber(), flight.getFlightRoute(), flight.getAircraftConfiguration().getName());
            if (flight.getComplementary() != null) {
                System.out.printf("%20s%20s%20s\n", flight.getComplementary().getFlightNumber(), flight.getComplementary().getFlightRoute(), flight.getComplementary().getAircraftConfiguration().getName());
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
            System.out.printf("%20s%20s%20s\n", "Flight Number", "Flight Route", "Aircraft Config");
            System.out.printf("%20s%20s%20s\n", flight.getFlightNumber(), flight.getFlightRoute(), flight.getAircraftConfiguration().getName());
            System.out.println("------------------------");
            System.out.println("1: Update flight");
            System.out.println("2: Delete flight");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

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
        System.out.printf("Confirm Delete Flight %s %s (Flight Id: %d) (Enter 'Y' to Delete)> ", flight.getFlightNumber(), flight.getFlightRoute().getOD(), flight.getFlightId());
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

        System.out.print("Enter new Flight Route OD pair (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            FlightRoute flightRoute = flight.getFlightRoute();
            flightRoute.getFlights().remove(flight);
            try {
                flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOD(input);
            } catch (FlightRouteDoesNotExistException ex) {
                Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
            }

            flight.setFlightRoute(flightRoute);
            flightRoute.getFlights().add(flight);
        }
        System.out.print("Enter name of new Aircraft Configuration (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            AircraftConfiguration aircraftConfiguration = flight.getAircraftConfiguration();
            aircraftConfiguration.getFlights().remove(flight);
            try {
                aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(input);
            } catch (AircraftConfigurationNotFoundException ex) {
                Logger.getLogger(FlightOperationModule.class.getName()).log(Level.SEVERE, null, ex);
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
                System.out.println("An error has occurred while updating staff: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForStaffEntity(constraintViolations);
        }

    }

    private void showInputDataValidationErrorsForStaffEntity(Set<ConstraintViolation<Flight>> constraintViolations) {
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
