# Easy Bank Section 1

All of these Microservices were developed by Spring Boot Framework and implementing next patters:

1. Database per service
2. API Composition
3. SAGA 


# Dependencies

All of these Microservices are based on:

1. Rabbit MQ.
   https://www.rabbitmq.com/
2. H2 Database
   https://www.h2database.com/html/main.html


# RabbitMQ

## Setup and Run RabbitMQ by Docker Container

Go to next URL:

    https://www.rabbitmq.com/docs/download

```
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```

## Verify the Setup

Go to browser and typing next URL in address bar:

    http://localhost:15672

and provide next credentials: guest/guest
    

# H2 Database
Also, all of these microservices used H2 Database which creating the database in user directory since the database
is based on file. On the other hand, to explore the content's database you could access by next URL:

    http://{HOST}:{PORT}/h2-console/

For example:

1. Accounts
    http://localhost:8081/h2-console/
2. Cards
    http://localhost:8082/h2-console/
3. Loans
    http://localhost:8083/h2-console/
4. Customers
    http://localhost:8080/h2-console/
5. Gateway
    http://localhost:8072/h2-console/
6. Eureka
    http://localhost:8070/h2-console/ 


## Order to Start each Microservice
You have to start each microservice according to next order:

1. Eureka Server
2. Customer
3. Accounts
4. Cards
5. Loans
6. Gateway Server
