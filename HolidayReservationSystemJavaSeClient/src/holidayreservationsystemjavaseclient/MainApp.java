/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemjavaseclient;

import java.util.List;
import java.util.Scanner;
import ws.client.partner.FlightReservation;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.Partner;

/**
 *
 * @author Yuki
 */
public class MainApp {
     public void runApp()  {
    
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Holiday Reservation System Java SE Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Flight");
            System.out.println("3: Reserve Flight");
            System.out.println("4: View Flight Reservations");
            System.out.println("5: View Flight Reservation Details");            
            System.out.println("6: Exit\n");
            response = 0;
            
            while(response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doLogin(scanner);
                }
                else if (response == 2) {
                    //searchFlight(scanner);
                }
                else if (response == 3) {
                    reserveFlight(scanner);
                }
                else if (response == 4){
                    viewAllFlightReservations(scanner);
                }
                else if (response == 5) {
                    viewFlightResevationDetails(scanner);
                }
                else if (response == 6)
                {
                    break;
                }
                else {
                    System.out.print("Invalid option, please try again!\n");  
                }
            }
            
            if (response == 6) 
            {
                break;
            }
        }
    }
    
    public void doLogin(Scanner scanner)
    {
        System.out.println("*** Holiday Reservation System Java SE Client :: 1 - Login ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        try 
        {
            partnerLogin(username, password);
        }
        catch(InvalidLoginCredentialException_Exception ex) 
        {
            System.out.println("Missing login credential!");
        }
        
    }
    
//    public void searchFlight(Scanner scanner) {
//        System.out.println("*** Holiday Reservation System Java SE Client :: 2 - Search Flight ***\n");
//
//        String departureDate = "";
//        String returnDate = "";
//        int numPassenger = 1;
//        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        System.out.println("Select trip type: ");
//        System.out.println("1: One-way");
//        System.out.println("2: Round-trip");
//        
//        int res = 0;
//        int trip = 0;
//        while (trip < 1 || trip > 2) {
//            System.out.print("> ");
//            trip = scanner.nextInt();
//            scanner.nextLine();
//        }
//    }
    
    public void reserveFlight(Scanner scanner) {
        System.out.println("*** Holiday Reservation System Java SE Client :: 3 - Reserve Flight ***\n");
    }
    
    public void viewAllFlightReservations(Scanner scanner) {
        System.out.println("*** Holiday Reservation System Java SE Client :: 4 - View All Flight Reservations ***\n");
       
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter partnerId> ");
        Long partnerId = scanner.nextLong();
        
        try
        {
            List<FlightReservation> flightReservations = viewFlightReservations(username, password, partnerId);
            
            for (FlightReservation flightReservation : flightReservations) {
                System.out.println("Flight Reservation: " + flightReservation.getFlightReservationId() + "; " 
                        + flightReservation.getFareAmount() + "; " + flightReservation.getCabinClassEnum());
            }
        }
        catch (InvalidLoginCredentialException_Exception ex) 
        {
            System.out.println("An error has occured while retrieving flight reservations: " + ex.getMessage());
        }
    }
    
    public void viewFlightResevationDetails(Scanner scanner) {
        System.out.println("*** Holiday Reservation System Java SE Client :: 5 - View Flight Reservation Details ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter partnerId> ");
        Long partnerId = scanner.nextLong();
        System.out.print("Enter reservationId> ");
        Long reservationId = scanner.nextLong();
        
        try 
        {
            List<FlightReservation> flightReservations = viewFlightReservationDetails(username, password, partnerId, reservationId);
            
            for (FlightReservation flightReservation : flightReservations) {
                System.out.println("Flight Reservation: " + flightReservation.getFlightReservationId() + "; " 
                        + flightReservation.getFareAmount());
            }
        }
        catch (InvalidLoginCredentialException_Exception ex)
        {
            System.out.println("An error has occured while retrieving flight reservations: " + ex.getMessage());
        }
    }

    private static Partner partnerLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception {
        ws.client.partner.HolidayReservationSystemWebService_Service service = new ws.client.partner.HolidayReservationSystemWebService_Service();
        ws.client.partner.HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.partnerLogin(username, password);
    }

    private static java.util.List<ws.client.partner.FlightReservation> viewFlightReservationDetails(java.lang.String username, java.lang.String password, java.lang.Long partnerId, java.lang.Long reservationId) throws InvalidLoginCredentialException_Exception {
        ws.client.partner.HolidayReservationSystemWebService_Service service = new ws.client.partner.HolidayReservationSystemWebService_Service();
        ws.client.partner.HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.viewFlightReservationDetails(username, password, partnerId, reservationId);
    }

    private static java.util.List<ws.client.partner.FlightReservation> viewFlightReservations(java.lang.String username, java.lang.String password, java.lang.Long partnerId) throws InvalidLoginCredentialException_Exception {
        ws.client.partner.HolidayReservationSystemWebService_Service service = new ws.client.partner.HolidayReservationSystemWebService_Service();
        ws.client.partner.HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
        return port.viewFlightReservations(username, password, partnerId);
    }
    
    
    
}
