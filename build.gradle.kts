plugins {
    id("com.plyd")
    id("com.diffplug.spotless") version "6.1.0"
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        target("**/src/**/*.kt", "**/src/**/*.kts")
        // by default the target is every '.kt' and '.kts` file in the java sourcesets
        ktfmt().kotlinlangStyle()
    }
    kotlinGradle {
        target("*.gradle.kts") // default target for kotlinGradle
        ktlint() // or ktfmt() or prettier()
    }
}
