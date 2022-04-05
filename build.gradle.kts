import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.diffplug.spotless") version "6.1.0"
}

group = "me.yuto"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val log4jVersion = "2.17.1"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("dev.kord:kord-core:0.8.0-M8")
    implementation("org.litote.kmongo:kmongo:4.5.0")
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.14")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<KotlinCompile>().configureEach {
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

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        // by default the target is every '.kt' and '.kts` file in the java sourcesets
        ktfmt().kotlinlangStyle()
    }
    kotlinGradle {
        target("*.gradle.kts") // default target for kotlinGradle
        ktlint() // or ktfmt() or prettier()
    }
}
