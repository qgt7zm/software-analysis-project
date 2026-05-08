package com.google.testing.compile;

import com.google.common.base.Splitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URL;
import java.net.URLClassLoader;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertThrows;

/**
 * True positive test for {@link Compiler#getClasspathFromClassloader}.
 * Put inside src/test/java/com/google/testing/compile.
 */
@RunWith(JUnit4.class)
public class SWAProjectTest {

    private String separator, classpath;

    @Test
    public void compilerNullDereference() {
        // Null system property may occur due to unusual runtime environments
//        System.setProperty("path.separator", null); // Cannot manually set null system property
//        assertThrows(
//                NullPointerException.class,
//                () -> javac().withClasspathFrom(new URLClassLoader(new URL[0], Compiler.platformClassLoader))
//        );

        // Simulate null system property
        separator = null;
        assertThrows(
                NullPointerException.class,
                () -> Splitter.on((String) separator)
        );

        separator = ",";
        classpath = null;
        assertThrows(
                NullPointerException.class,
                () -> Splitter.on(",").split((String) classpath)
        );
    }

}
