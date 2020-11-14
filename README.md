# IS2103 Enterprise Systems Server-Side Design and Development (Pair Project 2020)
This pair project is one of NUS School of Computing (Information Systems [IS] major) core IS modules.

## Business Rules
1. Every entity class has a unique key identifier.
2. Not all entities must perform CRUD (Create, Remove, Update & Delete) operations, only FlightSchedulePlan entity must.
3. The core logic lies in *Create Flight Schedule Plan* and *Search Flights*.

## Rationales
1. By having a unique key identifier, which is a primary key, data objects are retrieved easily and directly with its own ID.

## Assumption
1. We assume that there will be 30 aircrafts and tracking of aircraft based on its tail number is not required.
2. We assume that row numbers and seat letters are sequential with no skipping.
Such that it is not essential to skip letters that may be confused with numbers (e.g. I, O, Q, S or Z).
3. We assume that Merlion Airlines does not operates only in its home hub in Singapore Changi Airport and Taoyuan International Airport,
it can also operate in other airports like Narita and Incheon International Airport.
4. We must consider that different countries have different time zones. 

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
- [X] Search Flight 
- [ ] Reserve Flight

### Flight Schedule Plan Session Bean [Flight Operation Module]
- [X] Create Flight Schedule Plan
- [X] View All Flight Schedule Plans
- [X] View Flight Schedule Plan Details
- [X] Update Flight Schedule Plan
- [X] Delete Flight Schedule Plan

### Seats Inventory Session Bean [Sales Management Module]
- [X] View Seats Inventory

### Flight Reservations Session Bean [Sales Management Module]
- [X] View Flight Reservations
- [ ] View My Flight Reservations
- [ ] View My Flight Reservation Details

## FRS Reservation Client (for Customers)
### Customer Session Bean 
- [X] Register As Customer
- [X] Customer Login
- [ ] Customer Logout

![Alt Text](https://i.imgur.com/X4sptXo.png)

## Holiday Reservation System (tbc)
...


