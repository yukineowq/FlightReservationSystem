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
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatsInventorySessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author reuben
 */
public class MainApp {

    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote;

    private FlightOperationModule flightOperationModule;
    private FlightPlanningModule flightPlanningModule;
    private SalesManagementModule salesManagementModule;

    private Employee currentEmployee;

    public MainApp() {
    }

    public MainApp(AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
            AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote,
            AirportSessionBeanRemote airportSessionBeanRemote,
            CabinClassConfigurationSessionBeanRemote cabinClassConfigurationSessionBeanRemote,
            EmployeeSessionBeanRemote employeeSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote,
            FlightReservationSessionBeanRemote flightReservationSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote,
            FlightSessionBeanRemote flightSessionBeanRemote,
            SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote) {
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.cabinClassConfigurationSessionBeanRemote = cabinClassConfigurationSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.seatsInventorySessionBeanRemote = seatsInventorySessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Point-of-Sale (POS) System (v4.2) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        flightOperationModule = new FlightOperationModule(currentEmployee, flightSessionBeanRemote, flightScheduleSessionBeanRemote, flightSchedulePlanSessionBeanRemote, fareSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightRouteSessionBeanRemote);
                        flightPlanningModule = new FlightPlanningModule(currentEmployee, aircraftConfigurationSessionBeanRemote, flightRouteSessionBeanRemote, cabinClassConfigurationSessionBeanRemote, aircraftTypeSessionBeanRemote, airportSessionBeanRemote);
                        salesManagementModule = new SalesManagementModule(currentEmployee, seatsInventorySessionBeanRemote, flightReservationSessionBeanRemote, flightSessionBeanRemote);
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
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

        System.out.println("*** POS System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** FRS Management Client ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRight().toString() + " rights\n");
            System.out.println("1: Flight Planning - Aircraft Congiguration");
            System.out.println("2: Flight Planning - Flight Route");
            System.out.println("3: Flight Operation");
            System.out.println("4: Sales Management");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        flightPlanningModule.menuFlightPlanningAircraftConfiguration();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    try {
                        flightPlanningModule.menuFlightPlanningFlightRoute();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    try {
                        flightOperationModule.menuFlightOperation();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 4) {                    
                    try {
                        salesManagementModule.menuSalesManagement();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
    }
}
