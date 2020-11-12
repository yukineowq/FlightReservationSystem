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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Reuben Ang Wen Zheng
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String firstName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String lastName;
    @Column(nullable = false, length = 64)
    @Email(message = "Email should be valid")
    private String email;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String mobileNumber;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String address;
     @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(min = 4, max = 64)
    private String userName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 8, max = 32)
    private String password;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<FlightReservation> flightReservations;
    
    public Customer() {
        flightReservations = new ArrayList<>();
    }

    public Customer(String firstName, String lastName, String email, String mobileNumber, String address, String userName, String password, List<FlightReservation> flightReservations) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.flightReservations = flightReservations;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }
    
}
