/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.CabinClassEnum;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;

/**
 *
 * @author reuben
 */
@Entity
public class CabinClassConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassConfigurationId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Min(0)
    private int numAisle;
    @Column(nullable = false, length = 64)
    @NotNull
    @Min(0)
    private int numRow;
    @Column(nullable = false, length = 64)
    @NotNull
    @Min(0)
    private int numSeatsAbreast;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 10)
    private String seatingConfigurationPerColumn;
    @Column(nullable = false, length = 64)
    @NotNull
    @Min(0)
    private int maxCabinSeatCapacity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private CabinClassEnum cabinClass;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    @OneToMany(mappedBy = "cabinClassConfiguration")
    private List<Fare> fares;
    

    public Long getCabinClassConfigurationId() {
        return cabinClassConfigurationId;
    }

    public CabinClassConfiguration() {
        fares = new ArrayList<>();
    }

    public CabinClassConfiguration(int numAisle, int numRow, int numSeatsAbreast, String seatingConfigurationPerColumn, CabinClassEnum cabinClass) {
        this.numAisle = numAisle;
        this.numRow = numRow;
        this.numSeatsAbreast = numSeatsAbreast;
        this.seatingConfigurationPerColumn = seatingConfigurationPerColumn;
        this.cabinClass = cabinClass;
        this.maxCabinSeatCapacity = numRow * numSeatsAbreast;
    }

    
    public void setCabinClassConfigurationId(Long cabinClassConfigurationId) {
        this.cabinClassConfigurationId = cabinClassConfigurationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassConfigurationId != null ? cabinClassConfigurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassConfigurationId fields are not set
        if (!(object instanceof CabinClassConfiguration)) {
            return false;
        }
        CabinClassConfiguration other = (CabinClassConfiguration) object;
        if ((this.cabinClassConfigurationId == null && other.cabinClassConfigurationId != null) || (this.cabinClassConfigurationId != null && !this.cabinClassConfigurationId.equals(other.cabinClassConfigurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClassConfiguration[ id=" + cabinClassConfigurationId + " ]";
    }

    public int getNumAisle() {
        return numAisle;
    }

    public void setNumAisle(int numAisle) {
        this.numAisle = numAisle;
    }

    public int getNumRow() {
        return numRow;
    }

    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }

    public int getNumSeatsAbreast() {
        return numSeatsAbreast;
    }

    public void setNumSeatsAbreast(int numSeatsAbreast) {
        this.numSeatsAbreast = numSeatsAbreast;
    }

    public String getSeatingConfigurationPerColumn() {
        return seatingConfigurationPerColumn;
    }

    public void setSeatingConfigurationPerColumn(String seatingConfigurationPerColumn) {
        this.seatingConfigurationPerColumn = seatingConfigurationPerColumn;
    }

    public int getMaxCabinSeatCapacity() {
        return maxCabinSeatCapacity;
    }

    public void setMaxCabinSeatCapacity(int maxCabinSeatCapacity) {
        this.maxCabinSeatCapacity = maxCabinSeatCapacity;
    }

    public CabinClassEnum getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(CabinClassEnum cabinClass) {
        this.cabinClass = cabinClass;
    }
    
    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }
    
    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        if(this.aircraftConfiguration != null)
        {
            try {
                this.aircraftConfiguration.removeCabinClassConfiguration(this);
            } catch (EntityInstanceMissingInCollectionException ex) {
                Logger.getLogger(CabinClassConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.aircraftConfiguration = aircraftConfiguration;
        
        if(this.aircraftConfiguration != null)
        {
            if(!this.aircraftConfiguration.getCabinClassConfigurations().contains(this))
            {
                try {
                    this.aircraftConfiguration.addCabinClassConfiguration(this);
                } catch (EntityInstanceExistsInCollectionException ex) {
                    Logger.getLogger(CabinClassConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

}
