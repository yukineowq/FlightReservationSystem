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
import javax.ejb.EJB;

/**
 *
 * @author reuben
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static FlightSessionBeanRemote flightSessionBeanRemote;
    @EJB
    private static ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote;
    @EJB
    private static FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    public static void main(String[] args) {
       MainApp mainApp = new MainApp(customerSessionBeanRemote, flightSessionBeanRemote, reserveFlightSessionBeanRemote, flightReservationSessionBeanRemote);
       mainApp.runApp();
    }
    
}
