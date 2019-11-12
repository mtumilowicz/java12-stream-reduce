import spock.lang.Specification

import java.util.concurrent.ForkJoinPool

/**
 * Created by mtumilowicz on 2019-07-30.
 */
class ReduceTests extends Specification {

    def "parallel non identity value"() {
        given:
        def ints = 1..10

        expect:
        ints.stream().reduce(0, { a, b -> a + b }) == 55
        ints.stream().parallel().reduce(20, { a, b -> a + b }) != 75
    }

    def "combiner example"() {
        given:
        def letters = ['a', 'b', 'c']

        when:
        def initial = new StringBuilder()
        def accumulator = { stringBuilder, string -> stringBuilder + string }
        def combiner = { stringBuilder1, stringBuilder2 -> stringBuilder1 + stringBuilder2 }

        then:
        letters.stream().reduce(initial, accumulator, combiner).toString() == 'abc'
    }

    def "combiner equivalent example"() {
        given:
        def letters = ['a', 'b', 'c']

        expect:
        letters.stream()
                .map { new StringBuilder(it) }
                .reduce(new StringBuilder(), { a, b -> a + b })
                .toString() == 'abc'
    }
}
