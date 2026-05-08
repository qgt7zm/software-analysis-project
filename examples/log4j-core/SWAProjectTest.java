package org.apache.logging.log4j;

import org.apache.logging.log4j.core.AbstractLogEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.JdkMapAdapterStringMap;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.util.StringMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * True and false positive tests for {@link AbstractLogEvent} and {@link JdkMapAdapterStringMap}.
 * Place inside log4j-core-test/src/test/java/org/apache/logging/log4j.
 */
public class SWAProjectTest {

    // True positive example
    @Test
    void abstractClassNullProperties() {
        // Extend the abstract class without overriding all its methods
        LogEvent event = new TestLogEvent("logger");
        assertNull(event.getMessage());

        // Dereference the message elsewhere
        assertThrows(NullPointerException.class, () -> event.getMessage().toString());
    }

    // False positive example
    @Test
    void lazyInitSortedKeysNotNull() {
        // Initialze the map with some key-value pairs
        JdkMapAdapterStringMap map = new JdkMapAdapterStringMap(new HashMap(
                Map.of("a", "1", "b", "2")
        ), false);

        // Add a pair and invalidate the sorted keys
        map.putValue("c", "3");
        assertDoesNotThrow(() -> map.toString()); // toString() calls private getSortedKeys()

        // Remove a pair and invalidate the sorted keys
        map.remove("a");
        assertDoesNotThrow(() -> map.toString());

        // Clear all pairs and invalidate the sorted keys
        map.clear();
        assertDoesNotThrow(() -> map.toString());
    }

    class TestLogEvent extends AbstractLogEvent {
        private final String loggerName;

        public TestLogEvent(String loggerName) {
            this.loggerName = loggerName;
        }

        @Override
        public String getLoggerName() {
            return loggerName;
        }
    }

}
