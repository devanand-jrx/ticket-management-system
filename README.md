# Ticket Management System

A web application to manage support tickets effectively. This application allows support agents to track and resolve customer issues.

## Features

### Admin
- Create and manage agents.
- Perform CRUD operations on agents.

### Agent
- Perform CRUD operations on tickets and customers.
- Assign a ticket to an agent.
- Update the status of a ticket (Open, In Progress, Resolved, Closed).
- Search for tickets by issue description, customer name, or ticket ID.

### Customer
- Perform CRUD operations on tickets created by the customer.
## Technologies Used
- **Java** (JDK 17)
- **Spring Boot**
- **Maven**
- **PostgreSQL**

## Getting Started

### Prerequisites
- Java 17
- Maven
- PostgreSQL

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/devanand-jrx/ticket-management-system.git

2. **Configure the database:**
 
   Create a PostgreSQL database and update the application.yml file with your database configurations.

3. **Build the project:**

     ```bash
       mvn clean install
   
4. **Run the application:**
    ```bash
      mvn spring-boot:run

Note : The application will be accessible at ```http://localhost:8080```
Endpoints are added in postman collection (available in code).

## Admin Endpoints
- **Create Agent**: `POST /admin/agents`
- **Get All Agents**: `GET /admin/agents`
- **Get Agent By ID**: `GET /admin/agents/{id}`
- **Delete Agent**: `DELETE /admin/agents/{id}`
- **Update Agent**: `PUT /admin/agents/{id}`

## Agent Endpoints
- **Create Customer**: `POST /agents/customers`
- **Get All Customers**: `GET /agents/customers`
- **Get Customer By ID**: `GET /agents/customers/{id}`
- **Update Customer**: `PUT /agents/customers/{id}`
- **Delete Customer**: `DELETE /agents/customers/{id}`
- **Get All Tickets**: `GET /agents/ticket`
- **Get Ticket By ID**: `GET /agents/ticket/{id}`
- **Edit Ticket**: `PUT /agents/ticket/{id}`
- **Delete Ticket**: `DELETE /ticket/{id}`
- **Assign Ticket to Agent**: `PUT /agents/ticket/{ticketId}/assign/{agentId}`
- **Search Tickets by Customer**: `POST /agents/ticket/search/customer?customer={customerName}`
- **Search Tickets by Description**: `POST /agents/ticket/search/description?description={description}`

## Customer Endpoints
- **Create Ticket**: `POST /ticket/{customerId}`
- **Get All Tickets**: `GET /ticket`
- **Get Ticket By ID**: `GET /ticket/{id}`
- **Edit Ticket**: `PUT /ticket/{id}`
- **Delete Ticket**: `DELETE /ticket/{id}`

## Database//Tables

- Agent
  ![Agent](./images/Agent.png)
- Customer
  ![Customer](./images/Customer.png)
- Ticket
  ![Ticket](./images/Ticket.png)


## Code Coverage:

![TMS](./images/TMS.png)


#### Done by Devanand Ravindran
```contact : +91 8281529720 ```

``` email : devanandravindran10@gmail.com```
