/* ************************************************************************** *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. This file is licensed to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * **************************************************************************
 */
package org.ubimix.commons.parser.json;

import java.util.Stack;

import junit.framework.TestCase;

import org.ubimix.commons.parser.AbstractParser;

/**
 * @author kotelnikov
 */
public class JsonParserTest extends TestCase {

    protected static class TestJsonParserListener extends JsonListener {

        private final StringBuilder fBuf;

        Stack<Integer> fStack = new Stack<Integer>();

        public TestJsonParserListener() {
            this(null);
        }

        public TestJsonParserListener(StringBuilder buf) {
            fBuf = buf != null ? buf : new StringBuilder();
        }

        @Override
        public void beginArray() {
            fBuf.append("[");
            fStack.push(0);
        }

        @Override
        public void beginArrayElement() {
            if (inc() > 0) {
                print(",");
            }
            print("(");
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
            print("<" + property + ">");
        }

        @Override
        public void endArray() {
            fStack.pop();
            fBuf.append("]");
        }

        @Override
        public void endArrayElement() {
            print(")");
        }

        @Override
        public void endObject() {
            fStack.pop();
            print("}");
        }

        @Override
        public void endObjectProperty(String property) {
            print("</" + property + ">");
        }

        private int inc() {
            int idx = fStack.size() - 1;
            int num = fStack.get(idx);
            fStack.set(idx, num + 1);
            return num;
        }

        @Override
        public void onValue(boolean value) {
            onValue("" + value);
        }

        @Override
        public void onValue(double value) {
            onValue("" + value);
        }

        @Override
        public void onValue(int value) {
            onValue("" + value);
        }

        @Override
        public void onValue(long value) {
            onValue("" + value);
        }

        @Override
        public void onValue(String value) {
            print(value);
        }

        private void print(String string) {
            fBuf.append(string);
        }

        @Override
        public String toString() {
            return fBuf.toString();
        }
    }

    /**
     * @param name
     */
    public JsonParserTest(String name) {
        super(name);
    }

    protected JsonParser newJsonParser() {
        return new JsonParser();
    }

    protected void println(String string) {
        // System.out.println(string);
    }

    public void test() {
        test("{\"a\":{\n},\n\"b\":\"C\"\n}", ""
            + "{"
            + "<a>{}</a>,"
            + "<b>C</b>"
            + "}");

        // Check that "null" values are accepted
        test("{a:null}", "{<a>null</a>}");
        test("{a:,b:x,c:}", "{<a>null</a>,<b>x</b>,<c>null</c>}");
        test(
            "{a:[123, null, 0.5, hello]}",
            "{<a>[(123),(null),(0.5),(hello)]</a>}");

        test("{ 'price' : 12345}", "{<price>12345</price>}");
        test("{x: 'abc\\u003Cfoo\\u003Ecde' }", "{<x>abc<foo>cde</x>}");
        test(
            "{ ' something very long ' : ' A very long value '}",
            "{< something very long > A very long value </ something very long >}");

        test("{'abc':'cde'}", "{<abc>cde</abc>}");
        test("  {'abc':'cde'}  ", "{<abc>cde</abc>}");
        test("  {  'abc'  :  'cde'  }  ", "{<abc>cde</abc>}");
        test(
            "  {  'abc'  :  { 'cde' : 'x' }  }  ",
            "{<abc>{<cde>x</cde>}</abc>}");

        test(
            "  {  'A'  :  { 'x' : 'X' } , y:Y, \"z\" : Z  }  ",
            "{<A>{<x>X</x>}</A>,<y>Y</y>,<z>Z</z>}");
        test("{ a : A, b : B, c\n : \n C }", "{<a>A</a>,<b>B</b>,<c>C</c>}");
        test(
            "{ a : 'gklm qsdg qm \n qsdkgk \n\n\nqsldgjmqsdgqsdg'}",
            "{<a>gklm qsdg qm \n qsdkgk \n\n\nqsldgjmqsdgqsdg</a>}");
        test(
            "{ a : 'gklm qsdg qm \\n qsdkgk \\n\\n\\nqsldgjmqsdgqsdg'}",
            "{<a>gklm qsdg qm \n qsdkgk \n\n\nqsldgjmqsdgqsdg</a>}");

        test("{ a : '\\''}", "{<a>'</a>}");
        test("{ a : \"'\"}", "{<a>'</a>}");
        test("{ a : '\"'}", "{<a>\"</a>}");
        test(
            "{ x: [ 'toto', 'titi', 'tata' ] }",
            "{<x>[(toto),(titi),(tata)]</x>}");
        test(
            "{ x: [ 'toto', { y: 'titi' }, 'tata' ] }",
            "{<x>[(toto),({<y>titi</y>}),(tata)]</x>}");

        test("{ x: [ {}, {}, {} ] }", "{<x>[({}),({}),({})]</x>}");

        // BAD FORMED!!!
        test("{ x: ", "{<x>null</x>}");
        test("{ x: [ y ", "{<x>[(y)]</x>}");
        // test(" qdsgqsdg { x: [ y ", "{<x>[(y)]</x>}");
        test(" qdsgqsdg { x: [ y ", "qdsgqsdg");

        test(" { [x, y] }", "{}");
        test(
            "{first: { name: [x, y] }}",
            "{<first>{<name>[(x),(y)]</name>}</first>}");
        test(
            "{first: { name: [x, y, {first: { name: [x, y] } } ] }}",
            "{<first>{<name>[(x),(y),({<first>{<name>[(x),(y)]</name>}</first>})]</name>}</first>}");

        // test(
        // "{msg: This is a long message, code: 123}",
        // "{<msg>This is a long message</msg>,<code>123</code>}");
        // test(
        // "{    rdf\\:type: rdfs:Resource,    news: This is a short message. }",
        // ""
        // + "{"
        // + "<rdf:type>rdfs:Resource</rdf:type>,"
        // + "<news>This is a short message.</news>"
        // + "}");

        // test(
        // "{ hello\\ world : Value with the '\\:' sign. }",
        // "{<hello world>Value with the ':' sign.</hello world>}");

        test("{\n"
            + "  \"jsonrpc\":\"2.0\",\n"
            + "  \"id\":\"123\",\n"
            + "  \"params\":{\n"
            + "  },\n"
            + "  \"method\":\"testJsonRpcCall\"\n"
            + "}", ""
            + "{"
            + "<jsonrpc>2.0</jsonrpc>,"
            + "<id>123</id>,"
            + "<params>{}</params>,"
            + "<method>testJsonRpcCall</method>"
            + "}");
    }

