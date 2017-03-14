package com.github.rahulsom.grooves.api

import com.github.rahulsom.grooves.api.internal.BaseEvent

/**
 * Marks a class as a snapshot. This supports both temporal and versioned access.
 * @param <A> The Aggregate this snapshot works over
 *
 * @author Rahul Somasunderam
 */
trait Snapshot<A extends AggregateType, ID> implements VersionedSnapshot<A, ID>, TemporalSnapshot<A, ID> {

    @Override
    void setLastEvent(BaseEvent<A, ID> event) {
        this.lastEventTimestamp = event.timestamp
        this.lastEventPosition = event.position
    }
}
