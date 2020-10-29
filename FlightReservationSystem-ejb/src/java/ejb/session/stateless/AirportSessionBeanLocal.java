/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Local;

/**
 *
 * @author reuben
 */
@Local
public interface AirportSessionBeanLocal {
    Long createNewAirport(Airport newAirport);
}
