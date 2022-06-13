plugins {
    id("com.plyd")
}

group = "me.yuto"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd-core"))
    implementation("org.litote.kmongo:kmongo:4.5.1")
}
