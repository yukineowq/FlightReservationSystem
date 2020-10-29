/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author reuben
 */
@Remote
public interface EmployeeSessionBeanRemote {
    Long createNewEmployee(Employee newEmployee);
    
    List<Employee> retrieveAllEmployees();
    
    Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
    
    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    void updateEmployee(Employee employee);
    
    void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;
}
