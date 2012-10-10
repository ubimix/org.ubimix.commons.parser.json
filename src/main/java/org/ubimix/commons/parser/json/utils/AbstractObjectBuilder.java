/**
 * 
 */
package org.ubimix.commons.parser.json.utils;

import java.util.Stack;

import org.ubimix.commons.parser.ParserListener;
import org.ubimix.commons.parser.json.IJsonListener;
import org.ubimix.commons.parser.json.IJsonParser;

/**
 * An abstract utility class used to transform a sequence of events sent to the
 * {@link IJsonParser} interface to an object with specific properties.
 * 
 * @author kotelnikov
 */
public abstract class AbstractObjectBuilder extends ParserListener
    implements
    IJsonListener {
    private Stack<Object> fStack = new Stack<Object>();

    private Object fTop;

    private Object fValue;

    public AbstractObjectBuilder() {
    }

    protected abstract void addObjectValue(
        Object obj,
        String property,
        Object value);

    protected abstract void addToArray(Object array, Object value);

    @Override
    public void beginArray() {
        Object array = newArray();
        if (fTop == null) {
            fTop = array;
        }
        fStack.push(array);
    }

    @Override
    public void beginArrayElement() {
        fValue = null;
    }

    @Override
    public void beginObject() {
        Object object = newObject();
        if (fTop == null) {
            fTop = object;
        }
        fStack.push(object);
    }

    @Override
    public void beginObjectProperty(String property) {
    }

    @Override
    public void endArray() {
        fValue = fStack.pop();
    }

    @Override
    public void endArrayElement() {
        Object array = fStack.peek();
        addToArray(array, fValue);
        fValue = null;
    }

    @Override
    public void endObject() {
        fValue = fStack.pop();
    }

    @Override
    public void endObjectProperty(String property) {
        Object obj = fStack.peek();
        addObjectValue(obj, property, fValue);
        fValue = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractObjectBuilder)) {
            return false;
        }
        AbstractObjectBuilder o = (AbstractObjectBuilder) obj;
        return (fTop != null && o.fTop != null
            ? fTop.equals(o.fTop)
            : fTop == o.fTop);
    }

    public Object getTop() {
        return fTop;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    protected abstract Object newArray();

    protected abstract Object newObject();

    @Override
    public void onValue(boolean value) {
        fValue = value;
    }

    @Override
    public void onValue(double value) {
        fValue = value;
    }

    @Override
    public void onValue(int value) {
        fValue = value;
    }

    @Override
    public void onValue(long value) {
        fValue = value;
    }

    @Override
    public void onValue(String value) {
        fValue = value;
    }

    public void reset() {
        fStack.clear();
        fTop = null;
        fValue = null;
    }

    @Override
    public String toString() {
        return toString(fTop);
    }

    protected abstract String toString(Object top);

}
