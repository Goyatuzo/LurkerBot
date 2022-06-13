plugins {
    id("com.plyd")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd-core"))
    implementation("org.litote.kmongo:kmongo:4.5.1")
}
