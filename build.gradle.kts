plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.vadyushkins"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}