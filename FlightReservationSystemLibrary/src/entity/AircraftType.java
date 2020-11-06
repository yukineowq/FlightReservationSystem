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
public class AircraftType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftTypeId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private Long maxSeatCapacity;

    @OneToMany(mappedBy = "aircraftType")
    private List<AircraftConfiguration> aircraftConfigurations;

    public AircraftType() {
        aircraftConfigurations = new ArrayList<>();
    }

    public AircraftType(String name, Long maxSeatCapacity) {
        this();
        this.name = name;
        this.maxSeatCapacity = maxSeatCapacity;
    }
    
    
    
    public Long getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(Long aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftTypeId != null ? aircraftTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftTypeId fields are not set
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.aircraftTypeId == null && other.aircraftTypeId != null) || (this.aircraftTypeId != null && !this.aircraftTypeId.equals(other.aircraftTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftType[ id=" + aircraftTypeId + " ]";
    }
        public String getName() {
        return name;
    }
        
    public void addAircraftConfiguration(AircraftConfiguration aircraftConfiguration) throws EntityInstanceExistsInCollectionException
    {
        if(!this.aircraftConfigurations.contains(aircraftConfiguration))
        {
            this.getAircraftConfigurations().add(aircraftConfiguration);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("This aircraft configuration already exist for this aircraft type");
        }
    }
    
    
    
    public void removeAircraftConfiguration(AircraftConfiguration aircraftConfiguration) throws EntityInstanceMissingInCollectionException
    {
        if(this.getAircraftConfigurations().contains(aircraftConfiguration))
        {
            this.getAircraftConfigurations().remove(aircraftConfiguration);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("This aircraft configuration does not exist for this aircraft type");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMaxSeatCapacity() {
        return maxSeatCapacity;
    }

    public void setMaxSeatCapacity(Long maxSeatCapacity) {
        this.maxSeatCapacity = maxSeatCapacity;
    }

    public List<AircraftConfiguration> getAircraftConfigurations() {
        return aircraftConfigurations;
    }

    public void setAircraftConfigurations(List<AircraftConfiguration> aircraftConfigurations) {
        this.aircraftConfigurations = aircraftConfigurations;
    }
}
