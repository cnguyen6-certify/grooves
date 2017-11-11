package com.github.rahulsom.grooves.queries.internal;

import com.github.rahulsom.grooves.api.AggregateType;
import com.github.rahulsom.grooves.api.EventApplyOutcome;
import com.github.rahulsom.grooves.api.events.BaseEvent;
import com.github.rahulsom.grooves.api.snapshots.internal.BaseSnapshot;
import io.reactivex.Flowable;

import static io.reactivex.Flowable.fromPublisher;

public class SimpleExecutor<
        AggregateIdT,
        AggregateT extends AggregateType<AggregateIdT>,
        EventIdT,
        EventT extends BaseEvent<AggregateIdT, AggregateT, EventIdT, EventT>,
        ApplicableEventT extends EventT,
        SnapshotIdT,
        SnapshotT extends
                BaseSnapshot<AggregateIdT, AggregateT, SnapshotIdT, EventIdT, EventT>,
        QueryT extends
                SimpleQuery<AggregateIdT, AggregateT, EventIdT, EventT, ApplicableEventT,
                        SnapshotIdT, SnapshotT, QueryT>
        > extends
        QueryExecutor<AggregateIdT, AggregateT, EventIdT, EventT, SnapshotIdT, SnapshotT, QueryT> {

    @Override
    protected Flowable<EventApplyOutcome> callMethod(
            QueryT query, String methodName, SnapshotT snapshot, EventT event) {
        return fromPublisher(query.applyEvent((ApplicableEventT) event, snapshot));
    }
}