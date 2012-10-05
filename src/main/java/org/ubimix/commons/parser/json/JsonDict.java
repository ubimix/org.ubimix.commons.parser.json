/**
 * 
 */
package org.ubimix.commons.parser.json;

import org.ubimix.commons.parser.StreamToken;

/**
 * A basic dictionary defining all basic tokens like "word", "space", "special"
 * (special symbols), and "eol" (end of the line).
 * 
 * @author kotelnikov
 */
public class JsonDict {

    public static class ArrayBeginToken extends StreamToken {
    }

    public static class ArrayEndToken extends StreamToken {
    }

    public static class BooleanToken extends StreamToken {
    }

    public static class ColumnToken extends StreamToken {
    }

    public static class CommaToken extends StreamToken {
    }

    public static class NullToken extends StreamToken {
    }

    public static class ObjectBeginToken extends StreamToken {
    }

    public static class ObjectEndToken extends StreamToken {
    }

    public static class StringToken extends StreamToken {
    }

}
