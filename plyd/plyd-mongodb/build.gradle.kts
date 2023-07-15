plugins {
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd:plyd-core"))
    implementation(libs.kmongo)
    implementation(libs.kotlinLogging)
    implementation(libs.kotlinx.dateTime)
}
