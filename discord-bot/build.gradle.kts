plugins {
    id("com.plyd")
}

dependencies {
    implementation(project(":plyd-core"))
    implementation(project(":plyd-mongodb"))
    implementation("dev.kord:kord-core:0.8.0-M14")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
