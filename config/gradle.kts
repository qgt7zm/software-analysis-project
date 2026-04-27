plugins {
    id("net.ltgt.errorprone") version "5.1.0"
}

tasks.withType<JavaCompile> {
    options.errorprone  {
        disableAllChecks = true // Disable other checks
        option("NullAway:OnlyNullMarked", "true") // Only check null-marked code
        // option("NullAway:AnnotatedPackages", "package1,package2") // Only check given packages
        error("NullAway") // Use warning level
        option("NullAway:JSpecifyMode", "true") // JSpecify support
        // option("NullAway:JSpecifyMode", "true") // Assert statements are ignored by default

        // Disable NullAway on test code
        if (name.toLowerCase().contains("test")) {
            options.errorprone {
                disable("NullAway")
            }
        }
    }
}

dependencies {
    implementation("org.jspecify:jspecify:1.0.0") // JSpecify should already be provided
    errorprone("com.google.errorprone:error_prone_core:2.48.0")
    errorprone("com.uber.nullaway:nullaway:0.31.1")
}