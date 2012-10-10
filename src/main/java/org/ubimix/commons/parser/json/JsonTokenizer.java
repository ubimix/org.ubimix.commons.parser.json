/**
 * 
 */
package org.ubimix.commons.parser.json;

import org.ubimix.commons.parser.CompositeTokenizer;
import org.ubimix.commons.parser.StreamToken;
import org.ubimix.commons.parser.base.EscTokenizer;
import org.ubimix.commons.parser.base.QuotedValueTokenizer;
import org.ubimix.commons.parser.base.SequenceTokenizer;
import org.ubimix.commons.parser.base.SimpleTokenizer;
import org.ubimix.commons.parser.text.TextDict;

/**
 * @author kotelnikov
 */
public class JsonTokenizer extends CompositeTokenizer {

    public JsonTokenizer() {
        addTokenizer(new SimpleTokenizer() {
            @Override
            protected boolean checkChar(char ch, int pos) {
                return Character.isSpaceChar(ch)
                    || ch == '\r'
                    || ch == '\n'
                    || ch == '\t';
            }

            @Override
            protected StreamToken newToken() {
                return new TextDict.SpacesToken();
            }
        });
        addTokenizer(new QuotedValueTokenizer() {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.StringToken();
            }
        });
        addTokenizer(new SequenceTokenizer(":") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.ColumnToken();
            }
        });
        addTokenizer(new SequenceTokenizer("[") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.ArrayBeginToken();
            }
        });
        addTokenizer(new SequenceTokenizer("]") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.ArrayEndToken();
            }
        });
        addTokenizer(new SequenceTokenizer("{") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.ObjectBeginToken();
            }
        });
        addTokenizer(new SequenceTokenizer("}") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.ObjectEndToken();
            }
        });
        addTokenizer(new SequenceTokenizer("null") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.NullToken();
            }
        });
        addTokenizer(new SequenceTokenizer(",") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.CommaToken();
            }
        });
        addTokenizer(new SequenceTokenizer("true") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.BooleanToken();
            }
        });
        addTokenizer(new SequenceTokenizer("false") {
            @Override
            protected StreamToken newToken() {
                return new JsonDict.BooleanToken();
            }
        });
        addTokenizer(new EscTokenizer() {
            @Override
            protected boolean checkChar(char ch, int pos) {
                if (Character.isLetterOrDigit(ch) || ch == '+' || ch == '-') {
                    return true;
                }
                return pos > 0 && (ch == '.' || ch == ';');
            }

            @Override
            protected StreamToken newToken() {
                return new JsonDict.StringToken();
            }
        });
    }

}
