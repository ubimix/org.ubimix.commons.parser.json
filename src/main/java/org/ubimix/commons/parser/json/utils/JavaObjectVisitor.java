/**
 * 
 */
package org.ubimix.commons.parser.json.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ubimix.commons.parser.json.IJsonListener;

/**
 * This utility class is used to transform Java structures containing
 * {@link Map}, {@link List} and basic objects ({@link Integer}, {@link Long},
 * {@link Float}, {@link Double}, {@link Boolean}, {@link String}) into a
 * sequence calls to a listener implementing the {@link IJsonListener}
 * interface. The {@link JavaObjectBuilder} could be used to build such a
 * structure from a sequence of event calls.
 * 
 * @author kotelnikov
 * @see JavaObjectBuilder
 */
public class JavaObjectVisitor extends AbstractObjectVisitor {

    private static Comparator<Object> KEY_COMPARATOR = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            String str1 = keyToString(o1);
            String str2 = keyToString(o2);
            return str1.compareTo(str2);
        }
    };

    private static String keyToString(Object o1) {
        return o1 != null ? o1.toString() : "";
    }

    protected Iterable<?> getList(Object value) {
        if (value instanceof Iterable<?>) {
            return (Iterable<?>) value;
        }
        return null;
    }

    protected Map<?, ?> getMap(Object value) {
        if (value instanceof Map<?, ?>) {
            return (Map<?, ?>) value;
        }
        return null;
    }

    /**
     * Transforms the given java object in a JSON object.
     * 
     * @param value the java object to transform in a JSON instance
     * @return a newly created JSON object
     */
    @Override
    protected void visit(
        Object value,
        boolean sort,
        IJsonListener listener,
        final Set<Object> stack,
        boolean acceptNull) {
        if (value == null && !acceptNull) {
            return;
        }
        if (stack.contains(value)) {
            return;
        }
        stack.add(value);
        try {
            boolean result = false;
            if (value == null) {
                listener.onValue(null);
                result = true;
            }
            if (!result) {
                Map<?, ?> map = getMap(value);
                if (map != null) {
                    listener.beginObject();
                    if (sort) {
                        List<Object> keys = new ArrayList<Object>(map.keySet());
                        Collections.sort(keys, KEY_COMPARATOR);
                        for (Object key : keys) {
                            String name = keyToString(key);
                            listener.beginObjectProperty(name);
                            Object mapValue = map.get(key);
                            visit(mapValue, sort, listener, stack, true);
                            listener.endObjectProperty(name);
                        }
                    } else {
                        for (Map.Entry<?, ?> entry : map.entrySet()) {
                            String name = keyToString(entry.getKey());
                            listener.beginObjectProperty(name);
                            Object mapValue = entry.getValue();
                            visit(mapValue, sort, listener, stack, true);
                            listener.endObjectProperty(name);
                        }
                    }
                    listener.endObject();
                    result = true;
                }
                if (!result) {
                    Iterable<?> list = getList(value);
                    if (list != null) {
                        listener.beginArray();
                        for (Object o : list) {
                            listener.beginArrayElement();
                            visit(o, sort, listener, stack, true);
                            listener.endArrayElement();
                        }
                        listener.endArray();
                        result = true;
                    }
                }
                if (!result) {
                    if (value.getClass().isArray()) {
                        listener.beginArray();
                        for (Object o : (Object[]) value) {
                            listener.beginArrayElement();
                            visit(o, sort, listener, stack, true);
                            listener.endArrayElement();
                        }
                        listener.endArray();
                        result = true;
                    }
                }

                if (!result) {
                    if (value instanceof Integer) {
                        listener.onValue((Integer) value);
                    } else if (value instanceof Long) {
                        listener.onValue((Long) value);
                    } else if (value instanceof Short) {
                        listener.onValue((Short) value);
                    } else if (value instanceof Character) {
                        listener.onValue((Character) value);
                    } else if (value instanceof Byte) {
                        listener.onValue((Byte) value);
                    } else if (value instanceof Double) {
                        listener.onValue((Double) value);
                    } else if (value instanceof Float) {
                        listener.onValue((Float) value);
                    } else if (value instanceof Boolean) {
                        listener.onValue((Boolean) value);
                    } else if (value instanceof String) {
                        listener.onValue((String) value);
                    } else {
                        String str = value != null ? value.toString() : null;
                        listener.onValue(str);
                    }
                }
            }
        } finally {
            stack.remove(value);
        }
    }

}
