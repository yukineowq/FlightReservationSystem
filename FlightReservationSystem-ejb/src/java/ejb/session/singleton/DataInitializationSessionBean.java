/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeAccessRightEnum;
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
        } catch (EmployeeNotFoundException ex){
            initializeData();
        }
    }
    
    private void initializeData()
    {
        try
        {
            Employee employee =  new Employee("John", "Tan", "systemadministrator", "password", EmployeeAccessRightEnum.SYSTEMADMINISTRATOR);
            employeeSessionBeanLocal.createNewEmployee(employee);

            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD001", "Product A1", "Product A1", 100, new BigDecimal("10.00"), "Category A"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD002", "Product A2", "Product A2", 100, new BigDecimal("25.50"), "Category A"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD003", "Product A3", "Product A3", 100, new BigDecimal("15.00"), "Category A"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD004", "Product B1", "Product B1", 100, new BigDecimal("20.00"), "Category B"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD005", "Product B2", "Product B2", 100, new BigDecimal("10.00"), "Category B"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD006", "Product B3", "Product B3", 100, new BigDecimal("100.00"), "Category B"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD007", "Product C1", "Product C1", 100, new BigDecimal("35.00"), "Category C"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD008", "Product C2", "Product C2", 100, new BigDecimal("20.05"), "Category C"));
            productEntitySessionBeanLocal.createNewProduct(new ProductEntity("PROD009", "Product C3", "Product C3", 100, new BigDecimal("5.50"), "Category C"));
        }
        catch(StaffUsernameExistException | ProductSkuCodeExistException | UnknownPersistenceException ex)
        {
            ex.printStackTrace();
        }
    }
    
}