    private void test(String str, String control) {
        IJsonParser parser = newJsonParser();
        TestJsonParserListener listener = new TestJsonParserListener();
        parser.parse(str, listener);
        println(str + " => " + listener.toString());
        assertEquals(control, listener.toString());
    }

    public void testErrors() {
        testErrors("{", "{", "The '}' symbol expected.", "1[0:1]");
        testErrors("{\n", "{", "The '}' symbol expected.", "2[1:0]");
        testErrors("{ x: ", "{<x>", "Property value is expected.", "5[0:5]");
        testErrors("{ x: }", "{<x>", "Property value is expected.", "6[0:6]");
        testErrors("{ x: ''", "{<x></x>", "The '}' symbol expected.", "7[0:7]");
        testErrors(
            "{ x: [ y ",
            "{<x>[(y)",
            "The ']' symbol expected.",
            "9[0:9]");
        testErrors(
            "{ x:'n'} toto",
            "{<x>n</x>}",
            "The end of the stream is expected.",
            "8[0:8]");
    }

    private void testErrors(
        String str,
        String control,
        String msg,
        String posControl) {
        TestJsonParserListener listener = new TestJsonParserListener();
        listener.setReportErrors(true);
        try {
            IJsonParser parser = newJsonParser();
            parser.parse(str, listener);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(control, listener.toString());
            assertTrue(e instanceof AbstractParser.ParseError);
            AbstractParser.ParseError parseError = (AbstractParser.ParseError) e;
            String message = parseError.getMessage();
            assertEquals(msg, message);
            String posStr = parseError.getPointer() + "";
            assertEquals(posControl, posStr);
        }
    }

    public void testPrintQuery() {
        String str = "{\"ResultSet\":{\"totalResultsAvailable\":\"415870\",\"totalResultsReturned\":2,\"firstResultPosition\":1,\"Result\":[{\"Title\":\"potato.jpg\",\"Summary\":\"Exclude Chit Chat \\u2014 The Introducer at 8:26 pm on Saturday, October 21, 2006 The OFT transferring PPI to the Competition Commission could be seen as getting rid of a Hot Potato - but it was a struggle to find a picture of a potato that looked  Hot  I've had a first\",\"Url\":\"http:\\/\\/www.we-introduce-you.co.uk\\/theintroducer\\/wp-content\\/potato.jpg\",\"ClickUrl\":\"http:\\/\\/www.we-introduce-you.co.uk\\/theintroducer\\/wp-content\\/potato.jpg\",\"RefererUrl\":\"http:\\/\\/www.we-introduce-you.co.uk\\/theintroducer\\/90_the-hot-potato-of-payment-protection-insurance\",\"FileSize\":5632,\"FileFormat\":\"jpeg\",\"Height\":\"225\",\"Width\":\"225\",\"Thumbnail\":{\"Url\":\"http:\\/\\/sp1.yt-thm-a01.yimg.com\\/image\\/25\\/m3\\/2697440748\",\"Height\":\"130\",\"Width\":\"130\"}},{\"Title\":\"Long_White_Potato_826.JPG\",\"Summary\":\"Fingerling_Potato_65..  04-Jun-2001 10:07 35k Idaho_Russet_Potato_..  04-Jun-2001 10:07 24k Long_White_Potato_82..  04-Jun-2001 10:07 29k New_Potato_661.JPG 04-Jun-2001 10:07 33k\",\"Url\":\"http:\\/\\/www.gothamstudio.com\\/images\\/Vegetables\\/Potatos\\/Long_White_Potato_826.JPG\",\"ClickUrl\":\"http:\\/\\/www.gothamstudio.com\\/images\\/Vegetables\\/Potatos\\/Long_White_Potato_826.JPG\",\"RefererUrl\":\"http:\\/\\/www.gothamstudio.com\\/images\\/Vegetables\\/Potatos\",\"FileSize\":29184,\"FileFormat\":\"jpeg\",\"Height\":\"342\",\"Width\":\"504\",\"Thumbnail\":{\"Url\":\"http:\\/\\/sp1.yt-thm-a01.yimg.com\\/image\\/25\\/m4\\/2958963693\",\"Height\":\"98\",\"Width\":\"145\"}}]}}";
        TestJsonParserListener listener = new TestJsonParserListener();
        IJsonParser parser = newJsonParser();
        parser.parse(str, listener);
        println(str + "\n\n" + listener.toString());
    }

}
