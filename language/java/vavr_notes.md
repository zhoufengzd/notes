# some misc library notes

## io.vavr (Javaslang)
* Functional / Logic wrapper.
* Option: guard against null value.
    * Option<String> name = Option.of(nameParam);
* Tuple:
    * Tuple2<String, Integer> java8 = Tuple.of("Java", 8);
* Try: capture exception
    * Try<Integer> result = Try.of(() -> 1 / 0);
    * result.getOrElseThrow(ArithmeticException::new);
* Lambda function based on functional interfaces:
    *

## org.mapstruct.Mapper
*