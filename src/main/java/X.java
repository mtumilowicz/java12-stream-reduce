import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by mtumilowicz on 2019-07-30.
 */
public class X {
    
    static Stream<Integer> ints() {
        return Stream.iterate(0, i -> i < 10, i -> ++i);
    }
    
    static int reduce() {
        return ints().reduce(0, Integer::sum);
    }

    static int parallelReduce() {
        return ints().reduce(0, Integer::sum);
    }

    static int parallelNonIdentityReduce() {
        return ints().parallel().reduce(20, Integer::sum);
    }

    static int transform(int number) {
        System.out.println("transforming " + number + " " + Thread.currentThread());

        return number;
    }

    // check on parallel stream where are executed
    static void process(Stream<Integer> stream) throws InterruptedException {
        ForkJoinPool fjp = new ForkJoinPool(50);

        fjp.submit(() -> stream.forEach(e -> {}));

        fjp.shutdown();

        fjp.awaitTermination(30, TimeUnit.SECONDS);
    }
}
