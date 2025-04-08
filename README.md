# Easy Bank Application with Event-Driven Architecture

This application exposes an API with basic bank's functionality over Event-Driven Architecture. 

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
