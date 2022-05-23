# Spring framework notes

## IOC: BeanFactory + ApplicationContext (container)
* @Bean / @Autowired
    * Bean: singleton


## SpringApplication

### Spring Configuration
* configuration value class: decorated with @ConfigurationProperties
* configuration file: resources/application.yml
* configuration: @Component,@Controller,@service @Repository and @Bean

### To Start:
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

#### Bean construction
* annotation in spring framework: "@Component"
```
getSubscriptionConfig()
...
getExistingIfPossible()
...
bindObject()
bind()
...
postProcessBeforeInitialization()
applyBeanPostProcessorsBeforeInitialization()
initializeBean()
doCreateBean()
createBean()
...
doGetBean()
getBean()
preInstantiateSingletons() // all non-lazy singleton beans
finishBeanFactoryInitialization()
...
refreshContext()
run()
main()
```

### event sequence
* Constructor
* PostContruct method
* afterPropertiesSet method
* Bean init Method
* ApplicationStartedEvent
* ApplicationRunner Or CommandLineRunner depends on Order
* ApplicationReadyEvent

### Run call stack
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

### Spring Integration
* Channel Adapters for Google Cloud Pub/Sub
* https://docs.spring.io/spring-cloud-gcp/docs/1.0.0.RELEASE/reference/html/_spring_integration.html

* Event dispatch call stack
```
invokeForRequest()
...
doDispatch()
doService()
processRequest()
doPost()
...
doFilter()
invoke()
service()
process()
doRun()
runWorker()
run()
```

* axon server consume messages
```
consume()
onNext()
onMessage()
messagesAvailable()
runInContext()
runWorker()
run()
```

## other

### MC
* @RestController:
    * e.g., `@GetMapping("/greeting")`

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
* Command => Domain => Events
* Command: One handler per command
* Event: changes have happened / after change
    * domain / public event: shared to external
    * event sourcing event: internal to the domain
* Event Sourcing: event log
* Aggregate: collection of entities with root (id), handle commands
    * decoration: @Aggregate, @CommandHandler
    * command => event
* Projection
    * save: Repository.save(...), map(...Repository::save)
* Query
    * read => entity
* Sagas
* ref: https://sderosiaux.medium.com/cqrs-what-why-how-945543482313
