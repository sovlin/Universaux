package org.universaux



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CharacterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Character.list(params), model:[characterCount: Character.count()]
    }

    def show(Character character) {
        respond character
    }

    def create() {
        respond new Character(params)
    }

    @Transactional
    def save(Character character) {
        if (character == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (character.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond character.errors, view:'create'
            return
        }

        character.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'character.label', default: 'Character'), character.id])
                redirect character
            }
            '*' { respond character, [status: CREATED] }
        }
    }

    def edit(Character character) {
        respond character
    }

    @Transactional
    def update(Character character) {
        if (character == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (character.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond character.errors, view:'edit'
            return
        }

        character.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'character.label', default: 'Character'), character.id])
                redirect character
            }
            '*'{ respond character, [status: OK] }
        }
    }

    @Transactional
    def delete(Character character) {

        if (character == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        character.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'character.label', default: 'Character'), character.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'character.label', default: 'Character'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}