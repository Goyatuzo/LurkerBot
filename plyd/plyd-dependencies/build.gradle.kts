plugins {
    id("com.plyd")
}

dependencies {
    implementation(project(":plyd:plyd-core"))
    implementation(project(":plyd:plyd-mongodb"))
    implementation("org.litote.kmongo:kmongo:4.5.1")
}