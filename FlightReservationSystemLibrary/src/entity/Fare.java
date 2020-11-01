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
import util.enumeration.CabinClassEnum;

/**
 *
 * @author Yuki
 */
@Entity
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private CabinClassEnum cabinClass;
    @Column(nullable = false, length = 64)
    private String fareBasisCode;
    @Column(nullable = false, length = 64)
    private String fareAmount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    public Fare() {
    }

    public Fare(CabinClassEnum cabinClass, String fareBasisCode, String fareAmount) {
        this.cabinClass = cabinClass;
        this.fareBasisCode = fareBasisCode;
        this.fareAmount = fareAmount;
    }
    
    public Long getFareId() {
        return fareId;
    }

    public void setFareId(Long fareId) {
        this.fareId = fareId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareId != null ? fareId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fareId fields are not set
        if (!(object instanceof Fare)) {
            return false;
        }
        Fare other = (Fare) object;
        if ((this.fareId == null && other.fareId != null) || (this.fareId != null && !this.fareId.equals(other.fareId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fare[ id=" + fareId + " ]";
    }

    public CabinClassEnum getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(CabinClassEnum cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public String getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(String fareAmount) {
        this.fareAmount = fareAmount;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
}
