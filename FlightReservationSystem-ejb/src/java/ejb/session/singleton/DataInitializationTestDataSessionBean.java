/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.AircraftConfigurationSessionBeanLocal;
import ejb.session.stateless.CabinClassConfigurationSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.FareSessionBeanLocal;
import entity.AircraftConfiguration;

import entity.AircraftType;
import entity.Airport;
import entity.CabinClassConfiguration;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.CabinClassEnum;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.AircraftConfigurationNameExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportCodeExistException;
import util.exception.AirportNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationTestDataSessionBean {
    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;
    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;
    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBeanLocal;
    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;
    @EJB
    private CabinClassConfigurationSessionBeanLocal cabinClassConfigurationSessionBeanLocal;
    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;
    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBeanLocal;
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;
    @EJB
    private FareSessionBeanLocal fareSessionBeanLocal;
    
    public DataInitializationTestDataSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("systemadministrator");
            employeeSessionBeanLocal.retrieveEmployeeByUsername("fleetmanager");
            employeeSessionBeanLocal.retrieveEmployeeByUsername("routeplanner");
            employeeSessionBeanLocal.retrieveEmployeeByUsername("schedulemanager");
            employeeSessionBeanLocal.retrieveEmployeeByUsername("salesmanager");
            airportSessionBeanLocal.retrieveAirportByAirportCode("SIN");
            airportSessionBeanLocal.retrieveAirportByAirportCode("NRT");
            aircraftTypeSessionBeanLocal.retrieveAircraftTypeByName("Boeing 737");
            aircraftTypeSessionBeanLocal.retrieveAircraftTypeByName("Boeing 747");
        } catch (EmployeeNotFoundException | AirportNotFoundException | AircraftTypeNotFoundException ex) {
            try {
                initializeData();
            } catch (Exception ex1) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    private void initializeData() throws AirportCodeExistException, UnknownPersistenceException, InputDataValidationException {
        
        Employee employee = new Employee("Fleet", "Manager", "fleetmanager", "password", EmployeeAccessRightEnum.FLEETMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("Route", "Planner", "routeplanner", "password", EmployeeAccessRightEnum.ROUTEPLANNER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("Schedule", "Manager", "schedulemanager", "password", EmployeeAccessRightEnum.SCHEDULEMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("Sales", "Manager", "salesmanager", "password", EmployeeAccessRightEnum.SALESMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        
        airportSessionBeanLocal.createNewAirport(new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore", 8));
        airportSessionBeanLocal.createNewAirport(new Airport("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China", 8));
        airportSessionBeanLocal.createNewAirport(new Airport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C.", 8));
        airportSessionBeanLocal.createNewAirport(new Airport("Narita", "NRT", "Narita", "Chiba", "Japan", 9));
        airportSessionBeanLocal.createNewAirport(new Airport("Sydney", "SYD", "Sydney", "New South Wales", "Australia", 11));
        aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing 737", 200));
        aircraftTypeSessionBeanLocal.createNewAircraftType(new AircraftType("Boeing 747", 400));
        Long ac1 = 0L;
        Long ac2 = 0L;
        Long ac3 = 0L;
        Long ac4 = 0L;
        
        Long cc1 = 0L;
        Long cc2 = 0L;
        Long cc3 = 0L;
        Long cc4 = 0L;
        Long cc5 = 0L;
        Long cc6 = 0L;
        Long cc7 = 0L;
        Long cc8 = 0L;
        
        try {
            ac1 = aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(new AircraftConfiguration("Boeing 737 All Economy", 1), "Boeing 737");
        } catch (AircraftConfigurationNameExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        cc1 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(1, 30, 6, "3-3", CabinClassEnum.ECONOMY), ac1);
        try {
            ac2 = aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(new AircraftConfiguration("Boeing 737 Three Classes", 3), "Boeing 737");
        } catch (AircraftConfigurationNameExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        cc2 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(1, 5, 2, "1-1", CabinClassEnum.FIRSTCLASS), ac2);
        cc3 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(1, 5, 4, "2-2", CabinClassEnum.BUSINESS), ac2);
        cc4 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(1, 25, 6, "3-3", CabinClassEnum.ECONOMY), ac2);
        try {
            ac3 = aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(new AircraftConfiguration("Boeing 747 All Economy", 1), "Boeing 747");
        } catch (AircraftConfigurationNameExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cc5 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(2, 38, 10, "3-4-3", CabinClassEnum.ECONOMY), ac3);
        try {
            ac4 = aircraftConfigurationSessionBeanLocal.createNewAircraftConfiguration(new AircraftConfiguration("Boeing 747 Three Classes", 3), "Boeing 747");
        } catch (AircraftConfigurationNameExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cc6 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(1, 5, 2, "1-1", CabinClassEnum.FIRSTCLASS), ac4);
        cc7 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(2, 5, 6, "2-2-2", CabinClassEnum.BUSINESS), ac4);
        cc8 = cabinClassConfigurationSessionBeanLocal.createNewCabinClassConfiguration(new CabinClassConfiguration(2, 32, 10, "3-4-3", CabinClassEnum.ECONOMY), ac4);
        
        Airport origin = new Airport();
        Airport destination = new Airport();
        Long fr1 = 0L;
        Long fr2 = 0L;
        Long fr3 = 0L;
        Long fr4 = 0L;
        Long fr5 = 0L;
        Long fr6 = 0L;
        Long fr7 = 0L;
        Long fr8 = 0L;
        Long fr9 = 0L;
        Long fr10 = 0L;
        Long fr11 = 0L;
        Long fr12 = 0L;
        Long fr13 = 0L;
        Long fr14 = 0L;
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("SIN");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("HKG");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr1 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "SIN", "HKG");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr2 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "HKG", "SIN");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("SIN");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("TPE");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr3 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "SIN", "TPE");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr4 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "TPE", "SIN");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("SIN");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("NRT");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr5 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "SIN", "NRT");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr6 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "NRT", "SIN");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("HKG");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("NRT");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr7 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "HKG", "NRT");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr8 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "NRT", "HKG");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("TPE");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("NRT");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr9 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "TPE", "NRT");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr10 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "NRT", "TPE");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("SIN");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("SYD");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr11 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "SIN", "SYD");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr12 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "SYD", "SIN");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            origin = airportSessionBeanLocal.retrieveAirportByAirportCode("SYD");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            destination = airportSessionBeanLocal.retrieveAirportByAirportCode("NRT");
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr13 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(origin, destination, true), "SYD", "NRT");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fr14 = flightRouteSessionBeanLocal.createNewFlightRoute(new FlightRoute(destination, origin, true), "NRT", "SYD");
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AirportNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        FlightRoute flightRoute = new FlightRoute();
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();
        List<CabinClassConfiguration> ccc1 = new ArrayList<>();
        List<CabinClassConfiguration> ccc2 = new ArrayList<>();
        List<CabinClassConfiguration> ccc3 = new ArrayList<>();
        List<CabinClassConfiguration> ccc4 = new ArrayList<>();
        
        Long f1 = 0L;
        Long f2 = 0L;
        Long f3 = 0L;
        Long f4 = 0L;
        Long f5 = 0L;
        Long f6 = 0L;
        Long f7 = 0L;
        Long f8 = 0L;
        Long f9 = 0L;
        Long f10 = 0L;
        Long f11 = 0L;
        Long f12 = 0L;
        Long f13 = 0L;
        Long f14 = 0L;
        Long f15 = 0L;
        Long f16 = 0L;
        
        Flight flight1 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr1);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight1.setAircraftConfiguration(aircraftConfiguration);
        flight1.setFlightRoute(flightRoute);
        flight1.setFlightNumber("ML111");
        flight1.setComplementaryFlightNumber("ML112");
        
        ccc2 = cabinClassConfigurationSessionBeanLocal.retrieveCabinClassConfigurationsByAircraftConfiguration("Boeing 737 Three Classes");
        flight1.setCabinClassConfiguration(ccc2);
        
        try {
            f1 = flightSessionBeanLocal.createNewFlight(flight1);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight2 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr2);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight2.setAircraftConfiguration(aircraftConfiguration);
        flight2.setFlightRoute(flightRoute);
        flight2.setFlightNumber("ML112");
        flight2.setCabinClassConfiguration(ccc2);
        try {
            f2 = flightSessionBeanLocal.createNewFlight(flight2);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight3 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr3);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight3.setAircraftConfiguration(aircraftConfiguration);
        flight3.setFlightRoute(flightRoute);
        flight3.setFlightNumber("ML211");
        flight3.setComplementaryFlightNumber("ML212");
        flight3.setCabinClassConfiguration(ccc2);
        try {
            f3 = flightSessionBeanLocal.createNewFlight(flight3);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight4 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr4);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight4.setAircraftConfiguration(aircraftConfiguration);
        flight4.setFlightRoute(flightRoute);
        flight4.setFlightNumber("ML212");
        flight4.setCabinClassConfiguration(ccc2);
        try {
            f4 = flightSessionBeanLocal.createNewFlight(flight4);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight5 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr5);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 747 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight5.setAircraftConfiguration(aircraftConfiguration);
        flight5.setFlightRoute(flightRoute);
        flight5.setFlightNumber("ML311");
        flight5.setComplementaryFlightNumber("ML312");
        ccc4 = cabinClassConfigurationSessionBeanLocal.retrieveCabinClassConfigurationsByAircraftConfiguration("Boeing 747 Three Classes");
        flight5.setCabinClassConfiguration(ccc4);
        try {
            f5 = flightSessionBeanLocal.createNewFlight(flight5);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight6 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr6);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 747 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight6.setAircraftConfiguration(aircraftConfiguration);
        flight6.setFlightRoute(flightRoute);
        flight6.setFlightNumber("ML312");
        flight6.setCabinClassConfiguration(ccc4);
        try {
            f6 = flightSessionBeanLocal.createNewFlight(flight6);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight7 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr7);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight7.setAircraftConfiguration(aircraftConfiguration);
        flight7.setFlightRoute(flightRoute);
        flight7.setFlightNumber("ML411");
        flight7.setComplementaryFlightNumber("ML412");
        flight7.setCabinClassConfiguration(ccc2);
        try {
            f7 = flightSessionBeanLocal.createNewFlight(flight7);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight8 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr8);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight8.setAircraftConfiguration(aircraftConfiguration);
        flight8.setFlightRoute(flightRoute);
        flight8.setFlightNumber("ML412");
        flight8.setCabinClassConfiguration(ccc2);
        try {
            f8 = flightSessionBeanLocal.createNewFlight(flight8);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight9 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr9);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight9.setAircraftConfiguration(aircraftConfiguration);
        flight9.setFlightRoute(flightRoute);
        flight9.setFlightNumber("ML511");
        flight9.setComplementaryFlightNumber("ML512");
        flight9.setCabinClassConfiguration(ccc2);
        try {
            f9 = flightSessionBeanLocal.createNewFlight(flight9);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight10 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr10);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight10.setAircraftConfiguration(aircraftConfiguration);
        flight10.setFlightRoute(flightRoute);
        flight10.setFlightNumber("ML512");
        flight10.setCabinClassConfiguration(ccc2);
        try {
            f10 = flightSessionBeanLocal.createNewFlight(flight10);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight11 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr11);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight11.setAircraftConfiguration(aircraftConfiguration);
        flight11.setFlightRoute(flightRoute);
        flight11.setFlightNumber("ML611");
        flight11.setComplementaryFlightNumber("ML612");
        flight11.setCabinClassConfiguration(ccc2);
        try {
            f1 = flightSessionBeanLocal.createNewFlight(flight11);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight12 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr12);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight12.setAircraftConfiguration(aircraftConfiguration);
        flight12.setFlightRoute(flightRoute);
        flight12.setFlightNumber("ML612");
        flight12.setCabinClassConfiguration(ccc2);
        try {
            f12 = flightSessionBeanLocal.createNewFlight(flight12);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight13 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr11);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 All Economy");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight13.setAircraftConfiguration(aircraftConfiguration);
        flight13.setFlightRoute(flightRoute);
        flight13.setFlightNumber("ML621");
        flight13.setComplementaryFlightNumber("ML622");
        ccc1 = cabinClassConfigurationSessionBeanLocal.retrieveCabinClassConfigurationsByAircraftConfiguration("Boeing 737 All Economy");
        flight13.setCabinClassConfiguration(ccc1);
        try {
            f13 = flightSessionBeanLocal.createNewFlight(flight13);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight14 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr12);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 All Economy");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight14.setAircraftConfiguration(aircraftConfiguration);
        flight14.setFlightRoute(flightRoute);
        flight14.setFlightNumber("ML622");
        flight14.setCabinClassConfiguration(ccc1);
        try {
            f14 = flightSessionBeanLocal.createNewFlight(flight14);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight15 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr13);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight15.setAircraftConfiguration(aircraftConfiguration);
        flight15.setFlightRoute(flightRoute);
        flight15.setFlightNumber("ML711");
        flight15.setComplementaryFlightNumber("ML711");
        flight15.setCabinClassConfiguration(ccc2);
        try {
            f1 = flightSessionBeanLocal.createNewFlight(flight15);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Flight flight16 = new Flight();
        try {
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByFlightRouteId(fr14);
        } catch (FlightRouteNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aircraftConfiguration = aircraftConfigurationSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
        } catch (AircraftConfigurationNotFoundException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        flight16.setAircraftConfiguration(aircraftConfiguration);
        flight16.setFlightRoute(flightRoute);
        flight16.setFlightNumber("ML712");
        flight16.setCabinClassConfiguration(ccc2);
        try {
            f1 = flightSessionBeanLocal.createNewFlight(flight16);
        } catch (FlightNumberExistException ex) {
            Logger.getLogger(DataInitializationTestDataSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        FlightSchedulePlan flightSchedulePlan1 = new FlightSchedulePlan();
        
        Fare fare11 = new Fare(CabinClassEnum.FIRSTCLASS, "F001", 6500);
        flightSchedulePlan1.getFares().add(fare11);
        Fare fare12 = new Fare(CabinClassEnum.FIRSTCLASS, "F00", 6000);
        flightSchedulePlan1.getFares().add(fare12);
        Fare fare13 = new Fare(CabinClassEnum.BUSINESS, "J001", 3500);
        flightSchedulePlan1.getFares().add(fare13);
        Fare fare14 = new Fare(CabinClassEnum.BUSINESS, "J002", 3000);
        flightSchedulePlan1.getFares().add(fare14);
        Fare fare15 = new Fare(CabinClassEnum.ECONOMY, "Y001", 1500);
        flightSchedulePlan1.getFares().add(fare15);
        Fare fare16 = new Fare(CabinClassEnum.ECONOMY, "Y001", 1000);
        flightSchedulePlan1.getFares().add(fare16);
        
    }
}
