/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.ScheduleTypeEnum;

/**
 *
 * @author Yuki
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
    @Column(nullable = false, length = 64)
    private String flightNumber;
    @Column(nullable = false, length = 64)
    private Boolean complementaryRFSP;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private ScheduleTypeEnum scheduleType;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;
   
    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FlightReservation> flightReservations;
    
    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FlightSchedule> flightSchedule;
    
    @OneToMany(mappedBy = "flightSchedulePlan")
    private Fare fare;

    
    public FlightSchedulePlan() {
    }

    public FlightSchedulePlan(String flightNumber, List<FlightSchedule> flightSchedule) {
        this.flightNumber = flightNumber;
        this.flightSchedule = flightSchedule;
    }
    
    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightScheduleId + " ]";
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Boolean getComplementaryRFSP() {
        return complementaryRFSP;
    }

    public void setComplementaryRFSP(Boolean complementaryRFSP) {
        this.complementaryRFSP = complementaryRFSP;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }
    
}
