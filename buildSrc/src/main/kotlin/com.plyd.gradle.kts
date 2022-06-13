plugins { id("org.jetbrains.kotlin.jvm") }

group = "me.yuto"
version = "1.1-SNAPSHOT"

repositories { mavenCentral() }

val log4jVersion = "2.17.2"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    implementation("io.github.microutils:kotlin-logging:1.12.5")
}

tasks.test { useJUnit() }

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() { kotlinOptions.jvmTarget = "11" }
