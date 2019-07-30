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
}
