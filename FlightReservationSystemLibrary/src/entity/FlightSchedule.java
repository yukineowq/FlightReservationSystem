/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yuki Neo Wei Qian
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;

    @Column(nullable = false)
    private Calendar departureTime;

    @Column(nullable = false)
    private Calendar arrivalTime;
    
    @Column(nullable = false)
    private String departureDate;
    
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private int estimatedFlightHour;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private int estimatedFlightMinute;
    
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    @OneToMany(mappedBy = "flightSchedule", cascade = CascadeType.PERSIST)
    private List<FlightReservation> flightReservations;
    
    @OneToMany(mappedBy = "flightSchedule", cascade = CascadeType.PERSIST)
    private List<SeatInventory> seatInventories;

    
    public FlightSchedule() {
        flightReservations = new ArrayList<>();
        seatInventories = new ArrayList<>();
    }

    public FlightSchedule(GregorianCalendar departureTime, int estimatedHour, int estimatedMins) {
        this();
        this.departureTime = departureTime;
        this.estimatedFlightHour = estimatedHour;
        this.estimatedFlightMinute = estimatedMins;
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
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightScheduleId + " ]";
    }


    public Calendar getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    
    
    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }


    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<SeatInventory> getSeatInventories() {
        return seatInventories;
    }

    public void setSeatInventories(List<SeatInventory> seatInventories) {
        this.seatInventories = seatInventories;
    }

    public int getEstimatedFlightHour() {
        return estimatedFlightHour;
    }

    public void setEstimatedFlightHour(int estimatedFlightHour) {
        this.estimatedFlightHour = estimatedFlightHour;
    }

    public int getEstimatedFlightMinute() {
        return estimatedFlightMinute;
    }

    public void setEstimatedFlightMinute(int estimatedFlightMinute) {
        this.estimatedFlightMinute = estimatedFlightMinute;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }


    
}
