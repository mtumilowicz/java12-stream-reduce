# java12-stream-reduce

* https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/stream/package-summary.html
* https://www.youtube.com/watch?v=IwJ-SCfXoAU
* https://stackoverflow.com/questions/24308146/why-is-a-combiner-needed-for-reduce-method-that-converts-type-in-java-8

## preface
### Associativity
* an operator or function op is associative if the following holds:
    * `(a op b) op c == a op (b op c)`
the importance of this to parallel evaluation can be seen if we expand this to four terms:
     * `a op b op c op d == (a op b) op (c op d)`
     * so we can evaluate `(a op b)` in parallel with `(c op d)`, and then invoke op on the results
* examples of associative operations include numeric addition, min, and max, and string concatenation.

### Non-interference
* for most data sources, preventing interference means ensuring that the data source is not modified at all during 
the execution of the stream pipeline
* the notable exception to this are streams whose sources are concurrent collections, which are specifically 
designed to handle concurrent modification

### statelessness
* a stateful lambda (or other object implementing the appropriate functional interface) is one whose result depends 
on any state which might change during the execution of the stream pipeline
* the best approach is to avoid stateful behavioral parameters to stream operations entirely; there is usually a way 
to restructure the stream pipeline to avoid statefulness

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
    * combiner function must be compatible with the accumulator function
        `combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)` for all `u` and `t`
    * many reductions using this form can be represented more simply by an explicit combination of `map` 
    and `reduce` operations
        ```
        list.stream().reduce(identity,
                             accumulator,
                             combiner);
        ```
        produces the same results as:
        ```
        list.stream().map(i -> accumulator(identity, i))
                     .reduce(identity,
                             combiner);
        ```