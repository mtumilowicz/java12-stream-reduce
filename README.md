# java12-stream-reduce

https://www.youtube.com/watch?v=IwJ-SCfXoAU
* reduce takes identity value not initial value (41.40)
* 54.12 - size of common pool, 55.49 - main is actually a part of it
* -Djava.util.concurrent.ForkJoinPool.common.parallelism=100
* own pool: 1.05.28

* The identity value must be an identity for the accumulator
* function. This means that for all t,
* accumulator.apply(identity, t) is equal to t.
* The accumulator function must be an
* associative function.
* T reduce(T identity, BinaryOperator<T> accumulator);


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