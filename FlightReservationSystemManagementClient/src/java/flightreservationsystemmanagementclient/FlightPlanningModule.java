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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import util.enumeration.CabinClassEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftConfigurationNameExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeMaxSeatCapacityExceededException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteDoesNotExistException;
import util.exception.FlightRouteAlreadyExistException;
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
                    try {
                        doCreateAircraftConfiguration();
                    } catch (AircraftTypeMaxSeatCapacityExceededException ex) {
                        System.out.println("Max Seat capacity for aircraft exceeded!");
                    }
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
                    try {
                        doCreateFlightRoute();
                    } catch (FlightRouteAlreadyExistException ex) {
                        System.out.println("Flight route already exist in database!");
                    }
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

    private void doDeleteFlightRoute() {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** FRS :: Flight Planning Module - Flight Route :: Delete Flight Route ***\n");
        System.out.println("Enter origin airport code> ");
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
            Logger.getLogger(FlightPlanningModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Found Flight Route: ");
        System.out.printf("%20s%20s%20s%20s\n", flightRoute.getOrigin().getCountry(), flightRoute.getOrigin().getName(), flightRoute.getDestination().getCountry(), flightRoute.getDestination().getName());
        System.out.println("Confirm delete this Flight Route? Y/N > ");
        input = scanner.nextLine().trim();
        if (input.equals("Y")) {
            try {
                flightRouteSessionBeanRemote.deleteFlightRoute(origin, destination);
                System.out.println("Flight route deleted successfully!\n");
            } catch (FlightRouteDoesNotExistException ex) {
                System.out.println("An error has occurred while deleting Flight route.: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Flight Route NOT deleted!\n");
        }
    }

    private void doCreateFlightRoute() throws FlightRouteAlreadyExistException {
        Scanner scanner = new Scanner(System.in);
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
            List<FlightRoute> flightRoutes = flightRouteSessionBeanRemote.retrieveAllFlightRoutes();
            for (FlightRoute flightRoute : flightRoutes) {
                if (flightRoute.getOrigin().getAirportCode().equals(origin) && flightRoute.getDestination().getAirportCode().equals(destination)) {
                    throw new FlightRouteAlreadyExistException("Flight route already exist in database!");
                }
            }
            FlightRoute flightRoute = new FlightRoute();
            flightRoute.setOrigin(originAirport);
            flightRoute.setDestination(destinationAirport);
            /*
       FlightRoute flightRoute = new FlightRoute();
       flightRoute.setOrigin(originAirport);
       flightRoute.setDestination(destinationAirport);
       flightRoute.setOD(OD);
             */
            System.out.println("Create complemantary return flight?> ");
            System.out.println("1: Yes");
            System.out.println("2: No");
            int res = scanner.nextInt();
            FlightRoute complementary = new FlightRoute();
            if (res == 1) {
                flightRoute.setComplementary(true);
                complementary.setComplementary(true);
            } else {
                flightRoute.setComplementary(false);
            }

            try {

                Long id = flightRouteSessionBeanRemote.createNewFlightRoute(flightRoute, origin, destination);

                System.out.println("New flight route created successfully!: " + id + "\n");
            } catch (AirportNotFoundException ex) {
                System.out.println("Airport not found with airport code provided");
            } catch (FlightRouteNotFoundException ex) {
                System.out.println("Flight route not found1.");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while creating the new flight route!: " + ex.getMessage() + "\n");
            }
            if (res == 1) {
                try {

                    Long id = flightRouteSessionBeanRemote.createNewFlightRoute(flightRoute, destination, origin);

                    System.out.println("New Complementary flight route created successfully!: " + id + "\n");
                } catch (AirportNotFoundException ex) {
                    System.out.println("Airport not found with airport code provided");
                } catch (FlightRouteNotFoundException ex) {
                    System.out.println("Flight route not found1.");
                } catch (UnknownPersistenceException ex) {
                    System.out.println("An unknown error has occurred while creating the new flight route!: " + ex.getMessage() + "\n");
                }
            }
        } catch (AirportNotFoundException ex) {
            System.out.println("Airport not found with code provided.");
        }

    }

    private void doCreateAircraftConfiguration() throws AircraftTypeMaxSeatCapacityExceededException {
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

        AircraftType aircraftType = new AircraftType();
        try {
            aircraftType = aircraftTypeSessionBeanRemote.retrieveAircraftTypeByName(aircraftTypeName);
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(FlightPlanningModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        int maxCap = aircraftType.getMaxSeatCapacity();
        int currCap = 0;

        Set<ConstraintViolation<AircraftConfiguration>> constraintViolations = validator.validate(aircraftConfiguration);

        if (constraintViolations.isEmpty()) {
            try {

                System.out.println("======== Creating cabin classes ========");
                List<CabinClassConfiguration> cabinClassConfigurations = new ArrayList<>();
                for (int i = 0; i < numCabinClass; i++) {
                    CabinClassConfiguration cabinClassConfiguration = new CabinClassConfiguration();
                    System.out.println("1: First Class");
                    System.out.println("2: Business");
                    System.out.println("3: Premium Economy");
                    System.out.println("4: Economy");
                    System.out.println("");
                    System.out.println("Select Cabin Class to create the Cabin class configuration> ");
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
                    System.out.println("Enter number of aisle for " + ccEnum + " > ");
                    cabinClassConfiguration.setNumAisle(scanner.nextInt());
                    System.out.println("Enter number of rows for " + ccEnum + " > ");
                    int row = scanner.nextInt();
                    scanner.nextLine();
                    cabinClassConfiguration.setNumRow(row);
                    System.out.println("Enter number of seats abreast for " + ccEnum + " > ");
                    int numSeatsAbreast = scanner.nextInt();
                    scanner.nextLine();
                    cabinClassConfiguration.setNumSeatsAbreast(numSeatsAbreast);
                    int maxCapacity = row * numSeatsAbreast;
                    currCap = currCap + maxCapacity;
                    cabinClassConfiguration.setMaxCabinSeatCapacity(maxCapacity);
                    System.out.println("Enter Seating Configuration Per Column (e.g. 3-4-3) > ");
                    String seatConfig = scanner.nextLine().trim();
                    cabinClassConfiguration.setSeatingConfigurationPerColumn(seatConfig);
                    cabinClassConfigurations.add(cabinClassConfiguration);
                    aircraftConfiguration.getCabinClassConfigurations().add(cabinClassConfiguration);
                    cabinClassConfiguration.setAircraftConfiguration(aircraftConfiguration);
                }
                //problem here, still creates config
                if (currCap > maxCap) {
                    throw new AircraftTypeMaxSeatCapacityExceededException("Max Seat Capacity exceeded for Aircraft!");
                } else {
                    Long aircraftConfigurationId = aircraftConfigurationSessionBeanRemote.createNewAircraftConfiguration(aircraftConfiguration, aircraftTypeName);
                    for (CabinClassConfiguration cabinClassConfiguration : cabinClassConfigurations) {
                        cabinClassConfigurationSessionBeanRemote.createNewCabinClassConfiguration(cabinClassConfiguration, aircraftConfigurationId);
                    }

                    System.out.println("New Aircraft configuration created successfully!: " + aircraftConfigurationId + "\n");
                }
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
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** FRS :: Flight Planning Module - Flight Route :: View All Flight Routes ***\n");

        List<FlightRoute> flightRoutes = flightRouteSessionBeanRemote.retrieveAllFlightRoutes();
        Collections.sort(flightRoutes, new Comparator<FlightRoute>() {
            public int compare(FlightRoute r1, FlightRoute r2) {
                return r1.getOrigin().getName().compareTo(r2.getOrigin().getName());
            }
        });
        List<FlightRoute> flightRoutes1 = new ArrayList<>();
        for (FlightRoute flightRoute : flightRoutes) {
            if (!flightRoutes1.contains(flightRoute)) {
                flightRoutes1.add(flightRoute);
                Airport origin = flightRoute.getOrigin();
                Airport destination = flightRoute.getDestination();
                for (FlightRoute flightRoute1 : flightRoutes) {
                    if (flightRoute1.getOrigin() == destination && flightRoute1.getDestination() == origin) {
                        flightRoutes1.add(flightRoute1);
                    }
                }
            }
        }
        System.out.printf("%20s%20s%20s%20s\n", "Origin Country", "Origin Airport", "Destination Country", "Destination Airport");
        for (FlightRoute flightRoute : flightRoutes1) {

            System.out.printf("%20s%20s%20s%20s\n", flightRoute.getOrigin().getCountry(), flightRoute.getOrigin().getName(), flightRoute.getDestination().getCountry(), flightRoute.getDestination().getName());

        }

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
