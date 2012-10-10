/**
 * 
 */
package org.ubimix.commons.parser.json;

/**
 * This serializer transforms the sequence of method calls for the
 * {@link IJsonListener} interface into a JSON string representation.
 * 
 * @author kotelnikov
 */
public class JsonSerializer extends AbstractJsonSerializer {

    private StringBuilder fBuffer = new StringBuilder();

    /**
     * 
     */
    public JsonSerializer() {
    }

    /**
     * @param ident
     */
    public JsonSerializer(int ident) {
        super(ident);
    }

    /**
     * Cleans up the internal buffer.
     */
    public void clear() {
        fBuffer.delete(0, fBuffer.length());
    }

    public StringBuilder getBuffer() {
        return fBuffer;
    }

    /**
     * @see org.ubimix.commons.parser.json.AbstractJsonSerializer#print(java.lang.String)
     */
    @Override
    protected void print(String string) {
        fBuffer.append(string);
    }

    public void setBuffer(StringBuilder buffer) {
        fBuffer = buffer;
    }

    @Override
    public String toString() {
        return fBuffer.toString();
    }

}
