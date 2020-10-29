/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author reuben
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long createNewEmployee(Employee newEmployee){
        entityManager.persist(newEmployee);
        entityManager.flush();
        
        return newEmployee.getEmployeeId();
    }
    
    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = entityManager.createQuery("SELECT e FROM Employee e");
        
        return query.getResultList();
    }
    
    @Override
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = entityManager.find(Employee.class, employeeId);
        
        if(employee != null)
        {
            return employee;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }
    }
    
    @Override
    public Employee retrieveEmployeeByUsername(String userName) throws EmployeeNotFoundException {
        Query query = entityManager.createQuery("SELECT e FROM Employee e WHERE e.userName = :inUsername");
        query.setParameter("inUsername", userName);
        
        try
        {
            return (Employee)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee Username " + userName + " does not exist!");
        }
    }

    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try
        {
            Employee employee = retrieveEmployeeByUsername(username);
            
            if(employee.getPassword().equals(password))
            {            
                return employee;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public void updateEmployee(Employee employeeEntity) {
        entityManager.merge(employeeEntity);
    }
    
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Employee employeeToRemove = retrieveEmployeeByEmployeeId(employeeId);
        entityManager.remove(employeeToRemove);
    }
}