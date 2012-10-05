package org.ubimix.commons.parser.json;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Test for org.ubimix.commons.parser.json");
        // $JUnit-BEGIN$
        suite.addTestSuite(JsonParserTest.class);
        suite.addTestSuite(JsonTokenizerTest.class);
        // $JUnit-END$
        return suite;
    }
}
