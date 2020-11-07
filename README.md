# IS2103 Enterprise Systems Server-Side Design and Development (Pair Project 2020)
This pair project is one of NUS School of Computing (Information Systems [IS] major) core IS modules.

# High-level architecture of Flight Reservation System [FRS] 
This project also known as FRS, consists of 3 parts:
1. FRS Management Client
2. FRS Reservation Client
3. Holiday Reservation System

![Alt Text](https://i.imgur.com/ufWw3SG.png)

## FRS Management Client (for Merlion Airlines Employees)
### Data Initialisation [FRS System]
- [X] Create New Employee
- [X] Create New Partner
- [X] Create New Airport
- [X] Create New Aircraft Type

### Employee Session Bean [FRS Management Client]
- [X] Employee Login
- [X] Employee Logout

### Aircraft Configuration Session Bean [Flight Planning Module]
- [X] Create Aircraft Configuration
- [X] View All Aircraft Configurations
- [X] View All Aircraft Configuration Details

### Flight Route Session Bean [Flight Planning Module]
- [X] Create Flight Route
- [X] View All Flight Routes
- [X] Delete Flight Route

### Flight Session Bean [Flight Operation Module]
- [X] Create Flight
- [X] View All Flights
- [X] View Flight Details
- [X] Update Flight
- [X] Delete Flight
- [ ] Search Flight 
- [ ] Reserve Flight

### Flight Schedule Plan Session Bean [Flight Operation Module]
- [X] Create Flight Schedule Plan
- [X] View All Flight Schedule Plans
- [ ] View Flight Schedule Plan Details
- [ ] Update Flight Schedule Plan
- [ ] Delete Flight Schedule Plan

### Seats Inventory Session Bean [Sales Management Module]
- [ ] View Seats Inventory

### Flight Reservations Session Bean [Sales Management Module]
- [X] View Flight Reservations
- [ ] View My Flight Reservations
- [ ] View My Flight Reservation Details

## FRS Reservation Client (for Customers)
### Customer Session Bean 
- [ ] Register As Customer
- [ ] Customer Login
- [ ] Customer Logout

*(to include Domain Class Diagram)*

## Holiday Resevation System (tbc)
...


