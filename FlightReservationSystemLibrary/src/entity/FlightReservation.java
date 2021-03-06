/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.CabinClassEnum;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Entity
public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightReservationId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private List<String> seatNumber;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private List<String> passengerName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String fareBasisCode;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String creditCard;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private CabinClassEnum cabinClassEnum;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Min(0)
    private double fareAmount;
    
     @ManyToMany
    @JoinColumn(nullable = false)
    private List<FlightSchedule> flightSchedules;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Partner partner;

    
    public FlightReservation() {
        seatNumber = new ArrayList<>();
        passengerName = new ArrayList<>();
        flightSchedules = new ArrayList<>();
    }

    public FlightReservation(String fareBasisCode) {

        this.fareBasisCode = fareBasisCode;
    }

    public Long getFlightReservationId() {
        return flightReservationId;
    }

    public void setFlightReservationId(Long flightReservationId) {
        this.flightReservationId = flightReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightReservationId != null ? flightReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightReservationId fields are not set
        if (!(object instanceof FlightReservation)) {
            return false;
        }
        FlightReservation other = (FlightReservation) object;
        if ((this.flightReservationId == null && other.flightReservationId != null) || (this.flightReservationId != null && !this.flightReservationId.equals(other.flightReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightReservation[ id=" + flightReservationId + " ]";
    }

    public List<String> getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(List<String> seatNumber) {
        this.seatNumber = seatNumber;
    }

    public List<String> getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(List<String> passengerName) {
        this.passengerName = passengerName;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public List<FlightSchedule> getFlightSchedule() {
        return flightSchedules;
    }

    public void setFlightSchedule(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public CabinClassEnum getCabinClassEnum() {
        return cabinClassEnum;
    }

    public void setCabinClassEnum(CabinClassEnum cabinClassEnum) {
        this.cabinClassEnum = cabinClassEnum;
    }
    
}
