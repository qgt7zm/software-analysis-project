# Software Analysis Project Warnings

## General

- NullAway assumes `@NonNull` by default in `@NullMarked` code
    - Unannotated outside code (including the Java API) is considered `@NullMarked`
- In `@NullMarked` packages/classes, fields are non-null be default
- Skipping IDE warnings about style and redundant checks
- Ignoring assert statements for NullAway (not always supported in runtime)
- Must compile from clean build (not incremental) to raise all warnings

### Warning Reasons

- False positive warnings will not lead to an NPE assuming correct annotation
- True positive warnings will lead to an NPE without additional code fixes
- Uncertain warnings may or may not lead to an NPE, and should be treated as true positives

### Assertions

- `assert val != null` statements ignored: **false positive**
    - Enabled through `-XepOpt:NullAway:AssertsEnabled=true` flag
- Nullable property checked non-null through helper method before dereferencing: **false positive**
    - Includes `getProperty`, `hasProperty`, `Objects.nonNull`, and `Objects.requireNonNull`
    - Should use `@EnsuresNonNullIf(fields)` or `RequiresNonNull(fields)` for getters

### Fields

- Late- or lazy-init fields null by default: **false positive**
    - Must use `@Initializer` for non-constructor initializer method
    - Null used for "empty" or "pending" state
    - Getter methods may re-initialize null fields
- Non-null fields initialized to null: **true positive**

### Method Signatures

- Overridden method from null-marked or unannotated internal/external/Java API code not annotated as
  `@Nullable`: **false positive**
    - May happen with parameters or return type

### Null Assignments

- Null assignment to variable dereferenced later after check: **false positive**
    - Raised when nullable fields not annotated `@Nullable`
- Null assignment to variable dereferenced later without check: **true positive**

### Null Dereferences

- Dereferencing nullable object from internal/external/Java API code: **true positive**
- Passing nullable value to non-null internal function: **true positive**
- Passing nullable to unannotated internal function: **false positive**

### Null Returns

- Method returns non-null or exception: **false positive**
- Non-null functions annotated `@Nullable`: **false positive**
- Nullable function not annotated `@Nullable`: **false positive**
- Null-marked function returns null: **true positive**
    - Tends to happen in abstract classes or overrides

### Optionals

- `Optional<T>` compared with null: **true positive** (unless intended)
- `Optional<T>` returned as null: **true positive** (unless intended)