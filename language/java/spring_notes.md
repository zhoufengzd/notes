# Spring framework notes

## Spring Configuration
* configuration value class: decorated with @ConfigurationProperties
* configuration file: resources/application.yml

## SpringApplication
* Create an appropriate ApplicationContext instance
* Register a CommandLinePropertySource to expose command line arguments
* Refresh the application context, loading all singleton beans
* Trigger any CommandLineRunner beans

### Different ways to run method after startup
* Using CommandLineRunner interface
* With ApplicationRunner interface
    * with parsed ApplicationArguments
* Spring boot Application events
* @Postconstruct annotation on a method
* The InitializingBean Interface
* Init attribute of @bean annotation

### event sequence
* Constructor
* PostContruct method
* afterPropertiesSet method
* Bean init Method
* ApplicationStartedEvent
* ApplicationRunner Or CommandLineRunner depends on Order
* ApplicationReadyEvent

### call stack
```
public class SpringApplication {
}
```

* start:
```
public static void main(String[] args) {
    SpringApplication.run(<X>.class, args);
}
```

* call stack
```
CommandLineRunner::run(...)
callRunner(CommandLineRunner runner)
```

## other

### HikariCP
* light weight / fast jdbc connection pool

### Domain Driven Design (DDD)
* Class matches the business domain. Business first.
* Value Object: value matters, identity not.
* Entity: identifiable object
* Repository: read ojects from data store
* Factory: create domain objects
* Event sourcing: entity read / write to event store
* transactional boundaries

### CQRS
* :