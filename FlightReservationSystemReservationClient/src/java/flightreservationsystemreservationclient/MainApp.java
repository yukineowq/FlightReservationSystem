/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemreservationclient;

import ejb.session.stateless.ReserveFlightSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import entity.Airport;
import entity.Customer;
import entity.Fare;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassEnum;
import util.enumeration.PreferenceEnum;
import util.exception.AirportNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
public class MainApp {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private Customer customer;

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;

    private CustomerOperationModule customerOperationModule;

    public MainApp() {
        
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.reserveFlightSessionBeanRemote = reserveFlightSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;

    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Customer Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search flights");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        customerOperationModule = new CustomerOperationModule(customer, flightSessionBeanRemote, reserveFlightSessionBeanRemote, flightReservationSessionBeanRemote);
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    register();
                } else if (response == 3) {
                    search();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    public void search() {
        Scanner scanner = new Scanner(System.in);
        Airport originAirport = new Airport();
        Airport destinationAirport = new Airport();
        String departureDate = "";
        String returnDate = "";
        CabinClassEnum cabinClass = CabinClassEnum.NA;
        PreferenceEnum preference = PreferenceEnum.NA;
        int numPassenger = 1;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("Select trip type: ");
        System.out.println("1: One-way");
        System.out.println("2: Round-trip");
        int res = 0;
        int trip = 0;
        while (trip < 1 || trip > 2) {
            System.out.print("> ");
            trip = scanner.nextInt();
            scanner.nextLine();
        }
        if (trip == 1) {
            System.out.println("Enter departure airport code> ");
            try {
                originAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(scanner.nextLine().trim());
            } catch (AirportNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Enter destination airport code> ");
            try {
                destinationAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(scanner.nextLine().trim());
            } catch (AirportNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Enter departure date (yyyy-mm-dd)> ");
          
                departureDate = scanner.nextLine().trim();
           
            System.out.println("Enter number of passengers> ");
            numPassenger = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Select preference for flights: ");
            System.out.println("1: Direct");
            System.out.println("2: Connecting");
            System.out.println("3: No preference");
            int res1 = 0;
            while (res1 < 1 || res1 > 3) {
                System.out.print("> ");
                res1 = scanner.nextInt();
                scanner.nextLine();
            }
            if (res1 == 1) {
                preference = PreferenceEnum.DIRECT;
            } else if (res1 == 2) {
                preference = PreferenceEnum.CONNECTING;
            } else {
                preference = PreferenceEnum.NA;
            }
            System.out.println("Select preference for cabin class: ");
            System.out.println("1: First class");
            System.out.println("2: Business");
            System.out.println("3: Premium economy");
            System.out.println("4: Economy");
            System.out.println("5: No preference");
            int res2 = 0;
            while (res2 < 1 || res2 > 5) {
                System.out.print("> ");
                res2 = scanner.nextInt();
                scanner.nextLine();
            }
            if (res2 == 1) {
                cabinClass = CabinClassEnum.FIRSTCLASS;
            } else if (res2 == 2) {
                cabinClass = CabinClassEnum.BUSINESS;
            } else if (res2 == 3) {
                cabinClass = CabinClassEnum.PREMIUMECONOMY;
            } else if (res2 == 4) {
                cabinClass = CabinClassEnum.ECONOMY;
            } else {
                cabinClass = CabinClassEnum.NA;
            }
        } else if (trip == 2) {
            System.out.println("Enter Origin Airport Code> ");
            String originAirportCode = scanner.nextLine().trim();

            try {
                originAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(originAirportCode);
            } catch (AirportNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Enter destination airport code> ");
            String destinationAirportCode = scanner.nextLine().trim();

            try {
                destinationAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(destinationAirportCode);
            } catch (AirportNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Enter departure date (yyyy-mm-dd)> ");
        
                departureDate = scanner.nextLine().trim();
          
            System.out.println("Enter return date (yyyy-mm-dd)> ");
           
                returnDate = scanner.nextLine().trim();
            
            System.out.println("Enter number of passengers> ");
            numPassenger = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Select preference for flights: ");
            System.out.println("1: Direct");
            System.out.println("2: Connecting");
            System.out.println("3: No preference");
            int res1 = 0;
            while (res1 < 1 || res1 > 3) {
                System.out.print("> ");
                res1 = scanner.nextInt();
                scanner.nextLine();
            }
            if (res1 == 1) {
                preference = PreferenceEnum.DIRECT;
            } else if (res1 == 2) {
                preference = PreferenceEnum.CONNECTING;
            } else {
                preference = PreferenceEnum.NA;
            }
            System.out.println("Select preference for cabin class: ");
            System.out.println("1: First class");
            System.out.println("2: Business");
            System.out.println("3: Premium economy");
            System.out.println("4: Economy");
            System.out.println("5: No preference");
            int res2 = 0;
            while (res2 < 1 || res2 > 5) {
                System.out.print("> ");
                res2 = scanner.nextInt();
                scanner.nextLine();
            }
            if (res2 == 1) {
                cabinClass = CabinClassEnum.FIRSTCLASS;
            } else if (res2 == 2) {
                cabinClass = CabinClassEnum.BUSINESS;
            } else if (res2 == 3) {
                cabinClass = CabinClassEnum.PREMIUMECONOMY;
            } else if (res2 == 4) {
                cabinClass = CabinClassEnum.ECONOMY;
            } else {
                cabinClass = CabinClassEnum.NA;
            }

        }
        List<List<FlightSchedule>> flightSchedules = flightScheduleSessionBeanRemote.searchFlightSchedule(originAirport, destinationAirport, departureDate, numPassenger, preference, cabinClass);
        System.out.printf("%30s%30s%30s\n", "Flight Schedule ID", "Departure time", "Arrival Time");

        for (List<FlightSchedule> flightSchedulesList : flightSchedules) {
            System.out.println("");
            for (int i = 0; i < flightSchedulesList.size(); i++) {
                boolean connecting = false;
                FlightSchedule flightSchedule = flightSchedulesList.get(i);
                //if destination is not the same, means current flight schedule is a connecting flight, and the next flight schedule will be the flight containing the destination
                if (!flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportCode().equals(destinationAirport.getAirportCode())) {
                    connecting = true;
                }
                if (!connecting) {
                    System.out.printf("%30s%30s%30s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureTime().getTime(), flightSchedule.getArrivalTime().getTime());
                    List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                    System.out.printf("%30s%30s%30s%30s\n", "Cabin class", "Available seats", "Reserved seats", "Balanced");
                    for (SeatInventory seatInventory : seatInventories) {
                        System.out.printf("%30s%30s%30s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
                    }
                    System.out.printf("%30s%30s%30s\n", "Cabin class", "Price per passenger", "Total price");
                    List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                    List<Fare> filteredFares = new ArrayList<>();
                    List<CabinClassEnum> cabinClassEnums = new ArrayList<>();

                    for (Fare fare : fares) {
                        if (!cabinClassEnums.contains(fare.getCabinClass())) {
                            filteredFares.add(fare);
                            cabinClassEnums.add(fare.getCabinClass());
                        } else {
                            for (Fare filteredFare : filteredFares) {
                                if (filteredFare.getCabinClass().equals(fare.getCabinClass())) {
                                    if (filteredFare.getFareAmount() > fare.getFareAmount()) {
                                        filteredFares.remove(filteredFare);
                                        filteredFares.add(fare);
                                    }
                                }
                            }
                        }
                    }
                    for (Fare fare : filteredFares) {
                        System.out.printf("%30s%30s%30s\n", fare.getCabinClass(), fare.getFareAmount(), fare.getFareAmount() * numPassenger);
                    }

                } else {
                    System.out.printf("%30s%30s%30s%30s%30s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureTime().getTime(), flightSchedule.getArrivalTime().getTime(), flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getCity(), flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getCity());
                    List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                    System.out.printf("%30s%30s%30s%30s\n", "Cabin class", "Available seats", "Reserved seats", "Balanced");
                    for (SeatInventory seatInventory : seatInventories) {
                        System.out.printf("%30s%30s%30s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
                    }

                    List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                    List<Fare> filteredFares = new ArrayList<>();
                    List<CabinClassEnum> cabinClassEnums = new ArrayList<>();

                    for (Fare fare : fares) {
                        if (!cabinClassEnums.contains(fare.getCabinClass())) {
                            filteredFares.add(fare);
                            cabinClassEnums.add(fare.getCabinClass());
                        } else {
                            for (Fare filteredFare : filteredFares) {
                                if (filteredFare.getCabinClass().equals(fare.getCabinClass())) {
                                    if (filteredFare.getFareAmount() > fare.getFareAmount()) {
                                        filteredFares.remove(filteredFare);
                                        filteredFares.add(fare);
                                    }
                                }
                            }
                        }
                    }

                    FlightSchedule flightSchedule2 = flightSchedulesList.get(i + 1);
                    i++;
                    System.out.printf("%30s%30s%30s%30s%30s\n", flightSchedule2.getFlightScheduleId(), flightSchedule2.getDepartureTime().getTime(), flightSchedule2.getArrivalTime().getTime(), flightSchedule2.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getCity(), flightSchedule2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getCity());
                    List<SeatInventory> seatInventories2 = flightSchedule2.getSeatInventories();
                    System.out.printf("%30s%30s%30s%30s\n", "Cabin class", "Available seats", "Reserved seats", "Balanced");
                    for (SeatInventory seatInventory : seatInventories2) {
                        System.out.printf("%30s%30s%30s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
                    }
                    System.out.println("");
                    List<Fare> fares2 = flightSchedule2.getFlightSchedulePlan().getFares();
                    List<Fare> filteredFares2 = new ArrayList<>();
                    List<CabinClassEnum> cabinClassEnums2 = new ArrayList<>();

                    for (Fare fare : fares) {
                        if (!cabinClassEnums2.contains(fare.getCabinClass())) {
                            filteredFares2.add(fare);
                            cabinClassEnums2.add(fare.getCabinClass());
                        } else {
                            for (Fare filteredFare : filteredFares2) {
                                if (filteredFare.getCabinClass().equals(fare.getCabinClass())) {
                                    if (filteredFare.getFareAmount() > fare.getFareAmount()) {
                                        filteredFares2.remove(filteredFare);
                                        filteredFares2.add(fare);
                                    }
                                }
                            }
                        }
                    }
                    List<Double> prices = new ArrayList<>();
                    for (Fare fare : filteredFares) {
                        for (Fare fare2 : filteredFares2) {
                            if (fare.getCabinClass().equals(fare2.getCabinClass())) {
                                prices.add(fare.getFareAmount() + fare2.getFareAmount());
                            }
                        }
                    }
                    System.out.println("");
                    System.out.println("                         ========================Fare=====================");
                    System.out.printf("%30s%30s%30s\n", "Cabin class", "Price per passenger", "Total price");
                    for (int index = 0; index < prices.size(); index++) {
                        System.out.printf("%30s%30s%30s\n", filteredFares.get(index).getCabinClass(), prices.get(index), prices.get(index) * numPassenger);
                    }
                }
            }
        }
        //Return trip
        if (trip == 2) {
            System.out.println("");
            System.out.println("==================Return Trip==================");
            System.out.println("");
            List<List<FlightSchedule>> flightSchedules2 = flightScheduleSessionBeanRemote.searchFlightSchedule(destinationAirport, originAirport, returnDate, numPassenger, preference, cabinClass);
            System.out.printf("%30s%30s%30s\n", "Flight Schedule ID", "Departure time", "Arrival Time");
            for (List<FlightSchedule> flightSchedulesList : flightSchedules2) {
                
                for (int i = 0; i < flightSchedulesList.size(); i++) {
                    System.out.println("");
                    boolean connecting = false;
                    FlightSchedule flightSchedule = flightSchedulesList.get(i);
                    //if destination is not the same, means current flight schedule is a connecting flight, and the next flight schedule will be the flight containing the destination
                    if (!flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportCode().equals(originAirport.getAirportCode())) {
                        connecting = true;
                    }

                    if (!connecting) {
                        System.out.printf("%30s%30s%30s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureTime().getTime(), flightSchedule.getArrivalTime().getTime());
                        List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                        System.out.printf("%30s%30s%30s%30s\n", "Cabin class", "Available seats", "Reserved seats", "Balanced");
                        for (SeatInventory seatInventory : seatInventories) {
                            System.out.printf("%30s%30s%30s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
                        }
                        System.out.printf("%30s%30s%30s\n", "Cabin class", "Price per passenger", "Total price");
                        List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                        List<Fare> filteredFares = new ArrayList<>();
                        List<CabinClassEnum> cabinClassEnums = new ArrayList<>();

                        for (Fare fare : fares) {
                            if (!cabinClassEnums.contains(fare.getCabinClass())) {
                                filteredFares.add(fare);
                                cabinClassEnums.add(fare.getCabinClass());
                            } else {
                                int fareSize = filteredFares.size();
                                for (int id = 0; id < fareSize; id++) {
                                    Fare filteredFare = filteredFares.get(id);
                                    if (filteredFare.getCabinClass().equals(fare.getCabinClass())) {
                                        if (filteredFare.getFareAmount() > fare.getFareAmount()) {
                                            filteredFares.remove(filteredFare);
                                            filteredFares.add(fare);
                                        }
                                    }
                                }
                            }
                        }
                        for (Fare fare : filteredFares) {
                            System.out.printf("%30s%30s%30s\n", fare.getCabinClass(), fare.getFareAmount(), fare.getFareAmount() * numPassenger);
                        }

                    } else {
                        System.out.printf("%30s%30s%30s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureTime(), flightSchedule.getArrivalTime());
                        List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                        System.out.printf("%30s%30s%30s%30s\n", "Cabin class", "Available seats", "Reserved seats", "Balanced");
                        for (SeatInventory seatInventory : seatInventories) {
                            System.out.printf("%30s%30s%30s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
                        }

                        List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                        List<Fare> filteredFares = new ArrayList<>();
                        List<CabinClassEnum> cabinClassEnums = new ArrayList<>();

                        for (Fare fare : fares) {
                            if (!cabinClassEnums.contains(fare.getCabinClass())) {
                                filteredFares.add(fare);
                                cabinClassEnums.add(fare.getCabinClass());
                            } else {
                                for (Fare filteredFare : filteredFares) {
                                    if (filteredFare.getCabinClass().equals(fare.getCabinClass())) {
                                        if (filteredFare.getFareAmount() > fare.getFareAmount()) {
                                            filteredFares.remove(filteredFare);
                                            filteredFares.add(fare);
                                        }
                                    }
                                }
                            }
                        }

                        FlightSchedule flightSchedule2 = flightSchedulesList.get(i + 1);
                        i++;
                        System.out.printf("%30s%30s%30s\n", flightSchedule2.getFlightScheduleId(), flightSchedule2.getDepartureTime(), flightSchedule2.getArrivalTime());
                        List<SeatInventory> seatInventories2 = flightSchedule2.getSeatInventories();
                        System.out.printf("%30s%30s%30s%30s\n", "Cabin class", "Available seats", "Reserved seats", "Balanced");
                        for (SeatInventory seatInventory : seatInventories2) {
                            System.out.printf("%30s%30s%30s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
                        }

                        List<Fare> fares2 = flightSchedule2.getFlightSchedulePlan().getFares();
                        List<Fare> filteredFares2 = new ArrayList<>();
                        List<CabinClassEnum> cabinClassEnums2 = new ArrayList<>();

                        for (Fare fare : fares) {
                            if (!cabinClassEnums2.contains(fare.getCabinClass())) {
                                filteredFares2.add(fare);
                                cabinClassEnums2.add(fare.getCabinClass());
                            } else {
                                for (Fare filteredFare : filteredFares2) {
                                    if (filteredFare.getCabinClass().equals(fare.getCabinClass())) {
                                        if (filteredFare.getFareAmount() > fare.getFareAmount()) {
                                            filteredFares2.remove(filteredFare);
                                            filteredFares2.add(fare);
                                        }
                                    }
                                }
                            }
                        }
                        List<Double> prices = new ArrayList<>();
                        for (Fare fare : filteredFares) {
                            for (Fare fare2 : filteredFares2) {
                                if (fare.getCabinClass().equals(fare2.getCabinClass())) {
                                    prices.add(fare.getFareAmount() + fare2.getFareAmount());
                                }
                            }
                        }
                        System.out.printf("%30s%30s%30s\n", "Cabin class", "Price per passenger", "Total price");
                        for (int index = 0; index < prices.size(); i++) {
                            System.out.printf("%30s%30s%30s\n", filteredFares.get(index).getCabinClass(), filteredFares.get(index).getFareAmount(), filteredFares.get(index).getFareAmount() * numPassenger);
                        }
                    }
                }
            }

        }
    }

    

    

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** FRS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            customer = customerSessionBeanRemote.customerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void register() {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();

        System.out.println("*** POS System :: System Administration :: Create New Staff ***\n");
        System.out.print("Enter First Name> ");
        customer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        customer.setLastName(scanner.nextLine().trim());
        System.out.println("Enter email> ");
        customer.setEmail(scanner.nextLine().trim());
        System.out.println("Enter mobile number> ");
        customer.setMobileNumber(scanner.nextLine().trim());
        System.out.println("Enter address> ");
        customer.setAddress(scanner.nextLine().trim());
        System.out.print("Enter Username> ");
        customer.setUserName(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        customer.setPassword(scanner.nextLine().trim());

        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

        if (constraintViolations.isEmpty()) {
            try {
                Long customerId = customerSessionBeanRemote.createNewCustomer(customer);
                System.out.println("New customer created successfully!: " + customerId + "\n");
            } catch (CustomerUsernameExistException ex) {
                System.out.println("An error has occurred while creating the new customer!: The user name already exist\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new staff!: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }

    }

    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
