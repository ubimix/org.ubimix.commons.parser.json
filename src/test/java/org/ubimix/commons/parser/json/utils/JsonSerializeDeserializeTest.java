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
package org.ubimix.commons.parser.json.utils;

import junit.framework.TestCase;

import org.ubimix.commons.parser.json.IJsonParser;
import org.ubimix.commons.parser.json.JsonParser;
import org.ubimix.commons.parser.json.JsonSerializer;

/**
 * @author kotelnikov
 */
public class JsonSerializeDeserializeTest extends TestCase {

    /**
     * @param name
     */
    public JsonSerializeDeserializeTest(String name) {
        super(name);
    }

    protected void println(String string) {
        // System.out.println(string);
    }

    public void test() {
        test("{abc\\\"def:xyz}", "{\"abc\\\"def\":\"xyz\"}");

        test("{value:null}", "{\"value\":null}");
        test("{value:'123'}", "{\"value\":\"123\"}");
        test("{value:123}", "{\"value\":123}");
        test("{abc\\\"def:xyz}", "{\"abc\\\"def\":\"xyz\"}");

        test("{price:123}", "{\"price\":123}");
        test("", "");
        test("{}", "{}");
        test("  {    }    ", "{}");
        test("  { x : y    }    ", "{\"x\":\"y\"}");
        test("  { x :   }    ", "{\"x\":null}");
        test(
            "  { a : A, b:B, \"c\" : 'C' }    ",
            "{\"a\":\"A\",\"b\":\"B\",\"c\":\"C\"}");
        test(
            "  { a : {b:B, \"c\" : 'C'   } }    ",
            "{\"a\":{\"b\":\"B\",\"c\":\"C\"}}");
        test("{ x:'a\nb\nc'}", "{\"x\":\"a\\nb\\nc\"}");
        test("{ x:'a\n\n  b\n  c\n'}", "{\"x\":\"a\\n\\n  b\\n  c\\n\"}");
    }

    private void test(int ident, String str, String control) {
        IJsonParser parser = new JsonParser();
        JavaObjectBuilder util = new JavaObjectBuilder();
        parser.parse(str, util);
        Object obj = util.getTop();

        // Visit the object and create a copy.
        util.reset();
        JavaObjectVisitor visitor = new JavaObjectVisitor();
        visitor.visit(obj, util);
        Object test = util.getTop();
        if (obj == null) {
            assertNull(test);
        } else {
            assertFalse(obj == test);
            assertEquals(obj, test);
        }

        // Serialize the object and check that it corresponds to the expected
        // JSON.
        JsonSerializer serializer = new JsonSerializer(ident);
        visitor.visit(obj, serializer);
        println(str + " => " + serializer);
        assertEquals(control, serializer.toString());
    }

    private void test(String str, String control) {
        test(0, str, control);
    }

    public void testArrays() {
        test("[a]", "[\"a\"]");
        test("[first, second, third]", "[\"first\",\"second\",\"third\"]");
    }

