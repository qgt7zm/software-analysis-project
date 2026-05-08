package org.jsoup.parser;

import org.jsoup.helper.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * True and false positive tests for {@link CharacterReader#close} and {@link Token.Tag.normalName.}
 * Put inside src/test/java/org/jsoup/parser.
 */
public class SWAProjectTest {

    @Test
    public void useBeforeClose() {
        // Create the reader and read the buffer fully
        CharacterReader r = new CharacterReader("foo bar baz quux");

        // charBuf and stringCache are not null
        assertDoesNotThrow(() -> r.current());
        assertDoesNotThrow(() -> r.consumeTo(" "));

        r.close();
    }

    @Test
    public void useAfterClose() {
        // Create the reader and read the buffer fully
        CharacterReader r = new CharacterReader("foo bar baz quux");

        // Close the reader and set charBuf and stringCache to null
        r.close();

        // charBuf and stringCache are null
        assertThrows(NullPointerException.class, () -> r.current());
        assertThrows(NullPointerException.class, () -> r.consumeTo(" "));
    }

    @Test
    public void tagNormalNameNotNull() {
        Token.Tag tag = new Token.EndTag(new HtmlTreeBuilder());

        // Null on creation
        assertNull(tag.normalName);
        assertThrows(ValidationException.class, () -> tag.normalName());

        // Passing null sets to empty string
        tag.name(null);
        assertNotNull(tag.normalName);
        assertThrows(ValidationException.class, () -> tag.normalName()); // Still throws if empty

        // Passing null sets to empty string
        tag.name("foo");
        assertNotNull(tag.normalName);
        assertDoesNotThrow(() -> tag.normalName());

        // Null after reset
        tag.reset();
        assertNull(tag.normalName);
        assertThrows(ValidationException.class, () -> tag.normalName());
    }

}
