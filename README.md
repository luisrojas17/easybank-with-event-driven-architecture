# Easy Bank Application with Event-Driven Architecture

This application exposes an API with basic bank's functionality over Event-Driven Architecture. 

Also, all of these microservices used H2 Database which creating the database in user directory since the database is based on file. On the other hand, to explore the content's database you could access by next URL:

    http://{HOST}:{PORT}/h2-console/
    
Where HOST represents the host where each Microservice is running and PORT represents the port where each Microservice is listening.

To start the application from command line you can use next commands:

Using Java jar command:

    ** java -jar target/easybank-section-X-1.0.0.jar
    
Where X means number module that you can run. For example:

    ** java -jar target/easybank-section-4-1.0.0.jar

Using Maven:

    ** mvn spring-boot:run

However, it is necessary to add next plugin to your pom.xml file:

```
...
<build>
    <plugins>
    ...
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    ..
    </plugins>
</build>
...
```
