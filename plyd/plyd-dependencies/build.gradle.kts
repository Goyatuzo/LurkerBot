plugins {
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd:plyd-core"))
    implementation(project(":plyd:plyd-mongodb"))
    implementation(libs.kmongo)
}