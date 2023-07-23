plugins {
    alias(libs.plugins.kotlin)
    application
}

val ktorVersion = "2.1.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd:plyd-dependencies"))
    implementation(project(":plyd:plyd-core"))

    implementation(libs.ktor.server.jvm.core)
    implementation(libs.ktor.server.jvm.hostCommon)
    implementation(libs.ktor.server.jvm.statusPages)
    implementation(libs.ktor.server.jvm.auth)
    implementation(libs.ktor.server.jvm.sessions)
    implementation(libs.ktor.server.jvm.pebble)
    implementation(libs.ktor.server.jvm.contentNegotiation)
    implementation(libs.ktor.server.jvm.netty)
    implementation(libs.ktor.serialization.jvm.kotlinx.json)

    testImplementation(libs.ktor.server.jvm.tests)
}

val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName = "lurkerbot-web-fat"
    manifest {
        attributes["Main-Class"] = "com.lurkerbotweb.ApplicationKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}
