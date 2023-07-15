plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.dateTime)
    implementation(libs.kotlinx.serialization.json)
    api(libs.michaelBullResult)
    implementation(libs.kotlinLogging)

    testImplementation(libs.mockK)
    testImplementation(libs.googleTruth)
}