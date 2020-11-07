/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassConfigurationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import entity.Employee;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import util.enumeration.CabinClassEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftConfigurationNameExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
public class FlightPlanningModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;

    private Employee currentEmployee;

    public FlightPlanningModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public FlightPlanningModule(Employee currentEmployee, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote) {
        this();
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.cabinClassConfigurationSessionBeanRemote = cabinClassConfigurationSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuFlightPlanningAircraftConfiguration() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != EmployeeAccessRightEnum.FLEETMANAGER) {
            throw new InvalidAccessRightException("You don't have Fleet Manager rights to access the Flight Planning Aircraft Configuration module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("***  FRS :: Flight Planning Module - Aircraft Configuration ***\n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View Aircraft Configuration Details");
            System.out.println("3: View All Aircraft Configurations");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateAircraftConfiguration();
                } else if (response == 2) {
                    doViewAircraftConfigurationDetails();
                } else if (response == 3) {
                    doViewAllAircraftConfigurations();
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

    public void menuFlightPlanningFlightRoute() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != EmployeeAccessRightEnum.ROUTEPLANNER) {
            throw new InvalidAccessRightException("You don't have Route Planner rights to access the Flight Planning Flight Route module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("***  FRS :: Flight Planning Module - Flight Route ***\n");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Route");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateFlightRoute();
                } else if (response == 2) {
                    doViewAllFlightRoute();
                } else if (response == 3) {
                    doDeleteFlightRoute();
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

    private void doCreateFlightRoute(){
        Scanner scanner = new Scanner(System.in);
        FlightRoute flightRoute = new FlightRoute();
        System.out.println("*** FRS :: Flight Planning Module - Flight Route :: Create Flight Route ***\n");
        System.out.print("Enter origin Airport Code> ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter destination Airport Code> ");
        String destination = scanner.nextLine().trim();
        Airport originAirport = new Airport();
        Airport destinationAirport = new Airport();
        try {
            originAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(origin);
            destinationAirport = airportSessionBeanRemote.retrieveAirportByAirportCode(destination);
        } catch (AirportNotFoundException ex) {
            System.out.println("Airport not found with code provided.");
        }

        try {
        Long id = flightRouteSessionBeanRemote.createNewFlightRoute(flightRoute, origin, destination);
        System.out.println("New flight route created successfully!: " + id + "\n");
        } catch (AirportNotFoundException ex) {
            System.out.println("Airport not found with airport code provided");
        } catch (FlightRouteNotFoundException ex) {
            System.out.println("Flight route not found.");
        } catch (UnknownPersistenceException ex) {
            System.out.println("An unknown error has occurred while creating the new flight route!: " + ex.getMessage() + "\n");
        }
    }
    
    private void doCreateAircraftConfiguration() {
        Scanner scanner = new Scanner(System.in);
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();

        System.out.println("*** FRS :: Flight Planning Module - Aircraft Configuration :: Create Aircraft Configuration ***\n");
        System.out.print("Enter Aircraft Name> ");
        String aircraftTypeName = scanner.nextLine().trim();
        System.out.print("Enter Aircraft Configuration Name> ");
        aircraftConfiguration.setName(scanner.nextLine().trim());
        System.out.println("Enter number of cabin class> ");
        int numCabinClass = scanner.nextInt();
        aircraftConfiguration.setNumCabinClass(numCabinClass);

        Set<ConstraintViolation<AircraftConfiguration>> constraintViolations = validator.validate(aircraftConfiguration);

        if (constraintViolations.isEmpty()) {
            try {
                Long aircraftConfigurationId = aircraftConfigurationSessionBeanRemote.createNewAircraftConfiguration(aircraftConfiguration, aircraftTypeName);
                System.out.println("======== Creating cabin classes ========");

                for (int i = 0; i < numCabinClass; i++) {
                    CabinClassConfiguration cabinClassConfiguration = new CabinClassConfiguration();
                    System.out.println("1: First Class");
                    System.out.println("2: Business");
                    System.out.println("3: Premium Economy");
                    System.out.println("4: Economy");
                    int res = scanner.nextInt();
                    CabinClassEnum ccEnum = CabinClassEnum.FIRSTCLASS;
                    switch (res) {
                        case 1:
                            ccEnum = CabinClassEnum.FIRSTCLASS;
                            break;
                        case 2:
                            ccEnum = CabinClassEnum.BUSINESS;
                            break;
                        case 3:
                            ccEnum = CabinClassEnum.PREMIUMECONOMY;
                            break;
                        case 4:
                            ccEnum = CabinClassEnum.ECONOMY;
                            break;
                    }
                    cabinClassConfiguration.setCabinClass(ccEnum);
                    System.out.println("Enter number of aisle> ");
                    cabinClassConfiguration.setNumAisle(scanner.nextInt());
                    System.out.println("Enter number of rows> ");
                    int row = scanner.nextInt();
                    cabinClassConfiguration.setNumRow(row);
                    System.out.println("Enter number of seats abreast> ");
                    int numSeatsAbreast = scanner.nextInt();
                    cabinClassConfiguration.setNumSeatsAbreast(numSeatsAbreast);
                    int maxCap = row * numSeatsAbreast;
                    cabinClassConfiguration.setMaxCabinSeatCapacity(maxCap);
                    System.out.println("Enter Seating Configuration Per Column>");
                    cabinClassConfiguration.setSeatingConfigurationPerColumn(scanner.nextLine().trim());
                    cabinClassConfigurationSessionBeanRemote.createNewCabinClassConfiguration(cabinClassConfiguration, aircraftConfigurationId);
                }
                System.out.println("New Aircraft configuration created successfully!: " + aircraftConfigurationId + "\n");
            } catch (AircraftConfigurationNameExistException ex) {
                System.out.println("An error has occurred while creating the new aircraft configuration!: The aircraft configuration name already exist\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new aircraft configuration!: " + ex.getMessage() + "\n");
            } catch (AircraftTypeNotFoundException ex) {
                System.out.println("An error has occurred while creating the new aircraft configuration!: Aircraft type does not exist with name provided. " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForStaffEntity(constraintViolations);
        }
    }

    private void doViewAircraftConfigurationDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** FRS :: Flight Planning Module - Aircraft Configuration :: View Aircraft Configuration Details***\n");
        System.out.print("Enter Aircraft Configuration Name> ");
        String aircraftConfigurationName = scanner.nextLine().trim();

        try {
            AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationByName(aircraftConfigurationName);
            System.out.printf("%20s%20s\n", "Configuration ID", "Name");
            System.out.printf("%20s%20s\n", aircraftConfiguration.getAircraftConfigurationId().toString(), aircraftConfiguration.getName());
            System.out.println("------------------------");
            System.out.printf("%20s%20s%20s%20s%20s\n", "Aisles", "Rows", "Num of seats abreast", "Seat config per col", "Max cabin capacity");
            int numCabinClass = aircraftConfiguration.getNumCabinClass();
            for (int i = 0; i < numCabinClass; i++) {
                CabinClassConfiguration cabinClassConfiguration = aircraftConfiguration.getCabinClassConfigurations().get(i);
                System.out.printf("%20s%20s%20s%20s%20s\n", cabinClassConfiguration.getNumAisle(), cabinClassConfiguration.getNumRow(), cabinClassConfiguration.getNumSeatsAbreast(), cabinClassConfiguration.getSeatingConfigurationPerColumn(), cabinClassConfiguration.getMaxCabinSeatCapacity());
            }
            System.out.println("1: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("An error has occurred while retrieving aircraft configuration: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllFlightRoute() {
        
    }
    private void doViewAllAircraftConfigurations() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS :: Flight Planning Module - Aircraft Configuration :: View All Aircraft Configuration***\n");

        List<AircraftConfiguration> aircraftConfigurations = aircraftConfigurationSessionBeanRemote.retrieveAllAircraftConfigurations();
        System.out.printf("%20s%20s\n", "Aircraft Type", "Configuration Name");

        List<String> records = new ArrayList<>();
        for (AircraftConfiguration aircraftConfiguration : aircraftConfigurations) {
            String str = aircraftConfiguration.getAircraftType().getName();
            str = str + "       ";
            str = str + aircraftConfiguration.getName();
            records.add(str);
        }
        Collections.sort(records);
        for (int i = 0; i < records.size(); i++) {
            System.out.println(records.get(i));
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void showInputDataValidationErrorsForStaffEntity(Set<ConstraintViolation<AircraftConfiguration>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
