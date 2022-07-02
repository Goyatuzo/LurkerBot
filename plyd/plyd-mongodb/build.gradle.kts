plugins {
    id("com.plyd")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd:plyd-core"))
    implementation("org.litote.kmongo:kmongo:4.5.1")
}
