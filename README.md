# java12-stream-reduce

* https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/stream/package-summary.html
* https://www.youtube.com/watch?v=IwJ-SCfXoAU
* reduce takes identity value not initial value (41.40)
* 54.12 - size of common pool, 55.49 - main is actually a part of it
* `-Djava.util.concurrent.ForkJoinPool.common.parallelism=100` // try to avoid it
* https://stackoverflow.com/questions/24308146/why-is-a-combiner-needed-for-reduce-method-that-converts-type-in-java-8

*     T result = identity;
*     for (T element : this stream)
*         result = accumulator.apply(result, element)
*     return result;
* The identity value must be an identity for the accumulator
* function. This means that for all t,
* accumulator.apply(identity, t) is equal to t.
* The accumulator function must be an
* associative function.
* T reduce(T identity, BinaryOperator<T> accumulator);


*     boolean foundAny = false;
*     T result = null;
*     for (T element : this stream) {
*         if (!foundAny) {
*             foundAny = true;
*             result = element;
*         }
*         else
*             result = accumulator.apply(result, element);
*     }
*     return foundAny ? Optional.of(result) : Optional.empty();
* @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
*                    <a href="package-summary.html#NonInterference">non-interfering</a>,
*                    <a href="package-summary.html#Statelessness">stateless</a>
*                    function for combining two values
* Optional<T> reduce(BinaryOperator<T> accumulator);

* Additionally, the combiner function
* must be compatible with the accumulator function; for all
* u and t, the following must hold: 
* combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)

* Many reductions using this form can be represented more simply
* by an explicit combination of {@code map} and {@code reduce} operations.

* @param combiner an <a href="package-summary.html#Associativity">associative</a>,
*                    <a href="package-summary.html#NonInterference">non-interfering</a>,
*                    <a href="package-summary.html#Statelessness">stateless</a>
*                    function for combining two values, which must be
*                    compatible with the accumulator function
* `<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);`

```
list.stream().reduce(identity,
                     accumulator,
                     combiner);
```
Produces the same results as:

```
list.stream().map(i -> accumulator(identity, i))
             .reduce(identity,
                     combiner);
```