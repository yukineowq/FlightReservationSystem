/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.ScheduleTypeEnum;
import util.enumeration.StatusEnum;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 10)
    private String flightNumber;
    @Column(nullable = false, length = 64)
    private StatusEnum status;
    @Column(nullable = false, length = 64)
    private Boolean complementaryRFSP;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    @NotNull
    private ScheduleTypeEnum scheduleType;
    
    @Column(nullable = false)
    private String firstDepartureDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;
    
    @OneToMany(mappedBy = "flightSchedulePlan", cascade = CascadeType.PERSIST)
    private List<FlightSchedule> flightSchedules;
    
    @OneToMany(mappedBy = "flightSchedulePlan", cascade = CascadeType.PERSIST)
    private List<Fare> fares;

    @Column(nullable = false, length = 64)
    private long complementaryID;
    
    public FlightSchedulePlan() {
        flightSchedules = new ArrayList<>();
        fares = new ArrayList<>();
        this.complementaryRFSP = false;
        this.status = StatusEnum.ENABLED;
        this.complementaryID = -1L;
    }

    public FlightSchedulePlan(ScheduleTypeEnum scheduleType) {
        this();
        
        this.scheduleType = scheduleType;
        
    }
    
    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null) || (this.flightSchedulePlanId != null && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightSchedulePlanId + " ]";
    }

    public Boolean getComplementaryRFSP() {
        return complementaryRFSP;
    }

    public void setComplementaryRFSP(Boolean complementaryRFSP) {
        this.complementaryRFSP = complementaryRFSP;
    }

    public ScheduleTypeEnum getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleTypeEnum scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public long getComplementaryID() {
        return complementaryID;
    }

    public void setComplementaryID(long complementaryID) {
        this.complementaryID = complementaryID;
    }

    public String getFirstDepartureDate() {
        return firstDepartureDate;
    }

    public void setFirstDepartureDate(String firstDepartureDate) {
        this.firstDepartureDate = firstDepartureDate;
    }

    
    
}
