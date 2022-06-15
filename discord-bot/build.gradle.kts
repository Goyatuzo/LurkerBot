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

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes("Main-Class" to "com.lurkerbot.LurkerBotKt")
    }
    from(
        configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) }
    )
    val sourcesMain = sourceSets.main.get()
    from(sourcesMain.output)
}
