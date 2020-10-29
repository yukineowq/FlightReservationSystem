/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import entity.AircraftType;
import entity.Employee;
import entity.Airport;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author reuben
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;
    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;
    
    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("systemadministrator");
            airportSessionBeanLocal.retrieveAirportByAirportCode("SIN");
            airportSessionBeanLocal.retrieveAirportByAirportCode("NRT");
            aircraftTypeSessionBeanLocal.retrieveAircraftTypeByName("Boeing 737");
            aircraftTypeSessionBeanLocal.retrieveAircraftTypeByName("Boeing 747");
        } catch (EmployeeNotFoundException | AirportNotFoundException | AircraftTypeNotFoundException ex){
            initializeData();
        }
    }
    
    private void initializeData()
    {

            Employee employee =  new Employee("John", "Tan", "systemadministrator", "password", EmployeeAccessRightEnum.SYSTEMADMINISTRATOR);
            employeeSessionBeanLocal.createNewEmployee(employee);

            airportSessionBeanLocal.createNewAirport(new Airport("Changi Airport", "SIN", "Singapore", "Singapore", "Singapore", Long.valueOf(8)));
            airportSessionBeanLocal.createNewAirport(new Airport("Narita International Airport", "NRT", "Narita", "Chiba", "Japan", Long.valueOf(9)));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing 737", Long.valueOf(204)));
            aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing 747", Long.valueOf(467)));
            
        
    }
    
}
