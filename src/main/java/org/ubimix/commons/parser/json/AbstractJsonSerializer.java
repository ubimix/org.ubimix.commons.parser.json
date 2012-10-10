/* ************************************************************************** *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * This file is licensed to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ************************************************************************** */
package org.ubimix.commons.parser.json;

import java.util.Stack;

import org.ubimix.commons.parser.ParserListener;

/**
 * This class implements a JSON serializer transforming calls to the
 * {@link IJsonListener} interface into a string JSON representation. Subclasses
 * should implement the {@link #print(String)} method.
 * 
 * @author kotelnikov
 */
public abstract class AbstractJsonSerializer extends ParserListener
    implements
    IJsonListener {

    private static final String QUOT = "\"";

    private StringBuffer fBuf = new StringBuffer();

    private int fIdent;

    private Stack<Integer> fStack = new Stack<Integer>();

    /**
     * 
     */
    public AbstractJsonSerializer() {
        this(0);
    }

    public AbstractJsonSerializer(int ident) {
        fIdent = ident;
    }

    @Override
    public void beginArray() {
        print("[");
        fStack.push(0);
    }

    @Override
    public void beginArrayElement() {
        if (inc() > 0) {
            print(",");
        }
        printIdent();
    }

    @Override
    public void beginObject() {
        print("{");
        fStack.push(0);
    }

    @Override
    public void beginObjectProperty(String property) {
        if (inc() > 0) {
            print(",");
        }
        printIdent();
        fBuf.delete(0, fBuf.length());
        escape(property, fBuf);
        print(QUOT);
        print(fBuf.toString());
        print(QUOT);
        print(":");
    }

    @Override
    public void endArray() {
        fStack.pop();
        printIdent();
        print("]");
    }

    @Override
    public void endArrayElement() {
    }

    @Override
    public void endObject() {
        fStack.pop();
        printIdent();
        print("}");
    }

    @Override
    public void endObjectProperty(String property) {
    }

    protected boolean escape(String str, StringBuffer buf) {
        boolean result = false;
        if (str == null) {
            return result;
        }
        int len = str.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        for (int x = 0; x < len; x++) {
            char aChar = str.charAt(x);
            switch (aChar) {
                case '\"':
                    buf.append("\\\"");
                    result = true;
                    break;
                case '\t':
                    buf.append("\\t");
                    result = true;
                    break;
                case '\n':
                    buf.append("\\n");
                    result = true;
                    break;
                case '\r':
                    buf.append("\\r");
                    result = true;
                    break;
                case '\f':
                    buf.append("\\f");
                    result = true;
                    break;
                case '\\':
                    buf.append("\\\\");
                    result = true;
                    break;
                case '\'': // Fall through
                case ':': // Fall through
                case ';': // Fall through
                case '+': // Fall through
                case '-': // Fall through
                case '/': // Fall through
                case '=': // Fall through
                case '!':
                    buf.append(aChar);
                    result = true;
                    break;
                default:
                    if ((aChar > 61) && (aChar < 127)) {
                        buf.append(aChar);
                    } else if (aChar < 0x0020) {
                        buf.append("\\u");
                        buf.append(Integer.toHexString((aChar >> 12) & 0xF));
                        buf.append(Integer.toHexString((aChar >> 8) & 0xF));
                        buf.append(Integer.toHexString((aChar >> 4) & 0xF));
                        buf.append(Integer.toHexString(aChar & 0xF));
                        result = true;
                    } else {
                        buf.append(aChar);
                    }
            }
        }
        return result;
    }

    private int inc() {
        int idx = fStack.size() - 1;
        int num = fStack.get(idx);
        fStack.set(idx, num + 1);
        return num;
    }

    @Override
    public void onValue(boolean value) {
        print(Boolean.toString(value));
    }

    @Override
    public void onValue(double value) {
        print(Double.toString(value));
    }

    @Override
    public void onValue(int value) {
        print(Integer.toString(value));
    }

    @Override
    public void onValue(long value) {
        print(Long.toString(value));
    }

    @Override
    public void onValue(String value) {
        if (value == null) {
            print("null");
        } else {
            print(QUOT);
            fBuf.delete(0, fBuf.length());
            escape(value, fBuf);
            print(fBuf.toString());
            print(QUOT);
        }
    }

    protected abstract void print(String string);

    protected void printIdent() {
        if (fIdent > 0) {
            StringBuilder buf = new StringBuilder();
            buf.append("\n");
            int len = fIdent * fStack.size();
            for (int i = 0; i < len; i++) {
                buf.append(" ");
            }
            print(buf.toString());
        }
    }

}
