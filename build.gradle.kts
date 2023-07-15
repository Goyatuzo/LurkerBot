plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spotless)
}

repositories {
    mavenCentral()
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        target("**/src/**/*.kt")
        // by default the target is every '.kt' and '.kts` file in the java sourcesets
        ktfmt().kotlinlangStyle()
    }
    kotlinGradle {
        target("**/src/**/*.kts") // default target for kotlinGradle
        ktlint() // or ktfmt() or prettier()
    }
}
