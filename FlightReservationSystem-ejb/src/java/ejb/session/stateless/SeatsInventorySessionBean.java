/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.SeatInventory;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UpdateSeatInventoryException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Stateless
public class SeatsInventorySessionBean implements SeatsInventorySessionBeanRemote, SeatsInventorySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    public SeatsInventorySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public List<SeatInventory> viewSeatsInventory(){
        Query query = entityManager.createQuery("SELECT s FROM SeatInventory s");
        List<SeatInventory> seatInventories = query.getResultList();
        seatInventories.size();
        return seatInventories;
    }
    
    @Override
    public void updateSeatInventory(SeatInventory seatInventory) throws SeatInventoryNotFoundException, UpdateSeatInventoryException, InputDataValidationException {

        if (seatInventory != null && seatInventory.getSeatInventoryId() != null) {
            Set<ConstraintViolation<SeatInventory>> constraintViolations = validator.validate(seatInventory);

            if (constraintViolations.isEmpty()) {
                SeatInventory seatInventoryToUpdate = new SeatInventory();

                seatInventoryToUpdate = entityManager.find(SeatInventory.class, seatInventory.getSeatInventoryId());

                seatInventoryToUpdate.setTaken(seatInventory.getTaken());


            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new SeatInventoryNotFoundException("Seat inventory not found in records");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SeatInventory>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
}
