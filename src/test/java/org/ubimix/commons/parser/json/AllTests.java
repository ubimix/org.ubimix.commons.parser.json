package org.ubimix.commons.parser.json;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.ubimix.commons.parser.json.utils.JsonHelperTest;
import org.ubimix.commons.parser.json.utils.JsonSerializeDeserializeTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Test for org.ubimix.commons.parser.json");
        // $JUnit-BEGIN$
        suite.addTestSuite(JsonParserTest.class);
        suite.addTestSuite(JsonTokenizerTest.class);
        suite.addTestSuite(JsonHelperTest.class);
        suite.addTestSuite(JsonSerializeDeserializeTest.class);
        // $JUnit-END$
        return suite;
    }
}
