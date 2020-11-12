/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemreservationclient;

import ejb.session.stateful.ReserveFlightSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Airport;
import entity.Customer;
import entity.FlightSchedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private CustomerOperationModule customerOperationModule;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.reserveFlightSessionBeanRemote = reserveFlightSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;

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

    private void search() {
        Scanner scanner = new Scanner(System.in);
        Airport originAirport = new Airport();
        Airport destinationAirport = new Airport();
        Date departureDate, returnDate;
        CabinClassEnum cabinClass;
        PreferenceEnum preference;
        int numPassenger;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("Select trip type: ");
        System.out.println("1: One-way");
        System.out.println("2: Round-trip");
        int res = 0;
        while (res < 1 || res > 2) {
            System.out.print("> ");
            res = scanner.nextInt();
            scanner.nextLine();
        }
        if (res == 1) {
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
            try {
                departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            } catch (ParseException ex) {
                System.out.println("Invalid date input!\n");
            }
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
            }else if (res2 == 3) {
                cabinClass = CabinClassEnum.PREMIUMECONOMY;
            } else if (res2 == 4 ) {
                cabinClass = CabinClassEnum.ECONOMY;
            } else {
                cabinClass = CabinClassEnum.NA;
            }
        } else if (res == 2) {
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
            try {
                departureDate = inputDateFormat.parse(scanner.nextLine().trim());
            } catch (ParseException ex) {
                System.out.println("Invalid date input!\n");
            }
            System.out.println("Enter return date (yyyy-mm-dd)> ");
            try {
                returnDate = inputDateFormat.parse(scanner.nextLine().trim());
            } catch (ParseException ex) {
                System.out.println("Invalid date input!\n");
            }
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
            }else if (res2 == 3) {
                cabinClass = CabinClassEnum.PREMIUMECONOMY;
            } else if (res2 == 4 ) {
                cabinClass = CabinClassEnum.ECONOMY;
            } else {
                cabinClass = CabinClassEnum.NA;
            }

        }
        //List<FlightSchedule> flightSchedules = 

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
