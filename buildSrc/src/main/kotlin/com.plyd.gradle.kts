plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "me.yuto"
version = "0.0.1"

repositories { mavenCentral() }

dependencies {
    testImplementation(dependency.kotlinTest)
    testImplementation(dependency.googleTruth)
    testImplementation(dependency.mockK)
    testImplementation(dependency.kotlinCoRoutinesCore)
    implementation(dependency.kotlinResult)
    implementation(dependency.log4JCore)
    implementation(dependency.log4JApi)
    implementation(dependency.log4JImpl)
    implementation(dependency.kotlinLogging)
    implementation(dependency.kotlinxSerializationJson)
    implementation(dependency.kotlinxDatetime)
}

tasks.test { useJUnit() }

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() { kotlinOptions.jvmTarget = "11" }
