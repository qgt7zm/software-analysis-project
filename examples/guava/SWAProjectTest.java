package com.google.common.base;

import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.NullnessCasts.uncheckedCastNullableTToT;

/**
 * True positive test for {@link NullnessCasts#uncheckedCastNullableTToT}.
 * Put inside guava-tests/test/com/google/common/base
 */
@NullMarked
public class SWAProjectTest extends TestCase {

    public void testUnsafeCastNullableToNonNull() {
        @Nullable String[] strings = {
                "AAA", "BBB", "CCC", "DDD", null, null
        };

        // Forgot to filter for non-null
        List<@Nullable String> nonNullStrings = Arrays.stream(strings)
                .map(t -> uncheckedCastNullableTToT(t))
                .collect(Collectors.toList());

        boolean hasNull = false;
        for (String str : nonNullStrings) {
            if (str == null) hasNull = true;
        }
        assertTrue(hasNull); // Fails
    }

    public void testSafeCastNullableToNonNull() {
        @Nullable String[] strings = {
                "AAA", "BBB", "CCC", "DDD", null, null
        };

        // Filtered for non-null
        List<@Nullable String> nonNullStrings = Arrays.stream(strings)
                .filter(Objects::nonNull)
                .map(t -> uncheckedCastNullableTToT(t))
                .collect(Collectors.toList());

        for (String str : nonNullStrings) {
            assertNotNull(str); // Passes
        }
    }

}
