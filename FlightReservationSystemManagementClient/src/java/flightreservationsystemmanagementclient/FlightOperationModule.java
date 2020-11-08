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
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.FlightDeleteException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteDoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;
import util.exception.FlightNotFoundException;
import util.exception.UpdateFlightException;

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
        
        System.out.println("Create complementary Flight? > Y/N ");
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
            
            if(response == 1)
            {
                doUpdateFlight(flight);
            }
            else if(response == 2)
            {
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
        
        if(input.equals("Y"))
        {
            try
            {
                flightSessionBeanRemote.deleteFlight(flightNumber);
                System.out.println("Flight deleted successfully!\n");
            }
            catch (FlightNumberExistException ex) 
            {
                System.out.println("An error has occurred while deleting the flight: " + ex.getMessage() + "\n");
            }
            catch (FlightDeleteException ex)
            {
                System.out.println("Flight is used, cannot be deleted. Flight marked as disabled instead.");
            }
        }
        else
        {
            System.out.println("Flight NOT deleted!\n");
        }
    }
    private void doUpdateFlight(Flight flight) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** FRS :: Flight Operation Module :: View Flight Details :: Update Flight ***\n");
        System.out.print("Enter new Flight Number (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            flight.setFlightNumber(input);
        }
                
        System.out.print("Enter new Flight Route OD pair (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
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
        if(input.length() > 0)
        {
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
               
        Set<ConstraintViolation<Flight>>constraintViolations = validator.validate(flight);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                flightSessionBeanRemote.updateFlight(flight);
                System.out.println("Flight updated successfully!\n");
            } 
            catch (FlightNotFoundException | UpdateFlightException ex) 
            {
                System.out.println("An error has occurred while updating staff: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
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
    
}
