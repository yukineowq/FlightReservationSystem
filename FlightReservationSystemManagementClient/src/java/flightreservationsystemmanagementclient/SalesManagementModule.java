/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemmanagementclient;

import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatsInventorySessionBeanRemote;
import entity.Employee;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.FlightNumberExistException;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author reuben
 */
public class SalesManagementModule {

    private Employee currentEmployee;
    private SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;

    public SalesManagementModule() {
    }

    public SalesManagementModule(Employee currentEmployee, SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote) {
        this.currentEmployee = currentEmployee;
        this.seatsInventorySessionBeanRemote = seatsInventorySessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
    }

    public void menuSalesManagement() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != EmployeeAccessRightEnum.SALESMANAGER) {
            throw new InvalidAccessRightException("You don't have Sales Manager rights to access the Flight Planning Aircraft Configuration module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("***  FRS :: Sales Management Module ***\n");
            System.out.println("1: View seats inventory");
            System.out.println("2: View flight reservations");
            System.out.println("3: Back\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doViewSeatsInventory();
                } else if (response == 2) {
                    doViewFlightReservations();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    public void doViewFlightReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS :: Sales Management Module :: View Seats Inventory ***\n");
        System.out.println("Enter flight number> ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = new Flight();
        try {
            flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(SalesManagementModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlan();
        List<FlightSchedule> flightSchedules1 = new ArrayList<>();
        int counter = 0;
        System.out.println("List of flight schedules associated with this flight:");
        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
            List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
            for (FlightSchedule flightSchedule : flightSchedules) {
                System.out.println(counter + ": " + flightSchedule.getFlightScheduleId());
                flightSchedules1.add(flightSchedule);
                counter++;
            }
        }
        System.out.println("Select flight schedule> ");
        FlightSchedule flightSchedule = flightSchedules1.get(scanner.nextInt());
        List<FlightReservation> flightReservations = flightSchedule.getFlightReservations();
        Collections.sort(flightReservations, new Comparator<FlightReservation>() {
            public int compare(FlightReservation r1, FlightReservation r2) {
                return r1.getSeatNumber().compareTo(r2.getSeatNumber());
            }
        });
        System.out.printf("%20s%20s%20s\n", "Seat Number", "Passenger Name", "Fare Basis Code");
        for (FlightReservation flightReservation : flightReservations) {
            System.out.printf("%20s%20s%20s\n", flightReservation.getSeatNumber(), flightReservation.getPassengerName(), flightReservation.getFareBasisCode());
        }
    }

    public void doViewSeatsInventory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** FRS :: Sales Management Module :: View Seats Inventory ***\n");
        System.out.println("Enter flight number> ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = new Flight();
        try {
            flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(SalesManagementModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlan();
        List<FlightSchedule> flightSchedules1 = new ArrayList<>();
        int counter = 0;
        System.out.println("List of flight schedules associated with this flight:");
        for (FlightSchedulePlan flightSchedulePlan : flightSchedulePlans) {
            List<FlightSchedule> flightSchedules = flightSchedulePlan.getFlightSchedules();
            for (FlightSchedule flightSchedule : flightSchedules) {
                System.out.println(counter + ": " + flightSchedule.getFlightScheduleId());
                flightSchedules1.add(flightSchedule);
                counter++;
            }
        }
        System.out.println("Select flight schedule> ");
        FlightSchedule flightSchedule = flightSchedules1.get(scanner.nextInt());
        List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
        System.out.printf("%20s%20s%20s%20s\n", "Cabin Class", "Available Seats", "Reserved Seats", "Balance Seats");
        for (SeatInventory seatInventory : seatInventories) {
            System.out.printf("%20s%20s%20s%20s\n", seatInventory.getCabinClass(), seatInventory.getAvailable(), seatInventory.getReserved(), seatInventory.getBalance());
        }
        int total = 0;
        int available = 0;
        int reserved = 0;
        int balance = 0;
        for (SeatInventory seatInventory : seatInventories) {
            available = available + seatInventory.getAvailable();
            reserved = reserved + seatInventory.getReserved();
            balance = balance + seatInventory.getBalance();
        }
        total = available + balance + reserved;
        System.out.println("Total seats: " + total);
        System.out.println("Available seats: " + available);
        System.out.println("Balance seats: " + balance);
        System.out.println("Reserved seats: " + reserved);
    }

}
