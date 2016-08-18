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
            saveAll(plops(amount))

        when:
            controller.list()

        then:
            response.contentType.contains(JSON_CONTENT_TYPE)
            response.json.size() == amount
    }

    void 'Count plops'() {

        given:
            saveAll(plops(11))

        when:
            controller.count()

        then:
            response.contentType.contains(JSON_CONTENT_TYPE)
            response.json == [count: 11]
    }

    void 'Stores a valid plop'() {

        given:
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'

        when: 'The save action is executed with a valid instance'
            controller.save(plop())

        then: 'Added plop is in store'
            Plop.count() == 1

        and: 'A redirect is issued to the show action'
            response.status == CREATED.value()
            response.json
    }

    void 'Fails on invalid plop storing attempt'() {

        when: 'The save action is executed with an invalid instance'
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            def plop = new Plop()
            plop.validate()
            controller.save(plop)

        then: 'The create view is rendered again with the correct model'
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors
    }

    void 'Test the update action performs an update on a valid domain instance'() {
        when: "Update is called for a domain instance that doesn't exist"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: 'A 404 error is returned'
            response.status == NOT_FOUND.value()

        when: 'An invalid domain instance is passed to the update action'
            response.reset()
            def plop = new Plop()
            plop.validate()
            controller.update(plop)

        then: 'The edit view is rendered again with the invalid instance'
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors

        when: 'A valid domain instance is passed to the update action'
            response.reset()
            validParams(params)
            plop = new Plop(params).save(flush: true)
            controller.update(plop)

        then: 'A redirect is issued to the show action'
            plop != null
            response.status == OK.value()
            response.json.id == plop.id
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

    private Iterable<Plop> plops(int howMany) {
        return {
            int count = howMany
            [
                    next   : { plop("plop ${howMany - --count}") },
                    hasNext: { count > 0 }
            ] as Iterator<Plop>
        }
    }

    private plop(String text = 'plop!') {
        new Plop(text: text)
    }

    private static saveAll(Iterable<Plop> items) {
        return items.collect {
            it.save(
                    flush: true,
                    failOnError: true
            )
        }
    }
}