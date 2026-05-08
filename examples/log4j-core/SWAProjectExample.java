package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.TriConsumer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * False positive example for {@link JdkMapAdapterStringMap}.
 * Place inside log4j-core/src/main/java/org/apache/logging/log4j/core/impl.
 */
public class SWAProjectExample implements StringMap {
    private static final String FROZEN = "Frozen collection cannot be modified";
    private static final Comparator<? super String> NULL_FIRST_COMPARATOR = (Comparator<String>) (left, right) -> {
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }
        return left.compareTo(right);
    };
    // Cache of known unmodifiable map implementations.
    // It is a cache, no need to synchronise it between threads.
    private static Map<Class<?>, Void> UNMODIFIABLE_MAPS_CACHE = new WeakHashMap<>();

    private final Map<String, String> map;
    private boolean immutable = false;
    // Annotated sortedKeys are nullable array of non-nulls
    private transient String @Nullable [] sortedKeys;

    public SWAProjectExample() {
        this(new HashMap<>(), false);
    }

    /**
     * Constructs a new {@link StringMap}, based on a JDK map.
     * <p>
     *     The underlying map should not be modified after this call.
     * </p>
     * <p>
     *     If the {@link Map} implementation does not allow modifications, {@code frozen} should be set to {@code true}.
     * </p>
     * @param map a JDK map,
     * @param frozen if {@code true} this collection will be immutable.
     */
    public SWAProjectExample(final Map<String, String> map, final boolean frozen) {
        this.map = Objects.requireNonNull(map, "map");
        this.immutable = frozen;
    }

    @Override
    public Map<String, String> toMap() {
        return new HashMap<>(map);
    }

    private void assertNotFrozen() {
        if (immutable) {
            throw new UnsupportedOperationException(FROZEN);
        }
    }

    @Override
    public boolean containsKey(final String key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
        final String[] keys = getSortedKeys(); // No warnings here
        for (int i = 0; i < keys.length; i++) {
            action.accept(keys[i], (V) map.get(keys[i]));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
        final String[] keys = getSortedKeys(); // No warnings here
        for (int i = 0; i < keys.length; i++) {
            action.accept(keys[i], (V) map.get(keys[i]), state);
        }
    }

    // Annotated getSortedKeys as non-null
    // Made public to expose to tests
    public @NonNull String[] getSortedKeys() {
        if (sortedKeys == null) {
            sortedKeys = map.keySet().toArray(Strings.EMPTY_ARRAY);
            Arrays.sort(sortedKeys, NULL_FIRST_COMPARATOR);
        }
        return sortedKeys;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getValue(final String key) {
        return (V) map.get(key);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        if (map.isEmpty()) {
            return;
        }
        assertNotFrozen();
        map.clear();
        sortedKeys = null; // Null assignment okay
    }

    @Override
    public void freeze() {
        immutable = true;
    }

    @Override
    public boolean isFrozen() {
        return immutable;
    }

    @Override
    public void putAll(final ReadOnlyStringMap source) {
        assertNotFrozen();
        source.forEach(PUT_ALL, map);
        sortedKeys = null; // Null assignment okay
    }

    private static final TriConsumer<String, String, Map<String, String>> PUT_ALL =
            (key, value, stringStringMap) -> stringStringMap.put(key, value);

    @Override
    public void putValue(final String key, final Object value) {
        assertNotFrozen();
        map.put(key, value == null ? null : String.valueOf(value));
        sortedKeys = null; // Null assignment okay
    }

    @Override
    public void remove(final String key) {
        if (!map.containsKey(key)) {
            return;
        }
        assertNotFrozen();
        map.remove(key);
        sortedKeys = null; // Null assignment okay
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(map.size() * 13);
        result.append('{');
        final String[] keys = getSortedKeys(); // No warnings here
        for (int i = 0; i < keys.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(keys[i]).append('=').append(map.get(keys[i]));
        }
        result.append('}');
        return result.toString();
    }

}
