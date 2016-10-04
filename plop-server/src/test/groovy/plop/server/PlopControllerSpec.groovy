package plop.server

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

@Mock(Plop)
@TestFor(PlopController)
class PlopControllerSpec extends Specification {

    def 'Responds with empty list when no plops'() {
        when: controller.list()
        then: response.text == '[]'
    }

    void 'List all plops'() {

        given:
            int amount = 11
            saveAll(validPlops(amount))

        when:
            controller.list()

        then:
            response.contentType.contains(JSON_CONTENT_TYPE)
            response.json.size() == amount
    }

    void 'Count plops'() {

        given:
            saveAll(validPlops(6))

        when:
            controller.count()

        then:
            response.contentType.contains(JSON_CONTENT_TYPE)
            response.json == [count: 6]
    }

    void 'Stores a new valid plop'() {

        when:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            controller.save(validPlop())

        then: 'Added plop is in store'
            Plop.count() == 1

        and: 'A redirect is issued to the show action'
            response.status == CREATED.value()
            response.json
    }

    void 'Fails with 404 when receives no plop to store'() {

        when:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            controller.save(null)

        then: 'The create view is rendered again with the correct model'
            response.status == NOT_FOUND.value()
    }

    void 'Fails upon a new invalid plop storing attempt'() {

        when:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            controller.save(invalidPlop())

        then: 'The create view is rendered again with the correct model'
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors
    }

    void 'Fails with 404 on update for non-existing plop'() {

        expect:
            Plop.count == 0

        when:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(plop)

        then:
            response.status == NOT_FOUND.value()

        where:
            plop << [null, validPlop(), invalidPlop()]
    }

    def 'Fails upon invalid plop update attempt'() {

        given:
            def plop = invalid(existingPlop())

        when:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(plop)

        then: 'The edit view is rendered again with the invalid instance'
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors
    }

    void 'Updates a valid existing plop'() {

        given:
            def plop = existingPlop()
            plop.text = 'updated text'

        when:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(plop)

        then:
            response.status == OK.value()
            response.json.id == plop.id

        and:
            Plop.get(plop.id).text == 'updated text'
    }

    void 'Test that the delete action deletes an instance if it exists'() {
        when: 'The delete action is called for a null instance'
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: 'A 404 is returned'
            response.status == NOT_FOUND.value()


        when: 'A domain instance is created'
            response.reset()
            validParams(params)
            def plop = new Plop(params).save(flush: true)

        then: 'It exists'
            Plop.count() == 1

        when: 'The domain instance is passed to the delete action'
            controller.delete(plop)

        then: 'The instance is deleted'
            Plop.count() == 0
            response.status == NO_CONTENT.value()
    }

    private Map validParams(Map paramz = [:]) {
        paramz.text = 'Plop!'

        return paramz
    }

    private static Iterable<Plop> validPlops(int howMany) {
        return {
            int count = howMany
            [
                    next   : { validPlop("plop ${howMany - --count}") },
                    hasNext: { count > 0 }
            ] as Iterator<Plop>
        }
    }

    private static existingPlop() {
        save(validPlop())
    }

    private static validPlop(String text = 'plop!') {
        def plop = new Plop(text: text)
        assert plop.validate()

        return plop
    }

    private static invalidPlop() {
        invalid(new Plop())
    }

    private static Plop invalid(Plop plop) {
        plop.text = null
        assert !plop.validate()

        return plop
    }

    private static List<Plop> saveAll(Iterable<Plop> items) {
        items.collect { save(it) }
    }

    private static Plop save(Plop plop) {
        plop.save(
                flush: true,
                failOnError: true
        )
    }
}