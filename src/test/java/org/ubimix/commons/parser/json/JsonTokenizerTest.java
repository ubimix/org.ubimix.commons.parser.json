/**
 * 
 */
package org.ubimix.commons.parser.json;

import junit.framework.TestCase;

import org.ubimix.commons.parser.CharStream;
import org.ubimix.commons.parser.ICharStream;
import org.ubimix.commons.parser.StreamToken;

/**
 * @author kotelnikov
 */
public class JsonTokenizerTest extends TestCase {

    /**
     * @param name
     */
    public JsonTokenizerTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        test("\n", "SpacesToken:\n");
        test("{", "ObjectBeginToken:{");
        test("{}", "ObjectBeginToken:{", "ObjectEndToken:}");
        test(" ", "SpacesToken: ");
        test(":", "ColumnToken::");
        test(
            "{ a: b }",
            "ObjectBeginToken:{",
            "SpacesToken: ",
            "StringToken:a",
            "ColumnToken::",
            "SpacesToken: ",
            "StringToken:b",
            "SpacesToken: ",
            "ObjectEndToken:}");
    }

    private void test(String str, String... tokens) {
        ICharStream stream = new CharStream(str);
        JsonTokenizer tokenizer = new JsonTokenizer();
        for (String control : tokens) {
            StreamToken token = tokenizer.read(stream);
            assertNotNull(token);
            assertEquals(control, toString(token));
        }
    }

    private String toString(StreamToken token) {
        String name = token.getClass().getName();
        int idx = name.lastIndexOf('.');
        if (idx > 0) {
            name = name.substring(idx + 1);
        }
        idx = name.lastIndexOf('$');
        if (idx > 0) {
            name = name.substring(idx + 1);
        }
        String result = name + ":" + token.getText();
        return result;
    }

}
