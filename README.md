# AppointmentManagementSystem

## Description

An internal international appointment management system based on MySQL server [currently out of commision]. It provides functionality to add, update, delete customers and appointments associated with the customers. The application maintains business hours and ensures that appointments are scheduled whithin these hours and they don't overlap. The information is stored in a MySQL DB with maintained cache while the application is running. 

The application is developed using DAO (Data Access Object) pattern that provides separation of the low level data operations from high level business services. Additionally, MVC (Model View Controller) pattern was used as a base for the system.



## GUI

<b>Login window</b>. It is a login form for users of the application that provides two fields to input username and password. The app identifies the timezone the user is in and displays it in the bottom left hand corner of the screen.

Current login information: 
    Username test
    Password test

<img src="Images/LoginScreen.PNG">

<b>Main menu window</b>. Upon login, user is directed to the main menu. The app shows a reminder of any appointments in the next 15 minutes. It also provides shortcuts to the rest of the following screens:
<ul>
  <li>Customers - customer table</li>
  <li>Appointments - appointments table</li>
  <li>Add customer - add/update customer window</li>
  <li>Add appointment - add/update appointment window</li>
  <li>By Type and Month - generates reports of all appointments by type and month [currently unavailable]</li>
  <li>By Division/County - generates reports of all appointments by deivion or county [currently unavailable]</li>
</ul>

<img src="Images/MainMenu.PNG">

<b>Add/Update customer window</b>. A fill-out form for a customer with name, address, postal code, phone number, and drop-downs for division (county) and country. The customer ID is automatically generated based on the primary key in the Customer table in the DB. ```All Customers``` changes the screen to the ```Customers table```, ```Save``` validates the input and saves the new information to the DB updating the local cache, ```Cancel``` returns the user to the ```Main menu```.

<img src="Images/AddUpdateCustScreen.PNG">

<b>Add/Update appointment window</b>. A fill-out form for an appointment with title, type, location, description, and drop-downs for contact, start and end times, date calendar, and customer. The appointment ID is automatically generated based on the primary key in the Appointment table in the DB. ```All Appointments``` changes the screen to the ```Appointments table```, ```Save``` validates the input and saves the new information to the DB updating the local cache, ```Cancel``` returns the user to the ```Main menu```.

<img src="Images/AddUpdateAppScreen.PNG">

<b>Customers table</b>. Shows a table of all customers with the following information for each:
<ul>
  <li>Name of the customer</li>
  <li>Address</li>
  <li>Postal code</li>
  <li>Phone number</li>
  <li>Division ID/County the user is located in</li>
</ul>

The form also provides functionality to add a new customer, update or delete an existing one, or go back to the main menu.

<img src="Images/CustomerTable.PNG">

<b>Appointments table</b>. Shows a table of all appointments filtered by the Contact Name drop-down in the upper right-hand corner. An additional filter for the current week, month, or all appointments is also applied to the table. The table contains the following information for each appointment:
<ul>
  <li>Appointment ID [unique]</li>
  <li>Title of the appointment</li>
  <li>Appointment description</li>
  <li>Appointment location</li>
  <li>Appointment type</li>
  <li>Start time [local]</li>
  <li>End time [local]</li>
  <li>Customer who requested the appointment</li>
  <li>User who created the appointment or last updated it</li>
</ul>

The form also provides functionality to add a new appointment, update or delete an existing one, or go back to the main menu.

<img src="Images/AppointementTable.PNG">

## Requirements

<ul>
  <li>IntelliJ IDEA 2019.3</li>
  <li>Java 11.0.6 or higher</li>
  <li>JavaFX 11.0.2 or higher</li>
  <li>mysql-connector-java-8.0</li>
</ul>

## Authors
Iulia Bejsovec
