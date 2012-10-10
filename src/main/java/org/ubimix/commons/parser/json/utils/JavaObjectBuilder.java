/**
 * 
 */
package org.ubimix.commons.parser.json.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ubimix.commons.parser.json.IJsonListener;

/**
 * This class is used to transform method calls for the {@link IJsonListener}
 * interface into an hierarchy of Java objects. {@link Map}s correspond to JSON
 * objects , {@link List}s are used to represent JSON arrays. Basic types like
 * {@link Integer}, {@link Long}, {@link Double}, {@link String} and
 * {@link Boolean} are used to represent JSON numbers, string and boolean
 * values. The generated structures could be transformed back to a sequence of
 * calls to a {@link IJsonListener} listener using the {@link JavaObjectVisitor}
 * instance.
 * 
 * @author kotelnikov
 * @see JavaObjectVisitor
 */
public class JavaObjectBuilder extends AbstractObjectBuilder {

    /**
     * 
     */
    public JavaObjectBuilder() {
    }

    @Override
    protected void addObjectValue(Object obj, String property, Object value) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) obj;
        map.put(property, value);
    }

    @Override
    protected void addToArray(Object array, Object value) {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) array;
        list.add(value);
    }

    @Override
    protected Object newArray() {
        return new ArrayList<Object>();
    }

    @Override
    protected Object newObject() {
        return new LinkedHashMap<String, Object>();
    }

    @Override
    protected String toString(Object top) {
        return top != null ? top.toString() : null;
    }

}
