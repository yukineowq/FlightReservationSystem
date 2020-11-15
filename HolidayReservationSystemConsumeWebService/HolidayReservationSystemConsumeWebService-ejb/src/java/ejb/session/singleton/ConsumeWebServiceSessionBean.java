/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author Yuki
 */
@Singleton
@LocalBean
@Startup
public class ConsumeWebServiceSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {

        try {
            java.lang.String username = "";
            java.lang.String password = "";
            ejb.session.ws.HolidayReservationSystemWebService_Service service = new ejb.session.ws.HolidayReservationSystemWebService_Service();
            ejb.session.ws.HolidayReservationSystemWebService port = service.getHolidayReservationSystemWebServicePort();
            // TODO process result here
            ejb.session.ws.Partner result = port.partnerLogin(username, password);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            java.lang.String username1 = "";
            java.lang.String password1 = "";
            java.lang.Long partnerId = 0L;
            java.lang.Long reservationId = 0L;
            ejb.session.ws.HolidayReservationSystemWebService_Service service1 = new ejb.session.ws.HolidayReservationSystemWebService_Service();
            ejb.session.ws.HolidayReservationSystemWebService port1 = service1.getHolidayReservationSystemWebServicePort();
            // TODO process result here
            java.util.List<ejb.session.ws.FlightReservation> result = port1.viewFlightReservationDetails(username1, password1, partnerId, reservationId);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            java.lang.String username2 = "";
            java.lang.String password2 = "";
            java.lang.Long partnerId1 = 0L;
            ejb.session.ws.HolidayReservationSystemWebService_Service service2 = new ejb.session.ws.HolidayReservationSystemWebService_Service();
            ejb.session.ws.HolidayReservationSystemWebService port2 = service2.getHolidayReservationSystemWebServicePort();
            // TODO process result here
            java.util.List<ejb.session.ws.FlightReservation> result = port2.viewFlightReservations(username2, password2, partnerId1);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
