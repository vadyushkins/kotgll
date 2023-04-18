plugins {
  kotlin("jvm") version "1.8.0"
  application
}

group = "me.vadyushkins"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  testImplementation(kotlin("test"))
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(11) }

application { mainClass.set("org.kotgll.MainKt") }

tasks {
  val fatJar = register<Jar>("fatJar") {
    dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
    archiveClassifier.set("standalone") // Naming the jar
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get()
      .map { if (it.isDirectory) it else zipTree(it) } +
        sourcesMain.output
    from(contents)
  }
  build {
    dependsOn(fatJar) // Trigger fat jar creation during build
  }
}