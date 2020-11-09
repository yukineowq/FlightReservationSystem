/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemreservationclient;

import ejb.session.stateful.ReserveFlightSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
    
    private CustomerOperationModule customerOperationModule;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.reserveFlightSessionBeanRemote = reserveFlightSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Customer Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
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
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
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
    
    private void register()
    {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
        
        System.out.println("*** POS System :: System Administration :: Create New Staff ***\n");
        System.out.print("Enter First Name> ");
        customer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        customer.setLastName(scanner.nextLine().trim());

        System.out.print("Enter Username> ");
        customer.setUserName(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        customer.setPassword(scanner.nextLine().trim());
        
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(customer);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                Long customerId = customerSessionBeanRemote.createNewCustomer(customer);
                System.out.println("New staff created successfully!: " + customerId + "\n");
            }
            catch(CustomerUsernameExistException ex)
            {
                System.out.println("An error has occurred while creating the new customer!: The user name already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new staff!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }
    }
    
    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
}
