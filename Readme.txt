install and configure :
-> maven
-> a jre at least version java 17
-> a postgres database

Create a dedicated user and database on your postgres server using the commands inside initialization.sql
If you use a different database, change variables in src/main/resources/application.properties

Once the database is created, connect to the database alten_test as  user test, and then run

create table product (
  id bigserial not null,
  price bigint not null,
  quantity bigint not null,
  rating bigint,
  category varchar(255) check (category in ('Accessories','Clothing','Electronics','Fitness')),
  code varchar(255) not null,
  description varchar(255) not null,
  image varchar(255),
  inventory_status varchar(255) check (inventory_status in ('INSTOCK','LOWSTOCK','OUTOFSTOCK')),
  name varchar(255) not null,
  primary key (id)
);

Alternatively you can un-comment the following line in the application.properties file,
 to reset the database alten_test to what the application needs on startup (you'll loose all data and tables on this database)
#spring.jpa.properties.jakarta.persistence.schema-generation.database.action=drop-and-create

build the project:
-> mvn clean install

run the back-end application on port 8080 using this maven command:
-> mvn spring-boot:run

then you can access the swagger user interface there
-> http://127.0.0.1:8080/swagger-ui/index.html