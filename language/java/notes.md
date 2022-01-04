# java notes
* Thinking in java
* Swing

## Inversion of control (IOC)
* ...

## "== vs equals"
* "==" checks type and never throws NullPointerException
* "equals" checks the underline object

## Lamda functions
* examples
```
// implicit argument: the the invoking object
Function<Integer, String> intToString = Object::toString; // intToString(5) = Object::toString(5)
Function<String, String> quote = s -> "'" + s + "'";

// chain together
Function<Integer, String> quoteIntToString = quote.compose(intToString);
assertEquals("'5'", quoteIntToString.apply(5));

// * supplier: no argument, returns sth
Supplier<Double> lazyValue = () -> {
    Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
    return 9d;
};

// * consumer: takes augument, returns nothiing
List<String> names = Arrays.asList("John", "Freddy", "Samuel");
names.forEach(name -> System.out.println("Hello, " + name));

// * filter:
List<String> names = Arrays.asList("Angela", "Aaron", "Bob", "Claire", "David");

List<String> namesWithA = names.stream()
  .filter(name -> name.startsWith("A"))
  .collect(Collectors.toList());

// * operator:
List<String> names = Arrays.asList("bob", "josh", "megan");
names.replaceAll(name -> name.toUpperCase());
```

## Java Heap vs Stack
* sharing:
    * stack: thread private
    * heap: shared between threads
* recycle:
    * stack: LIFO
    * heap:

## Memory:
* Young => Tenured => Perm
* GC runs in Perm as well if space is full or nearly full
* Issues:
    * GC pause application: -Xlog:safepoint
    * memory leaks: static final object...
    * High allocation rates can cause performance issues
    * Young objects are promoted too quickly
    * Too few GC threads: -XX:ParallelGCThreads=4

## JavaBean: a standard for class defintion
* All properties are private (use getters/setters)
* A public no-argument constructor
* Implements Serializable.