/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "customerLoginUnmanaged")
    public Customer customerLogin(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
}
