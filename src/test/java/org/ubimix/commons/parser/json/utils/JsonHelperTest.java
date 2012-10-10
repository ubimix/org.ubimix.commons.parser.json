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
public class JsonHelperTest extends TestCase {

    public JsonHelperTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        IJsonParser parser = new JsonParser();
        String str = "{title: 'Hello, world!', content:'This is my new content'}";
        JsonSerializer listener = new JsonSerializer();
        parser.parse(str, listener);
        assertEquals(
            "{\"title\":\"Hello, world!\",\"content\":\"This is my new content\"}",
            listener.toString());

        listener.clear();
        JsonHelper helper = new JsonHelper(listener);
        helper.beginObject();
        {
            helper.addProperty("id", "007");
            helper.addProperty("title", "Hello, world!");
            helper.addProperty("age", 38);
            helper.addProperty("smoking", false);
            helper.addProperty("price", 345.567);
            helper.addProperty(
                "content",
                "This is the full content of this object");
            helper.beginProperty("item");
            {
                helper.beginArray();
                {
                    helper.beginObject();
                    {
                        helper.addProperty("id", "123");
                        helper.addProperty("rdf:type", "Test");
                        helper.addProperty("title", "My Super Feed Entry");
                        helper.addProperty(
                            "content",
                            "This is a very long description of an entry.");
                        helper.beginProperty("keywords");
                        helper.beginArray();
                        {
                            helper.addArrayValue("I am 'here'!");
                            helper.addArrayValue("And I am not...");
                            helper.addArrayValue("Мама мыла раму.");
                            helper.beginObject();
                            {
                                helper.beginProperty("text");
                                helper.end();
                            }
                            helper.end();
                        }
                        helper.end();
                        helper.end();
                    }
                    helper.end();
                }
                helper.end();
            }
            helper.end();
        }
        helper.end();
        assertEquals("{"
            + "\"id\":\"007\","
            + "\"title\":\"Hello, world!\","
            + "\"age\":38,"
            + "\"smoking\":false,"
            + "\"price\":345.567,"
            + "\"content\":\"This is the full content of this object\","
            + "\"item\":"
            + "["
            + "{"
            + "\"id\":\"123\","
            + "\"rdf:type\":\"Test\","
            + "\"title\":\"My Super Feed Entry\","
            + "\"content\":\"This is a very long description of an entry.\","
            + "\"keywords\":"
            + "["
            + "\"I am 'here'!\","
            + "\"And I am not...\","
            + "\"Мама мыла раму.\","
            + "{\"text\":null}"
            + "]"
            + "}"
            + "]"
            + "}"
            + "", listener.toString());
    }

}
