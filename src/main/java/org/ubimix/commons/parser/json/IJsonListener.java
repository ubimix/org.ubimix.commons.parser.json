package org.ubimix.commons.parser.json;

import org.ubimix.commons.parser.IParserListener;

/**
 * @author kotelnikov
 */
public interface IJsonListener extends IParserListener {

    void beginArray();

    void beginArrayElement();

    void beginObject();

    void beginObjectProperty(String property);

    void endArray();

    void endArrayElement();

    void endObject();

    void endObjectProperty(String property);

    void onValue(boolean value);

    void onValue(double value);

    void onValue(int value);

    void onValue(long value);

    void onValue(String value);
}