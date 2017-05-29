package rxmongo

import com.github.rahulsom.grooves.api.EventApplyOutcome
import com.github.rahulsom.grooves.grails.RxGormQuerySupport
import com.github.rahulsom.grooves.groovy.transformations.Query
import grails.compiler.GrailsCompileStatic
import rx.Observable

import static com.github.rahulsom.grooves.api.EventApplyOutcome.CONTINUE
import static rx.Observable.just

/**
 * Performs a query that shows the health of a patient
 *
 * @author Rahul Somasunderam
 */
@Query(aggregate = Patient, snapshot = PatientHealth)
@GrailsCompileStatic
class PatientHealthQuery implements
        RxGormQuerySupport<String, Patient, String, PatientEvent, String, PatientHealth> {

    final Class<PatientHealth> snapshotClass = PatientHealth
    final Class<PatientEvent> eventClass = PatientEvent

    @Override
    PatientHealth createEmptySnapshot() { new PatientHealth(deprecates: [], procedures: []) }

    @Override
    boolean shouldEventsBeApplied(PatientHealth snapshot) {
        true
    }

    @Override
    void addToDeprecates(PatientHealth snapshot, Patient deprecatedAggregate) {
        snapshot.addToDeprecatesIds deprecatedAggregate.id
    }

    @Override
    Observable<EventApplyOutcome> onException(
            Exception e, PatientHealth snapshot, PatientEvent event) {
        // ignore exceptions. Look at the mongo equivalent to see one possible way to
        // handle exceptions
        just CONTINUE
    }

    Observable<EventApplyOutcome> applyPatientCreated(
            PatientCreated event, PatientHealth snapshot) {
        snapshot.name = snapshot.name ?: event.name
        just CONTINUE
    }

    Observable<EventApplyOutcome> applyProcedurePerformed(
            ProcedurePerformed event, PatientHealth snapshot) {
        snapshot.addToProcedures(code: event.code, date: event.timestamp)
        just CONTINUE
    }

    @SuppressWarnings(['UnusedMethodParameter'])
    Observable<EventApplyOutcome> applyPaymentMade(
            PaymentMade event, PatientHealth snapshot) {
        // Ignore payments
        just CONTINUE
    }

    @Override
    Observable<Patient> reattachAggregate(Patient aggregate) {
        Patient.get aggregate.id
    }

}
