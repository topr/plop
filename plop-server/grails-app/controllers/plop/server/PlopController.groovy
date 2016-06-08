package plop.server

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PlopController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Plop.list(params), model: [plopCount: Plop.count()]
    }

    def show(Plop plop) {
        respond plop
    }

    @Transactional
    def save(Plop plop) {
        if (plop == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (plop.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plop.errors, view: 'create'
            return
        }

        plop.save flush: true

        respond plop, [status: CREATED, view: "show"]
    }

    @Transactional
    def update(Plop plop) {
        if (plop == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (plop.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond plop.errors, view: 'edit'
            return
        }

        plop.save flush: true

        respond plop, [status: OK, view: 'show']
    }

    @Transactional
    def delete(Plop plop) {

        if (plop == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        plop.delete flush: true

        render status: NO_CONTENT
    }
}
