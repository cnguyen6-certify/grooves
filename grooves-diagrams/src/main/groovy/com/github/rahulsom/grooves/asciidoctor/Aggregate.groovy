package com.github.rahulsom.grooves.asciidoctor

import groovy.transform.TupleConstructor

import static java.lang.System.identityHashCode

/**
 * Represents an aggregate when rendering as an image
 *
 * @author Rahul Somasunderam
 */
@TupleConstructor
class Aggregate {
    String type
    String id
    String description
    List<Event> events = []

    int index

    @Override String toString() { "|$type,$id,$description\n${events.join('\n')}" }

    void buildSvg(builder, Map<Date, Double> dates) {
        builder.mkp.comment "   aggregate"
        builder.g(id: "aggregate${identityHashCode(this)}", class: 'aggregate') {
            def y = index * Constants.eventLineHeight + Constants.offset

            builder.rect x: 10, y: y, width: Constants.aggregateWidth, height: Constants.aggregateHeight
            builder.text type.toString(), x: 15, y: y + Constants.textLineHeight, class: 'type'
            builder.text id, x: 15, y: y + Constants.textLineHeight * 2, class: 'id'
            builder.text description, x: 10, y: y - 5, class: 'description'

            def yMid = index * Constants.eventLineHeight + Constants.offset + Constants.aggregateHeight / 2

            builder.line x1: 10 + Constants.aggregateWidth, y1: yMid,
                    x2: dates.values().max() * Constants.eventSpace + 3 * Constants.aggregateWidth, y2: yMid,
                    class: 'eventLine', 'marker-end': "url(#triangle)"

        }
    }
}
