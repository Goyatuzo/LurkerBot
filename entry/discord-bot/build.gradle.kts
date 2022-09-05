plugins {
    id("com.plyd")
}

dependencies {
    implementation(project(":plyd:plyd-dependencies"))
    implementation(project(":plyd:plyd-core"))
    implementation("dev.kord:kord-core:0.8.0-M14")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "com.lurkerbot.LurkerBotKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}
