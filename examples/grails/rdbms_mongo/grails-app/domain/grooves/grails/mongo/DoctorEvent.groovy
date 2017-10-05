package grooves.grails.mongo

import com.github.rahulsom.grooves.api.events.BaseEvent
import com.github.rahulsom.grooves.api.events.DisjoinEvent
import com.github.rahulsom.grooves.api.events.JoinEvent
import com.github.rahulsom.grooves.api.events.RevertEvent
import com.github.rahulsom.grooves.groovy.transformations.Event
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import rx.Observable

import static rx.Observable.just

/**
 * Represents an Event on a Doctor
 *
 * @author Rahul Somasunderam
 */
@SuppressWarnings(['AbstractClassWithoutAbstractMethod', 'GrailsDomainReservedSqlKeywordName'])
@ToString
@EqualsAndHashCode(includes = ['aggregate', 'position'])
abstract class DoctorEvent implements BaseEvent<Long, Doctor, Long, DoctorEvent> {

    RevertEvent<Long, Doctor, Long, DoctorEvent> revertedBy
    Date timestamp
    Long position
    Doctor aggregate

    Observable<Doctor> getAggregateObservable() { just(aggregate) }

    static transients = ['revertedBy']

    static constraints = {
    }
}

@Event(Doctor)
@EqualsAndHashCode(includes = ['aggregate', 'position'])
class DoctorCreated extends DoctorEvent {
    String name

    @Override String toString() { "Doctor $name created" }
}

@Event(Doctor)
@EqualsAndHashCode(includes = ['aggregate', 'position'])
class DoctorGotPatient extends DoctorEvent
        implements JoinEvent<Long, Doctor, Long, DoctorEvent, Long, Patient> {
    Patient patient
    @Override Observable<Patient> getJoinAggregateObservable() { just patient }

    @Override String toString() { "Doctor $aggregate got $patient" }
}

@Event(Doctor)
@EqualsAndHashCode(includes = ['aggregate', 'position'])
class DoctorLostPatient extends DoctorEvent
        implements DisjoinEvent<Long, Doctor, Long, DoctorEvent, Long, Patient> {
    Patient patient
    @Override Observable<Patient> getJoinAggregateObservable() { just patient }

    @Override String toString() { "Doctor $aggregate lost $patient" }
}

@EqualsAndHashCode(includes = ['aggregate', 'position'])
class DoctorAddedToZipcode extends DoctorEvent
        implements JoinEvent<Long, Doctor, Long, DoctorEvent, Long, Zipcode> {
    Zipcode zipcode
    @Override Observable<Zipcode> getJoinAggregateObservable() { just(zipcode) }

    @Override String toString() { "Doctor $aggregate added to $zipcode" }

    static transients = ['joinAggregate']
}

@EqualsAndHashCode(includes = ['aggregate', 'position'])
class DoctorRemovedFromZipcode extends DoctorEvent
        implements DisjoinEvent<Long, Doctor, Long, DoctorEvent, Long, Zipcode> {
    Zipcode zipcode
    @Override Observable<Zipcode> getJoinAggregateObservable() { just(zipcode) }

    @Override String toString() { "Doctor $aggregate removed from $zipcode" }

    static transients = ['joinAggregate']
}
