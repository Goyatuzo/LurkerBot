plugins {
    id("com.plyd")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(dependency.kotlinxDatetime)
}