    public void testIdent() {
        testIdent("{value:null}", "{\n  \"value\":null\n}");
        testIdent(
            "{related:['abc', 123, 0.3, null, "
                + "{addr: { city: Paris, street: Rivoli, building: 1 }, name:'John Smith'}, "
                + "'cde']}",
            ""
                + "{\n"
                + "  \"related\":[\n"
                + "    \"abc\",\n"
                + "    123,\n"
                + "    0.3,\n"
                + "    null,\n"
                + "    {\n"
                + "      \"addr\":{\n"
                + "        \"city\":\"Paris\",\n"
                + "        \"street\":\"Rivoli\",\n"
                + "        \"building\":1\n"
                + "      },\n"
                + "      \"name\":\"John Smith\"\n"
                + "    },\n"
                + "    \"cde\"\n"
                + "  ]\n"
                + "}");
        testIdent("{\"rdf:RDF\":["
            + "{\"rdf:id\":\"toto:model\"},"
            + "{"
            + "\"rdf:id\":\"toto:person1\","
            + "\"firstName\":\"John\","
            + "\"lastName\":\"Smith\","
            + "\"livesIn\":\"toto:city1\""
            + "},"
            + "{"
            + "\"rdf:id\":\"toto:city1\","
            + "\"name\":\"NY\","
            + "\"phone\":[\"123-32-23-44\",\"123-33-24-45\"],"
            + "\"description\":\"abazdg\\nqsldkgj\\nqsdlfkjqsmdf\""
            + "}"
            + "]}", ""
            + "{\n"
            + "  \"rdf:RDF\":[\n"
            + "    {\n"
            + "      \"rdf:id\":\"toto:model\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"rdf:id\":\"toto:person1\",\n"
            + "      \"firstName\":\"John\",\n"
            + "      \"lastName\":\"Smith\",\n"
            + "      \"livesIn\":\"toto:city1\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"rdf:id\":\"toto:city1\",\n"
            + "      \"name\":\"NY\",\n"
            + "      \"phone\":[\n"
            + "        \"123-32-23-44\",\n"
            + "        \"123-33-24-45\"\n"
            + "      ],\n"
            + "      \"description\":\"abazdg\\nqsldkgj\\nqsdlfkjqsmdf\"\n"
            + "    }\n"
            + "  ]\n"
            + "}");
    }

    private void testIdent(String str, String control) {
        test(2, str, control);
    }

    public void testObjectWithArrays() {
        test("  { x : [ A,  B, C]   }    ", "{\"x\":[\"A\",\"B\",\"C\"]}");
        test(
            "{related:['abc', 123, 0.3, null, {}, 'cde']}",
            "{\"related\":[\"abc\",123,0.3,null,{},\"cde\"]}");
        test("{\"rdf:RDF\":["
            + "{\"rdf:id\":\"toto:model\"},"
            + "{"
            + "\"rdf:id\":\"toto:person1\","
            + "\"firstName\":\"John\","
            + "\"lastName\":\"Smith\","
            + "\"livesIn\":\"toto:city1\""
            + "},"
            + "{"
            + "\"rdf:id\":\"toto:city1\","
            + "\"name\":\"NY\","
            + "\"phone\":[\"123-32-23-44\",\"123-33-24-45\"],"
            + "\"description\":\"abazdg\\nqsldkgj\\nqsdlfkjqsmdf\""
            + "}"
            + "]}", "{\"rdf:RDF\":["
            + "{\"rdf:id\":\"toto:model\"},"
            + "{"
            + "\"rdf:id\":\"toto:person1\","
            + "\"firstName\":\"John\","
            + "\"lastName\":\"Smith\","
            + "\"livesIn\":\"toto:city1\""
            + "},"
            + "{"
            + "\"rdf:id\":\"toto:city1\","
            + "\"name\":\"NY\","
            + "\"phone\":[\"123-32-23-44\",\"123-33-24-45\"],"
            + "\"description\":\"abazdg\\nqsldkgj\\nqsdlfkjqsmdf\""
            + "}"
            + "]}");
    }

    public void testOrdered() {
        testOrdered(
            "{d:D,c:C,b:B,a:A}",
            "{\"a\":\"A\",\"b\":\"B\",\"c\":\"C\",\"d\":\"D\"}");
        testOrdered(
            "{second:'Hello, there', price:123, first:false}",
            "{\"first\":false,\"price\":123,\"second\":\"Hello, there\"}");
    }

    private void testOrdered(String str, String control) {
        JsonParser parser = new JsonParser();
        JavaObjectBuilder util = new JavaObjectBuilder();
        parser.parse(str, util);
        Object obj = util.getTop();
        // Serialize the object and check that it corresponds to the expected
        // JSON.
        JsonSerializer serializer = new JsonSerializer();
        JavaObjectVisitor visitor = new JavaObjectVisitor();
        visitor.visit(obj, true, serializer);
        println(str + " => " + serializer);
        assertEquals(control, serializer.toString());
    }

}
