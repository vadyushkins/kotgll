plugins {
  kotlin("jvm") version "1.8.0"
  application
  `maven-publish`
}

group = "me.vadyushkins"

version = "1.0.0"

repositories { mavenCentral() }

dependencies {
  testImplementation(kotlin("test"))
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(11) }

application { mainClass.set("org.kotgll.MainKt") }

tasks.withType<Jar> {
  dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources"))
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
  val sourcesMain = sourceSets.main.get()
  val contents =
      configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } +
          sourcesMain.output
  from(contents)
}

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/vadyushkins/kotgll")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
}
