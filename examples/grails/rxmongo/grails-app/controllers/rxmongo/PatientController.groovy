package rxmongo

import grails.rx.web.RxController

import static org.springframework.http.HttpStatus.NOT_FOUND
import static rx.RxReactiveStreams.toObservable

/**
 * Serves up patient data
 *
 * @author Rahul Somasunderam
 */
@SuppressWarnings(['DuplicateStringLiteral'])
class PatientController implements RxController {

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Patient.list(params)
    }

    def show(String id) {
        Patient.get(id).
                map { rx.respond it }
    }

    def account(ObjectRequest objectRequest) {
        def query = new PatientAccountQuery()
        Patient.get(objectRequest.id).
                flatMap { patient ->
                    toObservable(objectRequest.version ?
                            query.computeSnapshot(patient, objectRequest.version) :
                            objectRequest.date ?
                                    query.computeSnapshot(patient, objectRequest.date) :
                                    query.computeSnapshot(patient, Long.MAX_VALUE))
                }.
                map { patientAccount ->
                    rx.respond patientAccount
                }
    }

    def health(ObjectRequest objectRequest) {
        def query = new PatientHealthQuery()
        Patient.get(objectRequest.id).
                flatMap { patient ->
                    toObservable(objectRequest.version ?
                            query.computeSnapshot(patient, objectRequest.version) :
                            objectRequest.date ?
                                    query.computeSnapshot(patient, objectRequest.date) :
                                    query.computeSnapshot(patient, Long.MAX_VALUE))
                }.
                map { patientHealth ->
                    rx.respond patientHealth
                }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message',
                        args: [message(code: 'patient.label', default: 'Patient'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
