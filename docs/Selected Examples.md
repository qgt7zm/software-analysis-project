# Software Analysis Project Examples

## False Positives

### 1. compile-testing, CompilationSubject.java:525

```java
Streams<String, @Nullable Long>.map(
        /* Mapping to @Nullable Long */
    )
    .filter(notNull())
    .map(index -> index + 1)
```

Both checkers raised an "Unboxing of a nullable value" warning for `index`. The predicate `nonNull()` clearly filters
out null values of `index`. However, since `filter` does not alter the type parameter,
the underlying type in the second map is still `@Nullable Long`. Thus, the analyzers still believed the second `map`
could result in a null dereference. This warning could be silenced by choosing another sentinel value for empty values
of `index`, like -1.

### 2. guava, Optional.java:129

```java
@SuppressWarnings("NullableOptional")
public static <T> @Nullable Optional<T> fromJavaUtil(
    java.util.@Nullable Optional<T> javaUtilOptional) {
return (javaUtilOptional == null) ? null : fromNullable(javaUtilOptional.orElse(null));
}
```

The Guava `Optional<T>` class serves a similar purpose as the `java.util.Optional<T>`—wrapping a nullable object in a
type that allows checking for the presence or absence of a value. Normally, programmers who intend to check if the
wrapped object is `null` (i.e. the optional is empty) may accidentally check if the optional itself is null (a
consequence of Java's object-oriented focus). Therefore, both checkers raise the "Optional compared with null" warning
at the above method. However, since the method is meant to convert between two different `Optional<T>` classes, this
comparison is intended.

### 3. jsoup, HtmlTreeBuilderState.java:798

```java
boolean anyOtherEndTag(Token t, HtmlTreeBuilder tb) {
    final String name = t.asEndTag().normalName; // case insensitive search - goal is to preserve output case, not for the parse to be case sensitive
    /* ... */
    Element elFromStack = tb.getFromStack(name);
    /* ... */
    return true;
}
```

The field `Tag.normalName` is a nullable String, but NullAway warned that the `HtmlTreeBuilder.getFromStack()` method is
annotated to only accept non-null values. However, since `Tag` objects are reused to prevent garbage collection, the
only time `normalName` is actually null is when the `Tag` is just initialized or "reset". Calling `Tag.name()` will
set `normalName` to an empty string if the caller attempts to pass null. In fact, `getFromStack()` also performs safe
string comparisons. One way to dismiss the warnings is to use `Tag.normalName()`, which will raise an exception if
`normalName` is null.

### 4. log4j-core, JdkMapAdapterStringMap:185

```java
public void putValue(final String key, final Object value) {
    assertNotFrozen();
    map.put(key, value == null ? null : String.valueOf(value));
    sortedKeys = null;
}
```

The field `sortedKeys` is lazy-init, meaning it may be null at any time, but will be initialized through its accessor
method `getSortedKeys()`. Although both IntelliJ and NullAway warn about the null assignment to `sortedKeys`,
the field is always used safety throughout its class. The new `@EnsureNonNullIf` annotation can silence the warning.
To be more efficient, the developers can also avoid resorting the list on every modification and use linear-time
insertion and removal operations instead.

## True Positives

### 1. compile-test, Compiler.java:254

```java
Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value())
    .split(StandardSystemProperty.JAVA_CLASS_PATH.value()));
```

The function `value()` for `StandardSystemProperty` may return `null` in exceptionally rare cases if the runtime
environment is unusual. Since there is no null check of the return value, the calls to `Splitter` are technically
nullable dereferences. Regardless, testing if `value()` returns null would be impractical, as those paths are very
unlikely to be reached.

### 2. guava, NullnessCasts.java:54

```java
@SuppressWarnings("nullness")
static <T extends @Nullable Object> T uncheckedCastNullableTToT(@Nullable T t) {
    return t;
}
```

The following method performs a forced cast from `@Nullable T` to `@NonNull T` without checking whether the value of `t`
is null. This method may be used where the IDE or compiler might have trouble inferring the type parameter, such as
after `filter(Objects::nonNull)`. However, if a programmer neglected to filter to non-null values, then this method
would easily produce a `NullPointerException`.

### 3. jsoup, CharacterReader.java:64

```java
public void close() {
    if (reader == null)
        return;
    try {
        reader.close();
    } catch (IOException ignored) {
    } finally {
        reader = null;
        /* ... */
        charBuf = null;
        /* ... */
        stringCache = null;
    }
}
```

The `CharacterReader` class wraps a `java.io.Reader` and stores the buffer's contents in `charBuf`. Reused strings are
held in the `stringCache` array. Upon construction, the class opens its `reader` consumes the buffer fully. The
`close()` method closes the `reader` and also frees the `charBuf` and `stringCache` fields. However, the class contains
other methods that use the `charBuf` and `stringCache` without checking for whether they are null. If the reader was
used after being closed, then any of those methods would cause a `NullPointerException`. A better way to write the class
would be track the open/closed state with a boolean, with the caller or callee checking if the reader is closed first.

### log4j-core, AbstractLogEvent.java:74

```java
@Override
public Message getMessage() {
    return null;
}
```

The `AbstractLogEvent` class implements the `LogEvent` interface, while providing dummy implementations of each method
that return `null`. This interface-abstract-concrete pattern is common on Java projects with large inheritance trees.
The problem occurs when developers use these classes without realizing they need to extend all the methods. `LogEvent`
is null-marked, which means the methods must return non-null. The Javadoc comments are unclear whether this behavior is
intended, or if someone forgot to annotate the methods with `@Nullable`. A programmer creating a subclass could easily
create a method returning `null`, only for another class to attempt to deference the null reference.