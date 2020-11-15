/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationsystemreservationclient;

import ejb.session.stateless.ReserveFlightSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.SeatsInventorySessionBeanRemote;
import entity.CabinClassConfiguration;
import entity.Customer;
import entity.Fare;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassEnum;
import util.exception.FlightReservationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateSeatInventoryException;

/**
 *
 * @author reuben
 */
public class CustomerOperationModule {

    private Customer customer;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote;

    public CustomerOperationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerOperationModule(Customer customer, FlightSessionBeanRemote flightSessionBeanRemote, ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote) {
        this();
        this.customer = customer;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.reserveFlightSessionBeanRemote = reserveFlightSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.seatsInventorySessionBeanRemote = seatsInventorySessionBeanRemote;
    }
    
    public void doViewAllReservation() {
        List<FlightReservation> flightReservations = flightReservationSessionBeanRemote.viewFlightReservations(customer.getCustomerId());
        for (FlightReservation flightReservation : flightReservations) {
            System.out.printf("%20s%20s\n", "Reservation ID", "Total Amount", "Cabin Class");
            System.out.printf("%20s%20s\n", flightReservation.getFlightReservationId(), flightReservation.getFareAmount(), flightReservation.getCabinClassEnum());
            System.out.println("");
            System.out.printf("%20s\n", "Passengers");
            for (String str : flightReservation.getPassengerName()) {
                System.out.printf("%20s\n", str);
            }
            System.out.println("");
            for (FlightSchedule flightSchedule : flightReservation.getFlightSchedule()) {
                System.out.printf("%20s%20s%20s%20s%20s\n", "Flight Number", "Origin", "Destination", "Departure Time", "Arrivale Time");
                System.out.printf("%20s%20s%20s%20s%20s\n", flightSchedule.getFlightSchedulePlan().getFlightNumber(), flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getAirportCode(), flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportCode(), flightSchedule.getDepartureTime(), flightSchedule.getArrivalTime());
            }
            System.out.println("");
            System.out.printf("%20s\n", "Seat Number");
            for (String str : flightReservation.getSeatNumber() ) {
                System.out.printf("%20s\n", str);
            }
            
        }
    }
    
    public void doViewReservationDetails() {
        List<FlightReservation> flightReservations = flightReservationSessionBeanRemote.viewFlightReservations(customer.getCustomerId());
        for (FlightReservation flightReservation : flightReservations) {
            System.out.printf("%20s%20s\n", "Reservation ID", "Total Amount");
            System.out.printf("%20s%20s\n", flightReservation.getFlightReservationId(), flightReservation.getFareAmount());
        }
    }

