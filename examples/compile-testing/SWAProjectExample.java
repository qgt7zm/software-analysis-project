package com.google.testing.compile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import java.util.stream.Collector;

import static com.google.common.base.Predicates.notNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * False positive example for {@link CompilationSubject.DiagnosticOnLine#findLineContainingSubstring}.
 * Put inside src/main/java/com/google/testing/compile.
 */
@NullMarked
public class SWAProjectExample {

    private final CompilationSubject.LinesInFile linesInFile;

    private SWAProjectExample(JavaFileObject file) {
        this.linesInFile = new CompilationSubject.LinesInFile(file);
    }

    // Original
    private void findLineContainingSubstring(String expectedLineSubstring) {
        Streams.<String, @Nullable Long>mapWithIndex(
                        linesInFile.linesInFile().stream(),
                        (line, index) -> line.contains(expectedLineSubstring) ? index : null)
                .filter(notNull())
                .map(index -> index + 1) // Warning: Long index may be null
                .collect(toImmutableSet());
    }

    // Fixed
    private void findLineContainingSubstringFixed(String expectedLineSubstring) {
        Streams.<String, Long>mapWithIndex(
                        linesInFile.linesInFile().stream(),
                        (line, index) -> line.contains(expectedLineSubstring) ? index : -1)
                .filter(index -> index != -1) // No warning
                .map(index -> index + 1)
                .collect(toImmutableSet());
    }

    private static <T> Collector<T, ?, ImmutableSet<T>> toImmutableSet() {
        return collectingAndThen(toList(), ImmutableSet::copyOf);
    }

}
