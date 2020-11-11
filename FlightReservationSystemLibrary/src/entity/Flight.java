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
import static javax.persistence.CascadeType.PERSIST;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.omg.CORBA.PERSIST_STORE;
import util.enumeration.StatusEnum;

/**
 *
 * @author Reuben
 */
@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String flightNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private StatusEnum status;
    
    @OneToMany(mappedBy = "flight", cascade = CascadeType.PERSIST)
    private List<FlightSchedulePlan> flightSchedulePlan; 
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = false)
    private FlightRoute flightRoute;
    
    @ManyToMany
    @JoinColumn(nullable = false)
    private List<CabinClassConfiguration> cabinClassConfiguration;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    @Column(nullable = false, length = 64)
    private String complementaryFlightNumber;

    public Flight() {
        flightSchedulePlan = new ArrayList<>();
        cabinClassConfiguration = new ArrayList<>();
        this.status = StatusEnum.ENABLED;
        this.complementaryFlightNumber = "";
    }

    public Flight(String flightNumber) {
        this();
        this.flightNumber = flightNumber;       
    }
    
    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightId != null ? flightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightId fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightId == null && other.flightId != null) || (this.flightId != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightId + " ]";
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public List<FlightSchedulePlan> getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(List<FlightSchedulePlan> flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }

    public String getComplementaryFlightNumber() {
        return complementaryFlightNumber;
    }

    public void setComplementaryFlightNumber(String complementaryFlightNumber) {
        this.complementaryFlightNumber = complementaryFlightNumber;
    }

    public List<CabinClassConfiguration> getCabinClassConfiguration() {
        return cabinClassConfiguration;
    }

    public void setCabinClassConfiguration(List<CabinClassConfiguration> cabinClassConfiguration) {
        this.cabinClassConfiguration = cabinClassConfiguration;
    }


    
    
}
