plugins {
    id("com.plyd")
}

dependencies {
    implementation(project(":plyd-core"))
    implementation(project(":plyd-mongodb"))
    implementation("dev.kord:kord-core:0.8.0-M14")
    implementation("org.litote.kmongo:kmongo:4.5.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
