# Get A Room

This project was developed within the course "Technologies Internet Applications" at the National and Kapodistrian University of Athens. "Get A Room", is a website on which everyone with their entrance has the possibility to browse and to search for housing adapted to his preferences. The website also offers the registered user the possibility to rent a rental from approved hosts, submit reviews, chat with the host of each rental as well as providing personalized proposals. The approved hosts on the other hand can import and modify the spaces for rent as well as to chat with the users. Finally the administrator approves the hosts and can produce database files.

The project consists of two parts:
- The front end (front-end), which was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.1.4.
- The back-end, which has been implemented with Spring-Boot

Regarding the security of the application, Spring Security has been used
as well as JWT technology and the SSL protocol

## Table of Contents
- [Installation](#Installation)
  - [Database](#Database)
  - [Backend](#Backend)
  - [Frontend](#Frontend)
- [Instructions](#Instructions)
  - [Simple_User](#Simple_User)
  - [Host](#Host)
  - [Admin](#Admin)
- [Screenshot](#Screenshot)
  - [User_Home_Page](#User Home Page)
  - [Rental_Result_Page](#Rental Result Page)
  - [Rental_Page](#Rental Page)
  - [Host_Page](#Host Page)
  - [Chat](#Chat)
  - [Admin Page](#Admin Page)

## Installation

### Database
It is required to import the database file from My-Sql-Workbench.

### Backend
1) Download the file and open it in intellij
2) hanging the personal information in the "application properties" regarding the url
(base name), username, password
3) Run the program via Run "ΝewAirbnbApplication" on the top right.

### Frontend
1) Angular 16 is required to run in VS-Code
2) Download the file and open it in VS Code.
3) Run the command `ng serve` to run the program.
4) Navigate to `http://localhost:4200/

## Instructions
The new usew can register in Get A Room as:
- simple user or
- host

However, in Get A Room you can log in as
- simple user 
- host and
- admin

### Simple_User
For the "simple user" Inerface try loging in as following:
- username: john
- password: 123456

When logged as "simple user" in the search area try the following fields:
- Destination: Athens
- Check-in: 01/10/2023
- Check-out: 02/10/2023
- Number of Guests: 2

### Host
For the "host" Inerface try loging in as following:
- username: anna
- password: 123456

### Admin
For the "admin" Inerface try loging in as following:
- username: admin
- password: 123456

## Screenshot

### User_Home_Page
![User Home Page](https://github.com/LydiaKyriakou/myAirbnbProject/blob/master/Screenshot/user_home.png)

### Rental_Result_Page
![Rental Result Page](https://github.com/LydiaKyriakou/myAirbnbProject/blob/master/Screenshot/result_page.png)

### Rental_Page
![Rental Page](https://github.com/LydiaKyriakou/myAirbnbProject/blob/master/Screenshot/rental_page.png)

### Host_Page
![Host Page](https://github.com/LydiaKyriakou/myAirbnbProject/blob/master/Screenshot/host_page.png)

### Chat
![Chat](https://github.com/LydiaKyriakou/myAirbnbProject/blob/master/Screenshot/chat.png)

### Admin_Page
![Admin Page](https://github.com/LydiaKyriakou/myAirbnbProject/blob/master/Screenshot/admin_page.png)


