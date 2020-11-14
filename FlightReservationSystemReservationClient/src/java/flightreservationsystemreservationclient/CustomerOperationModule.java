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
import entity.Customer;

/**
 *
 * @author reuben
 */
public class CustomerOperationModule {
    private Customer customer;
    
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;

    public CustomerOperationModule() {
    }

    public CustomerOperationModule(Customer customer, FlightSessionBeanRemote flightSessionBeanRemote, ReserveFlightSessionBeanRemote reserveFlightSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote) {
        this();
        this.customer = customer;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.reserveFlightSessionBeanRemote = reserveFlightSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
    }
    
    
}
