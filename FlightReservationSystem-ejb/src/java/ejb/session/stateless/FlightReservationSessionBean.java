/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightReservation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightRouteOriginExistException;
import util.exception.SeatNumberNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Yuki
 */
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    public Long createNewFlightReservation(FlightReservation newFlightReservation) throws FlightReservationNotFoundException, UnknownPersistenceException {
        try 
        {
            entityManager.persist(newFlightReservation);
            entityManager.flush();
            
            return newFlightReservation.getFlightReservationId();
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new FlightReservationNotFoundException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    public FlightReservation retrieveFlightReservationBySeatNumber(String seatNumber) throws SeatNumberNotFoundException {
        Query query = entityManager.createQuery("SELECT r FROM FlightReservation r WHERE r.seatNumber = :inSeatNumber");
        query.setParameter("inSeatNumber", seatNumber);
        
        try
        {
        return (FlightReservation)query.getResultList();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new SeatNumberNotFoundException("Seat number: " + seatNumber + " does not exist!");
        }
    }
    
}
