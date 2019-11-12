# java12-stream-reduce

* https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/stream/package-summary.html
* https://www.youtube.com/watch?v=IwJ-SCfXoAU
* https://stackoverflow.com/questions/24308146/why-is-a-combiner-needed-for-reduce-method-that-converts-type-in-java-8

## preface
### Associativity
* an operator or function `op` is associative if the following holds:
    * `(a op b) op c == a op (b op c)`
* the importance of this to parallel evaluation can be seen if we expand this to four terms:
     * `a op b op c op d == (a op b) op (c op d)`
     * so we can evaluate `(a op b)` in parallel with `(c op d)`, and then invoke op on the results
* example: numeric addition, min, and max, and string concatenation

### Non-interference
* means ensuring that the data source is not modified at all during the execution of the stream pipeline
* the notable exception to this are streams whose sources are concurrent collections, which are specifically 
designed to handle concurrent modification

### statelessness
* a stateful lambda is one whose result depends on any state which might change during the execution of the 
stream pipeline
* the best approach is to avoid stateful behavioral parameters to stream operations entirely

## reduce
* accumulator have to be an **associative**, **non-interfering**, **stateless** function for combining two values
1. `Optional<T> reduce(BinaryOperator<T> accumulator);`
    * equivalent to
        ```
        boolean foundAny = false;
        T result = null;
        for (T element : this stream) {
            if (!foundAny) {
                foundAny = true;
                result = element;
            }
            else
                result = accumulator.apply(result, element);
        }
        return foundAny ? Optional.of(result) : Optional.empty();
        ```
1. `T reduce(T identity, BinaryOperator<T> accumulator);`
    * equivalent to
        ```
        T result = identity;
        for (T element : this stream)
            result = accumulator.apply(result, element)
        return result;
        ```
    * identity value must be an identity for the accumulator function. This means that for all t,
        `accumulator.apply(identity, t) is equal to t` for all `t`
1. `<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);`
    * note that with other two implementations of `reduce` there is no possibility to return other type
    than stream type (eg. if you are reducing stream of integers with other two implementations the reduced value
     is also an integer)
    * combiner function must be compatible with the accumulator function
        `combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)` for all `u` and `t`
    * many reductions using this form can be represented more simply by an explicit combination of `map` 
    and `reduce` operations
        ```
        list.stream().reduce(identity, accumulator, combiner);
        ```
        produces the same results as:
        ```
        list.stream().map(i -> accumulator(identity, i)) .reduce(identity, combiner);
        ```
## project description
1. non identity value in parallel streams
    ```
    given:
    def ints = 1..10

    expect:
    ints.stream().reduce(0, { a, b -> a + b }) == 55
    ints.stream().reduce(20, { a, b -> a + b }) == 75 // possibly true, but not guarantee by specification
    ints.stream().parallel().reduce(20, { a, b -> a + b }) != 75 // 20 is not an identity value
    ```
1. reduce with combiner
    ```
    given:
    def letters = ['a', 'b', 'c']

    when:
    def initial = new StringBuilder()
    def accumulator = { stringBuilder, string -> stringBuilder + string }
    def combiner = { stringBuilder1, stringBuilder2 -> stringBuilder1 + stringBuilder2 }

    then:
    letters.stream().reduce(initial, accumulator, combiner).toString() == 'abc'
    ```
1. note that above example with `combiner` could be easily represent as a `map` + ordinary `reduce`
    ```
    given:
    def letters = ['a', 'b', 'c']
    
    expect:
    letters.stream()
            .map { new StringBuilder(it) }
            .reduce(new StringBuilder(), { a, b -> a + b })
            .toString() == 'abc'
    ```