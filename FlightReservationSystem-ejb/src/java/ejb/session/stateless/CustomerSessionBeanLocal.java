/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Local
public interface CustomerSessionBeanLocal {
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException; 
    public Long createNewCustomer(Customer customer) throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException;
}
