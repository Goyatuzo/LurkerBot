

object dependency {
    object version {
        const val log4J = "2.17.2"
    }

    const val googleTruth = "com.google.truth:truth:1.1.3"
    const val mockK = "io.mockk:mockk:1.12.4"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:1.6.0"
    const val kotlinCoRoutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2"
    const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:1.1.16"
    const val log4JCore = "org.apache.logging.log4j:log4j-core:${version.log4J}"
    const val log4JApi = "org.apache.logging.log4j:log4j-api:${version.log4J}"
    const val log4JImpl = "org.apache.logging.log4j:log4j-slf4j-impl:${version.log4J}"
    const val kotlinLogging = "io.github.microutils:kotlin-logging:1.12.5"
    const val kotlinxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"
    const val kotlinxDatetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.3.2"
}
