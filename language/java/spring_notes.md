# Spring framework notes

## Spring Configuration
*

## SpringApplication
* Create an appropriate ApplicationContext instance
* Register a CommandLinePropertySource to expose command line arguments
* Refresh the application context, loading all singleton beans
* Trigger any CommandLineRunner beans
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
