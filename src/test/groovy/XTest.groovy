import spock.lang.Specification

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
}
