plugins {
    id("com.plyd")
    id("org.jetbrains.kotlin.plugin.serialization") version dependency.version.kotlin
}

dependencies {
    implementation(dependency.kotlinxDatetime)
}