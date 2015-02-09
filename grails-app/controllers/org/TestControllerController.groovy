package org



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TestControllerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TestController.list(params), model:[testControllerCount: TestController.count()]
    }

    def show(TestController testController) {
        respond testController
    }

    def create() {
        respond new TestController(params)
    }

    @Transactional
    def save(TestController testController) {
        if (testController == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testController.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testController.errors, view:'create'
            return
        }

        testController.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testController.label', default: 'TestController'), testController.id])
                redirect testController
            }
            '*' { respond testController, [status: CREATED] }
        }
    }

    def edit(TestController testController) {
        respond testController
    }

    @Transactional
    def update(TestController testController) {
        if (testController == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testController.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testController.errors, view:'edit'
            return
        }

        testController.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'testController.label', default: 'TestController'), testController.id])
                redirect testController
            }
            '*'{ respond testController, [status: OK] }
        }
    }

    @Transactional
    def delete(TestController testController) {

        if (testController == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        testController.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'testController.label', default: 'TestController'), testController.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testController.label', default: 'TestController'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}