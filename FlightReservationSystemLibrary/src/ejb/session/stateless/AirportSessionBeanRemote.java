/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Remote;
import util.exception.AirportCodeExistException;
import util.exception.AirportNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author reuben
 */
@Remote
public interface AirportSessionBeanRemote {
    Long createNewAirport(Airport newAirport) throws AirportCodeExistException, UnknownPersistenceException;
    Airport retrieveAirportByAirportCode(String airportCode) throws AirportNotFoundException;
}
