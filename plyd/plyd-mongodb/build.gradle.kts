plugins {
    id("com.plyd")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plyd:plyd-core"))
    implementation(dependency.kmongo)
}
