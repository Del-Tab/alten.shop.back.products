install and configure :
-> maven
-> a jre at least version java 17
-> a postgres database

Create a dedicated user and database on your postgres server using the commands inside initialization.sql

If you use a different database, change variables in src/main/resources/application.properties

build the project:
-> mvn clean install

run the back-end application on port 8080 using this maven command:
-> mvn spring-boot:run

then you can access the swagger user interface there
-> http://127.0.0.1:8080/swagger-ui/index.html