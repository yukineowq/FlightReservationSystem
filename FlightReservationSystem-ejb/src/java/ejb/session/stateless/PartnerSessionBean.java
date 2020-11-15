/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PartnerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewPartner(Partner newPartner) throws InputDataValidationException {
        Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(newPartner);
        if (constraintViolations.isEmpty()) {
            entityManager.persist(newPartner);
            entityManager.flush();

            return newPartner.getPartnerId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Partner partner = retrievePartnerByUsername(username);

            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public Partner retrievePartnerByUsername(String userName) throws PartnerNotFoundException {
        Query query = entityManager.createQuery("SELECT p FROM Partner p WHERE p.userName = :inUsername");
        query.setParameter("inUsername", userName);

        try {
            return (Partner) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Employee Username " + userName + " does not exist!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>> constraintViolations) {
    String msg = "Input data validation error!:";

    for (ConstraintViolation constraintViolation : constraintViolations) {
        msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
    }

        return msg;
    }
    
}
