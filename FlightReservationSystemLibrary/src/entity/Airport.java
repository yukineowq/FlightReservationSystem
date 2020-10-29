/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;

/**
 *
 * @author reuben
 */
@Entity
public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, unique = true, length = 3)
    private String airportCode;
    @Column(nullable = false, length = 64)
    private String city;
    @Column(nullable = false, length = 64)
    private String state;
    @Column(nullable = false, length = 64)
    private String country;
    @Column(nullable = false, length = 64)
    private Long GMT;

    //List of flight routes with this airport as an origin
    @OneToMany(mappedBy = "origin")
    private List<FlightRoute> flightRouteOrigins;
    
    //List of flight routes with this airport as the destination
    @OneToMany(mappedBy = "destination")
    private List<FlightRoute> flightRouteDestinations;
    
    public Airport() {
        flightRouteOrigins = new ArrayList<>();
        flightRouteDestinations = new ArrayList<>();
    }

    public Airport(String name, String airportCode, String city, String state, String country, Long GMT) {
        this.name = name;
        this.airportCode = airportCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.GMT = GMT;
    }

    
    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {
        this.airportId = airportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportId != null ? airportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the airportId fields are not set
        if (!(object instanceof Airport)) {
            return false;
        }
        Airport other = (Airport) object;
        if ((this.airportId == null && other.airportId != null) || (this.airportId != null && !this.airportId.equals(other.airportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Airport[ id=" + airportId + " ]";
    }
    
    public void addFlightRouteOrigin(FlightRoute flightRoute) throws EntityInstanceExistsInCollectionException
    {
        if(!this.flightRouteOrigins.contains(flightRoute))
        {
            this.flightRouteOrigins.add(flightRoute);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("This flight route already exist in this airports's origin list");
        }
    }
    
    
    
    public void removeFlightRouteOrigin(FlightRoute flightRoute) throws EntityInstanceMissingInCollectionException
    {
        if(this.flightRouteOrigins.contains(flightRoute))
        {
            this.flightRouteOrigins.remove(flightRoute);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("This flight route does not exist in this airports's origin list");
        }
    }
    
    public void addFlightRouteDestination(FlightRoute flightRoute) throws EntityInstanceExistsInCollectionException
    {
        if(!this.flightRouteDestinations.contains(flightRoute))
        {
            this.flightRouteDestinations.add(flightRoute);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("This flight route already exist in this airports's origin list");
        }
    }
    
    
    
    public void removeFlightRouteDestination(FlightRoute flightRoute) throws EntityInstanceMissingInCollectionException
    {
        if(this.flightRouteDestinations.contains(flightRoute))
        {
            this.flightRouteDestinations.remove(flightRoute);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("This flight route does not exist in this airports's origin list");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getGMT() {
        return GMT;
    }

    public void setGMT(Long GMT) {
        this.GMT = GMT;
    }
    
}
