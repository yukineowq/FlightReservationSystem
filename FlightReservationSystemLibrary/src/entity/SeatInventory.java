/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.CabinClassEnum;

/**
 *
 * @author reuben
 */
@Entity
public class SeatInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatInventoryId;
    @Column(nullable = false, length = 64)
    private int available;
    @Column(nullable = false, length = 64)
    private int reserved;
    @Column(nullable = false, length = 64)
    private int balance;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private CabinClassEnum cabinClass;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;

    public SeatInventory() {
    }

    public SeatInventory(int available, CabinClassEnum cabinClassEnum) {
        this.available = available;
        this.reserved = 0;
        this.balance = 0;
        this.cabinClass = cabinClassEnum;
    }

    
    public Long getSeatInventoryId() {
        return seatInventoryId;
    }

    public void setSeatInventoryId(Long seatInventoryId) {
        this.seatInventoryId = seatInventoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatInventoryId != null ? seatInventoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the seatInventoryId fields are not set
        if (!(object instanceof SeatInventory)) {
            return false;
        }
        SeatInventory other = (SeatInventory) object;
        if ((this.seatInventoryId == null && other.seatInventoryId != null) || (this.seatInventoryId != null && !this.seatInventoryId.equals(other.seatInventoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatInventory[ id=" + seatInventoryId + " ]";
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getReserved() {
        return reserved;
    }

    //Whenever set reserved, automatically update available and balance as well
    public void setReserved(int reserved) {
        this.available = available - reserved;
        this.reserved = reserved;
        this.balance = available - this.reserved;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public CabinClassEnum getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(CabinClassEnum cabinClass) {
        this.cabinClass = cabinClass;
    }

    
}
