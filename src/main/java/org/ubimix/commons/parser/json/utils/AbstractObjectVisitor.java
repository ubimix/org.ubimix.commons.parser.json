/**
 * 
 */
package org.ubimix.commons.parser.json.utils;

import java.util.HashSet;
import java.util.Set;

import org.ubimix.commons.parser.json.IJsonListener;

/**
 * @author kotelnikov
 */
public abstract class AbstractObjectVisitor {

    /**
     * 
     */
    public AbstractObjectVisitor() {
    }

    /**
     * Transforms the given java object in a sequence of JSON listener calls.
     * 
     * @param value the java object to transform in JSON calls
     */
    public void visit(Object value, boolean sort, IJsonListener listener) {
        Set<Object> stack = new HashSet<Object>();
        visit(value, sort, listener, stack, false);
    }

    /**
     * Transforms the given java object in a JSON object.
     * 
     * @param value the java object to transform in a JSON instance
     * @return a newly created JSON object
     */
    protected abstract void visit(
        Object value,
        boolean sort,
        IJsonListener listener,
        Set<Object> stack,
        boolean acceptNull);

    /**
     * Transforms the given java object in a sequence of JSON listener calls.
     * 
     * @param value the java object to transform in JSON calls
     */
    public void visit(Object value, IJsonListener listener) {
        visit(value, false, listener);
    }
}