    public void doReserveFlight() {
        Scanner scanner = new Scanner(System.in);
        Long id = 0L;
        System.out.println("Please enter the flight ID that you want to reserve> ");
        id = scanner.nextLong();

        System.out.println("Enter number of passengers> ");
        int numPassenger = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Select trip type: ");
        System.out.println("1: One-way");
        System.out.println("2: Round-trip");
        int trip = 0;
        while (trip < 1 || trip > 2) {
            System.out.print("> ");
            trip = scanner.nextInt();
            scanner.nextLine();
        }
        System.out.println("Direct flight or connecting? >  ");
        System.out.println("1: Direct");
        System.out.println("2: Connecting");
        int res1 = 0;
        while (res1 < 1 || res1 > 2) {
            System.out.print("> ");
            res1 = scanner.nextInt();
            scanner.nextLine();
        }
        System.out.println("Please select the cabin class> ");
        CabinClassEnum cabinClass = CabinClassEnum.ECONOMY;
        System.out.println("1: First class");
        System.out.println("2: Business");
        System.out.println("3: Premium economy");
        System.out.println("4: Economy");
        int res2 = 0;
        while (res2 < 1 || res2 > 4) {
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
        } else {
            cabinClass = CabinClassEnum.ECONOMY;
        }

        if (trip == 1) {
            FlightReservation flightReservation = new FlightReservation();
            flightReservation.setCabinClassEnum(cabinClass);
            flightReservation.setCustomer(customer);
            customer.getFlightReservations().add(flightReservation);

            if (res1 == 1) {
                //One way direct flight
                System.out.println("Please enter the flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                SeatInventory seatInventory = new SeatInventory();
                List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory = seatInventory1;
                    }
                }
                CabinClassConfiguration cabinClassConfiguration = seatInventory.getCabinClassConfiguration();
                int numRows = cabinClassConfiguration.getNumRow();
                int numSeatAbreast = cabinClassConfiguration.getNumSeatsAbreast();
                boolean[][] seats = seatInventory.getTaken();
                System.out.println("Available seats:");
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numSeatAbreast; col++) {
                        boolean taken = seats[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats[row][col] = true;
                    String seat = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getPassengerName().add(details);
                    flightReservation.getSeatNumber().add(seat);
                    
                }
                seatInventory.setTaken(seats);
                Set<ConstraintViolation<SeatInventory>> constraintViolations = validator.validate(seatInventory);
                if (constraintViolations.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations);
                }
                System.out.println("Enter creditCard details (Name, card number, expiration date, cvv)> ");
                String creditCardDetails = scanner.nextLine().trim();
                flightReservation.setCreditCard(creditCardDetails);
                flightReservation.getFlightSchedule().add(flightSchedule);
                flightSchedule.getFlightReservations().add(flightReservation);
                List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                Fare fare = new Fare();
                for (Fare fare1 : fares) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare = fare1;
                    }
                }
                double fareAmt = fare.getFareAmount() * numPassenger;
                String fareCode = fare.getFareBasisCode();
                flightReservation.setFareAmount(fareAmt);
                flightReservation.setFareBasisCode(fareCode);

                Set<ConstraintViolation<FlightReservation>> constraintViolations1 = validator.validate(flightReservation);
                if (constraintViolations1.isEmpty()) {
                    try {
                        Long flightId = flightReservationSessionBeanRemote.createNewFlightReservation(flightReservation);
                        System.out.println("New Flight Reservation created successfully!: " + flightId + "\n");
                    } catch (FlightReservationNotFoundException ex) {
                        System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
                    } catch (UnknownPersistenceException ex) {
                        System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForFlightReservation(constraintViolations1);
                }
            } else {
                //Connecting flight one way
                System.out.println("Please enter the first flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                flightSchedule.getFlightReservations().add(flightReservation);
                flightReservation.getFlightSchedule().add(flightSchedule);
                System.out.println("Please enter the second flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule2 = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                flightSchedule2.getFlightReservations().add(flightReservation);
                flightReservation.getFlightSchedule().add(flightSchedule2);
                SeatInventory seatInventory = new SeatInventory();
                List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory = seatInventory1;
                    }
                }

                SeatInventory seatInventory2 = new SeatInventory();
                List<SeatInventory> seatInventories2 = flightSchedule2.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories2) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory2 = seatInventory1;
                    }
                }

                //First flight
                CabinClassConfiguration cabinClassConfiguration = seatInventory.getCabinClassConfiguration();
                int numRows = cabinClassConfiguration.getNumRow();
                int numSeatAbreast = cabinClassConfiguration.getNumSeatsAbreast();
                boolean[][] seats = seatInventory.getTaken();
                System.out.println("Available seats for first flight:");
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numSeatAbreast; col++) {
                        boolean taken = seats[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats[row][col] = true;
                    String seat = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getSeatNumber().add(seat);
                    flightReservation.getPassengerName().add(details);
                }
                seatInventory.setTaken(seats);
                Set<ConstraintViolation<SeatInventory>> constraintViolations = validator.validate(seatInventory);
                if (constraintViolations.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations);
                }

                //Second flight
                CabinClassConfiguration cabinClassConfiguration2 = seatInventory2.getCabinClassConfiguration();
                int numRows2 = cabinClassConfiguration2.getNumRow();
                int numSeatAbreast2 = cabinClassConfiguration2.getNumSeatsAbreast();
                boolean[][] seats2 = seatInventory2.getTaken();
                System.out.println("Available seats for second flight:");
                for (int row = 0; row < numRows2; row++) {
                    for (int col = 0; col < numSeatAbreast2; col++) {
                        boolean taken = seats2[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats2[row][col] = true;
                    String seat = flightSchedule2.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule2.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();

                    flightReservation.getSeatNumber().add(seat);
                    flightReservation.getPassengerName().add(details);
                }
                seatInventory2.setTaken(seats2);
                Set<ConstraintViolation<SeatInventory>> constraintViolations2 = validator.validate(seatInventory2);
                if (constraintViolations2.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory2);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations);
                }
                System.out.println("Enter creditCard details (Name, card number, expiration date, cvv)> ");
                String creditCardDetails = scanner.nextLine().trim();
                flightReservation.setCreditCard(creditCardDetails);
                List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                Fare fare = new Fare();
                for (Fare fare1 : fares) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare = fare1;
                    }
                }
                double fareAmt = fare.getFareAmount() * numPassenger;
                List<Fare> fares2 = flightSchedule2.getFlightSchedulePlan().getFares();
                Fare fare2 = new Fare();
                for (Fare fare1 : fares2) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare2 = fare1;
                    }
                }
                fareAmt = fareAmt + fare2.getFareAmount() * numPassenger;
                String fareCode = fare.getFareBasisCode();
                flightReservation.setFareAmount(fareAmt);
                flightReservation.setFareBasisCode(fareCode);

                Set<ConstraintViolation<FlightReservation>> constraintViolations1 = validator.validate(flightReservation);
                if (constraintViolations1.isEmpty()) {
                    try {
                        Long flightId = flightReservationSessionBeanRemote.createNewFlightReservation(flightReservation);
                        System.out.println("New Flight Reservation created successfully!: " + flightId + "\n");
                    } catch (FlightReservationNotFoundException ex) {
                        System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
                    } catch (UnknownPersistenceException ex) {
                        System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForFlightReservation(constraintViolations1);
                }
            }

        } else {
            //Return flight
            FlightReservation flightReservation = new FlightReservation();
            flightReservation.setCabinClassEnum(cabinClass);
            flightReservation.setCustomer(customer);
            customer.getFlightReservations().add(flightReservation);

            if (res1 == 1) {
                //Return direct flight
                System.out.println("Please enter the outbound flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                SeatInventory seatInventory = new SeatInventory();
                List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory = seatInventory1;
                    }
                }
                CabinClassConfiguration cabinClassConfiguration = seatInventory.getCabinClassConfiguration();
                int numRows = cabinClassConfiguration.getNumRow();
                int numSeatAbreast = cabinClassConfiguration.getNumSeatsAbreast();
                boolean[][] seats = seatInventory.getTaken();
                System.out.println("Available seats:");
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numSeatAbreast; col++) {
                        boolean taken = seats[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats[row][col] = true;
                    String seat = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getPassengerName().add(details);
                    flightReservation.getSeatNumber().add(seat);
                    
                }
                seatInventory.setTaken(seats);
                Set<ConstraintViolation<SeatInventory>> constraintViolations = validator.validate(seatInventory);
                if (constraintViolations.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations);
                }

                System.out.println("Please enter the return flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule2 = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                SeatInventory seatInventory2 = new SeatInventory();
                List<SeatInventory> seatInventories2 = flightSchedule2.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories2) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory2 = seatInventory1;
                    }
                }
                CabinClassConfiguration cabinClassConfiguration2 = seatInventory2.getCabinClassConfiguration();
                int numRows2 = cabinClassConfiguration2.getNumRow();
                int numSeatAbreast2 = cabinClassConfiguration2.getNumSeatsAbreast();
                boolean[][] seats2 = seatInventory2.getTaken();
                System.out.println("Available seats:");
                for (int row = 0; row < numRows2; row++) {
                    for (int col = 0; col < numSeatAbreast2; col++) {
                        boolean taken = seats2[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats2[row][col] = true;
                    String seat = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getPassengerName().add(details);
                    flightReservation.getSeatNumber().add(seat);
                }
                seatInventory2.setTaken(seats);
                Set<ConstraintViolation<SeatInventory>> constraintViolations4 = validator.validate(seatInventory2);
                if (constraintViolations4.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory2);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations4);
                }
                System.out.println("Enter creditCard details (Name, card number, expiration date, cvv)> ");
                String creditCardDetails = scanner.nextLine().trim();
                flightReservation.setCreditCard(creditCardDetails);
                flightReservation.getFlightSchedule().add(flightSchedule);
                flightReservation.getFlightSchedule().add(flightSchedule2);
                flightSchedule.getFlightReservations().add(flightReservation);
                flightSchedule2.getFlightReservations().add(flightReservation);
                List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                Fare fare = new Fare();
                for (Fare fare1 : fares) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare = fare1;
                    }
                }
                double fareAmt = fare.getFareAmount() * numPassenger;

                List<Fare> fares2 = flightSchedule2.getFlightSchedulePlan().getFares();
                Fare fare2 = new Fare();
                for (Fare fare1 : fares2) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare2 = fare1;
                    }
                }
                fareAmt = fareAmt + fare2.getFareAmount() * numPassenger;
                String fareCode = fare.getFareBasisCode();
                flightReservation.setFareAmount(fareAmt);
                flightReservation.setFareBasisCode(fareCode);

                Set<ConstraintViolation<FlightReservation>> constraintViolations5 = validator.validate(flightReservation);
                if (constraintViolations5.isEmpty()) {
                    try {
                        Long flightId = flightReservationSessionBeanRemote.createNewFlightReservation(flightReservation);
                        System.out.println("New Flight Reservation created successfully!: " + flightId + "\n");
                    } catch (FlightReservationNotFoundException ex) {
                        System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
                    } catch (UnknownPersistenceException ex) {
                        System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForFlightReservation(constraintViolations5);
                }
            } else {
                //Connecting flight return
                System.out.println("Please enter the first outbound flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                flightSchedule.getFlightReservations().add(flightReservation);
                flightReservation.getFlightSchedule().add(flightSchedule);
                System.out.println("Please enter the second outbound flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule2 = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                flightSchedule2.getFlightReservations().add(flightReservation);
                flightReservation.getFlightSchedule().add(flightSchedule2);
                SeatInventory seatInventory = new SeatInventory();
                List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory = seatInventory1;
                    }
                }

                SeatInventory seatInventory2 = new SeatInventory();
                List<SeatInventory> seatInventories2 = flightSchedule2.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories2) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory2 = seatInventory1;
                    }
                }

                //First flight
                CabinClassConfiguration cabinClassConfiguration = seatInventory.getCabinClassConfiguration();
                int numRows = cabinClassConfiguration.getNumRow();
                int numSeatAbreast = cabinClassConfiguration.getNumSeatsAbreast();
                boolean[][] seats = seatInventory.getTaken();
                System.out.println("Available seats for first flight:");
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numSeatAbreast; col++) {
                        boolean taken = seats[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats[row][col] = true;
                    String seat = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getSeatNumber().add(seat);
                    flightReservation.getPassengerName().add(details);
                }
                seatInventory.setTaken(seats);
                Set<ConstraintViolation<SeatInventory>> constraintViolations = validator.validate(seatInventory);
                if (constraintViolations.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations);
                }

                //Second flight
                CabinClassConfiguration cabinClassConfiguration2 = seatInventory2.getCabinClassConfiguration();
                int numRows2 = cabinClassConfiguration2.getNumRow();
                int numSeatAbreast2 = cabinClassConfiguration2.getNumSeatsAbreast();
                boolean[][] seats2 = seatInventory2.getTaken();
                System.out.println("Available seats for second flight:");
                for (int row = 0; row < numRows2; row++) {
                    for (int col = 0; col < numSeatAbreast2; col++) {
                        boolean taken = seats2[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats2[row][col] = true;
                    String seat = flightSchedule2.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule2.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();

                    flightReservation.getSeatNumber().add(seat);
                    flightReservation.getPassengerName().add(details);
                }
                seatInventory2.setTaken(seats2);
                Set<ConstraintViolation<SeatInventory>> constraintViolations2 = validator.validate(seatInventory2);
                if (constraintViolations2.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory2);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations);
                }

                //Return
                System.out.println("Please enter the first return flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule3 = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                flightSchedule3.getFlightReservations().add(flightReservation);
                flightReservation.getFlightSchedule().add(flightSchedule3);
                System.out.println("Please enter the second return flight schedule ID that you want to reserve> ");
                id = scanner.nextLong();
                FlightSchedule flightSchedule4 = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(id);
                flightSchedule4.getFlightReservations().add(flightReservation);
                flightReservation.getFlightSchedule().add(flightSchedule4);
                SeatInventory seatInventory3 = new SeatInventory();
                List<SeatInventory> seatInventories3 = flightSchedule3.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories3) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory3 = seatInventory1;
                    }
                }

                SeatInventory seatInventory4 = new SeatInventory();
                List<SeatInventory> seatInventories4 = flightSchedule4.getSeatInventories();
                for (SeatInventory seatInventory1 : seatInventories4) {
                    if (seatInventory1.getCabinClass().equals(cabinClass)) {
                        seatInventory4 = seatInventory1;
                    }
                }

                //First flight
                CabinClassConfiguration cabinClassConfiguration3 = seatInventory3.getCabinClassConfiguration();
                int numRows3 = cabinClassConfiguration3.getNumRow();
                int numSeatAbreast3 = cabinClassConfiguration3.getNumSeatsAbreast();
                boolean[][] seats3 = seatInventory3.getTaken();
                System.out.println("Available seats for first flight:");
                for (int row = 0; row < numRows3; row++) {
                    for (int col = 0; col < numSeatAbreast3; col++) {
                        boolean taken = seats3[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats3[row][col] = true;
                    String seat = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getSeatNumber().add(seat);
                    flightReservation.getPassengerName().add(details);
                }
                seatInventory3.setTaken(seats);
                Set<ConstraintViolation<SeatInventory>> constraintViolations6 = validator.validate(seatInventory3);
                if (constraintViolations6.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory3);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations6);
                }

                //Second flight
                CabinClassConfiguration cabinClassConfiguration4 = seatInventory4.getCabinClassConfiguration();
                int numRows4 = cabinClassConfiguration4.getNumRow();
                int numSeatAbreast4 = cabinClassConfiguration4.getNumSeatsAbreast();
                boolean[][] seats4 = seatInventory4.getTaken();
                System.out.println("Available seats for second returning flight:");
                for (int row = 0; row < numRows4; row++) {
                    for (int col = 0; col < numSeatAbreast2; col++) {
                        boolean taken = seats4[row][col];
                        if (!taken) {
                            System.out.println("Row: " + row + " Column: " + col);
                        }
                    }
                }

                for (int i = 0; i < numPassenger; i++) {
                    System.out.println("Selecting seat for passenger " + (i + 1));
                    System.out.println("Enter Row number> ");
                    int row = scanner.nextInt();
                    System.out.println("Enter Column number> ");
                    int col = scanner.nextInt();
                    seats4[row][col] = true;
                    String seat = flightSchedule2.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    String details = flightSchedule2.getFlightSchedulePlan().getFlightNumber() + " Row: " + row + "Column: " + col;
                    System.out.println("Enter passenger first name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger last name> ");
                    details = details + " " + scanner.nextLine().trim();
                    System.out.println("Enter passenger passport number> ");
                    details = details + " " + scanner.nextLine().trim();
                    flightReservation.getSeatNumber().add(seat);
                    flightReservation.getPassengerName().add(details);

                }
                seatInventory4.setTaken(seats4);
                Set<ConstraintViolation<SeatInventory>> constraintViolations7 = validator.validate(seatInventory4);
                if (constraintViolations7.isEmpty()) {
                    try {
                        seatsInventorySessionBeanRemote.updateSeatInventory(seatInventory4);
                    } catch (UpdateSeatInventoryException ex) {
                        System.out.println("An error has occurred while while updating the seat inventory\n");
                    } catch (SeatInventoryNotFoundException ex) {
                        System.out.println("An unknown error has occurred while updating the seat inventory! Seat inventory not found: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForSeatInventory(constraintViolations7);
                }

                System.out.println("Enter creditCard details (Name, card number, expiration date, cvv)> ");
                String creditCardDetails = scanner.nextLine().trim();
                flightReservation.setCreditCard(creditCardDetails);
                List<Fare> fares = flightSchedule.getFlightSchedulePlan().getFares();
                Fare fare = new Fare();
                for (Fare fare1 : fares) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare = fare1;
                    }
                }
                double fareAmt = fare.getFareAmount() * numPassenger;
                List<Fare> fares2 = flightSchedule2.getFlightSchedulePlan().getFares();
                Fare fare2 = new Fare();
                for (Fare fare1 : fares2) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare2 = fare1;
                    }
                }
                fareAmt = fareAmt + fare2.getFareAmount() * numPassenger;
                
                List<Fare> fares3 = flightSchedule3.getFlightSchedulePlan().getFares();
                Fare fare3 = new Fare();
                for (Fare fare1 : fares3) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare3 = fare1;
                    }
                }
                
                fareAmt = fareAmt + fare3.getFareAmount() * numPassenger;
                
                List<Fare> fares4 = flightSchedule3.getFlightSchedulePlan().getFares();
                Fare fare4 = new Fare();
                for (Fare fare1 : fares4) {
                    if (fare1.getCabinClass().equals(cabinClass)) {
                        fare4 = fare1;
                    }
                }
                
                fareAmt = fareAmt + fare4.getFareAmount() * numPassenger;
                
                String fareCode = fare.getFareBasisCode();
                flightReservation.setFareAmount(fareAmt);
                flightReservation.setFareBasisCode(fareCode);

                Set<ConstraintViolation<FlightReservation>> constraintViolations1 = validator.validate(flightReservation);
                if (constraintViolations1.isEmpty()) {
                    try {
                        Long flightId = flightReservationSessionBeanRemote.createNewFlightReservation(flightReservation);
                        System.out.println("New Flight Reservation created successfully!: " + flightId + "\n");
                    } catch (FlightReservationNotFoundException ex) {
                        System.out.println("An error has occurred while creating the new flight!: The flight number already exist\n");
                    } catch (UnknownPersistenceException ex) {
                        System.out.println("An unknown error has occurred while creating the new flight!: " + ex.getMessage() + "\n");
                    } catch (InputDataValidationException ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }

                } else {
                    showInputDataValidationErrorsForFlightReservation(constraintViolations1);
                }
            }
        }

    }

    private void showInputDataValidationErrorsForFlightReservation(Set<ConstraintViolation<FlightReservation>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForSeatInventory(Set<ConstraintViolation<SeatInventory>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
