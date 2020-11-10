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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Entity
public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigurationId;
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(nullable = false, length = 64)
    @NotNull
    @Min(0)
    private int numCabinClass;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftType aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfiguration")
    private List<CabinClassConfiguration> cabinClassConfigurations;
    
    @OneToMany(mappedBy = "aircraftConfiguration")
    private List<Flight> flights;

    public Long getAircraftConfigurationId() {
        return aircraftConfigurationId;
    }

    public AircraftConfiguration() {
        this.cabinClassConfigurations = new ArrayList<>();
        this.flights = new  ArrayList<>();
    }

    public AircraftConfiguration(String name, int numCabinClass) {
        this();
        this.name = name;
        this.numCabinClass = numCabinClass;
    }

    
    public void setAircraftConfigurationId(Long aircraftConfigurationId) {
        this.aircraftConfigurationId = aircraftConfigurationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigurationId != null ? aircraftConfigurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftConfigurationId fields are not set
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.aircraftConfigurationId == null && other.aircraftConfigurationId != null) || (this.aircraftConfigurationId != null && !this.aircraftConfigurationId.equals(other.aircraftConfigurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfiguration[ id=" + aircraftConfigurationId + " ]";
    }
    
    public List<CabinClassConfiguration> getCabinClassConfigurations() {
        return cabinClassConfigurations;
    }
    
    public void addCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) throws EntityInstanceExistsInCollectionException
    {
        if(!this.cabinClassConfigurations.contains(cabinClassConfiguration))
        {
            this.cabinClassConfigurations.add(cabinClassConfiguration);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("This cabin class configuration already exist in this aircraft configuration list");
        }
    }
    
    
    
    public void removeCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) throws EntityInstanceMissingInCollectionException
    {
        if(this.cabinClassConfigurations.contains(cabinClassConfiguration))
        {
            this.cabinClassConfigurations.remove(cabinClassConfiguration);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("This cabin class configuration does not exist in this aircraft configuration list");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumCabinClass() {
        return numCabinClass;
    }

    public void setNumCabinClass(int numCabinClass) {
        this.numCabinClass = numCabinClass;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    
}
