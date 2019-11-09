import spock.lang.Specification

import java.util.concurrent.ForkJoinPool

/**
 * Created by mtumilowicz on 2019-07-30.
 */
class XTest extends Specification {
    def "test reduce"() {
        expect:
        X.reduce() == 45
    }

    def "test parallelReduce"() {
        expect:
        X.parallelReduce() == 45
    }

    def "test parallelNonIdentityReduce"() {
        expect:
        X.parallelNonIdentityReduce() != 55
    }

    def "xxx"() {
        def nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

        nums.stream().map({ X.transform(it) }).forEach({})
        nums.stream().parallel().map({ X.transform(it) }).forEach({}) // work stealing, main thread participates in

        println "DONE " + Thread.currentThread()

        println Runtime.getRuntime().availableProcessors()
        println ForkJoinPool.commonPool()

        expect:
        1 == 1
    }
}
