/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.FlightReservationSessionBeanLocal;
import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Airport;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Partner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CabinClassEnum;
import util.enumeration.PreferenceEnum;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private FlightReservationSessionBeanLocal flightReservationSessionBeanLocal;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;
    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
        System.out.println("*** HolidayReservationSystemWebService.partnerLogin(): Partner " + partner.getUserName() + " login remotely via web service");
        
        return partner;
    }
 
    @WebMethod(operationName = "searchFlightSchedule")
    public List<List<FlightSchedule>> searchFlightSchedule
            (@WebParam(name = "origin") Airport origin, 
            @WebParam(name = "destination") Airport destination, @WebParam(name = "departureDate") String departureDate, 
            @WebParam(name = "numPassenger") int numPassenger, @WebParam(name = "preferenceEnum") PreferenceEnum preferenceEnum, 
            @WebParam(name = "cabinClassEnum") CabinClassEnum cabinClassEnum)
    {
        List<List<FlightSchedule>> flightScheduleList = flightScheduleSessionBeanLocal.searchFlightSchedule(origin, destination, departureDate, numPassenger, preferenceEnum, cabinClassEnum);
         
        Query query = entityManager.createQuery("SELECT fs FROM FlightSchedule fs");
        List<FlightSchedule> allFlightSchedules = query.getResultList();
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat fsd = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String originCode = origin.getAirportCode();
        String destinationCode = destination.getAirportCode();
        
        return flightScheduleList;
    }
    
    @WebMethod(operationName = "viewFlightReservations")
    public List<FlightReservation> viewFlightReservations(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "partnerId") Long partnerId) throws InvalidLoginCredentialException {
         Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
         
         System.out.println("*** HolidayReservationSystemWebService.partnerLogin(): Partner " + partner.getUserName() + " login remotely via web service");
         
         return flightReservationSessionBeanLocal.viewFlightReservations(partnerId);
    }
    
    @WebMethod(operationName = "viewFlightReservationDetails")
    public List<FlightReservation>  viewFlightReservationDetails(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "partnerId") Long partnerId,
        @WebParam(name = "reservationId") Long reservationId) throws InvalidLoginCredentialException {
        
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
         
        System.out.println("*** HolidayReservationSystemWebService.partnerLogin(): Partner " + partner.getUserName() + " login remotely via web service");
            
        return flightReservationSessionBeanLocal.viewFlightReservationDetails(partnerId, reservationId);
    }
}
