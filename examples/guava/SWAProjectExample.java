package com.google.common.base;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static com.google.common.base.Optional.fromNullable;

/**
 * False positive example for {@link Optional#fromJavaUtil} and {@link Optional#toJavaUtil}.
 * Place inside guava/src/com/google/common/base.
 */
@NullMarked
public class SWAProjectExample {

    /*
     * Unannotated Versions
     * Warnings:
     * - Comparing optional with null
     * - Return null for optional
     */

    public static <T> Optional<T> fromJavaUtil(
            java.util.Optional<T> javaUtilOptional) {
        return (javaUtilOptional == null) ? null : fromNullable(javaUtilOptional.orElse(null));
    }

    public static <T> java.util.Optional<T> toJavaUtil(
            Optional<T> googleOptional) {
        return googleOptional == null ? null : googleOptional.toJavaUtil();
    }

    /*
     * Annotated Versions
     * No nullability warnings
     * Nothing to rewrite here; just restore the annotations
     */

    @SuppressWarnings("NullableOptional")
    public static <T> @Nullable Optional<T> fromJavaUtilFixed(
            java.util.@Nullable Optional<T> javaUtilOptional) {
        return (javaUtilOptional == null) ? null : fromNullable(javaUtilOptional.orElse(null));
    }

    @SuppressWarnings("NullableOptional")
    public static <T> java.util.@Nullable Optional<T> toJavaUtilFixed(
            @Nullable Optional<T> googleOptional) {
        return googleOptional == null ? null : googleOptional.toJavaUtil();
    }


}
