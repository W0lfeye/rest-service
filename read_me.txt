This is a Spring Boot project, which uses H2 for its in-memory database, to allow handling of HTTP requests as defined in the coding assignment.
To run it, you will need java 1.8 and the latest version of maven.

The project has a standard java structure with an src/main folder, which contains the classfiles and an src/test folder, where the unit and integration
tests are located. The main package is chargingsessionstore, which contains the spring boot application class and all other classes are in sub-packages. This allows
spring to scan for the other components. The entity is in chargingsessionstore.entity, the rest controller is in chargingsessionstore.controller
and you can guess where the repository is. The REST endpoints are defined in the controller and spring automatically creates database definitions based on the entity
and repository. By default all data and tables are dropped once the application is closed, but this behaviour can be changed within a property file. This wasn't part of the
required functionality, so I have used the default configuration.

The only place, where we need to worry about thread safety here is the controller. The same instance will be accessed by all requests. However the only shared resource
between the threads will be the repository. I couldn't find any documentation, where it's written in stone, that the default repository defined with Spring Boot is 100% thread safe,
but as far as I can tell, it is.

Regading the time complexity of the business logic:
All endpoints except for the summary are simple CRUD operations, so there's no slow-down from the business logic regardless of the amount of charging sessions.For the
/chargingSummary endpoint I created two methods in the repository, which will count the sessions started and suspended in a time period. With the numbers from those
methods I create the response object. I don't think this can be made any more efficient.

To run the application, navigate to the folder containing the pom file with cmd or terminal, depending on your operating system, and run "mvn clean package". This will download
all necessary dependancies, run the tests and create a target folder with a jar file, which can be run with the following command "java -jar target\rest-service-0.0.1-SNAPSHOT.jar".
I developed this on windows, so I am unsure, how exactly those commands will look on linux (at least the path to the jarfile will be with a forward slash).