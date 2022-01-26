plugins {
  `java-library`
  `maven-publish`
  kotlin("jvm") version "1.6.10"
  id("org.jetbrains.dokka") version "1.6.10"
  signing
}

group = "io.foxcapades.lib"
version = "1.0.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(kotlin("stdlib-jdk7"))
  implementation(kotlin("stdlib-jdk8"))

  implementation("io.foxcapades.lib:json4k:1.2.0")
  implementation("io.foxcapades.lib:json4k-jackson:1.2.0")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
  testImplementation(kotlin("test"))
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
  }
}

java {
  toolchain {
    this.languageVersion.set(JavaLanguageVersion.of(17))
  }

  withJavadocJar()
  withSourcesJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>()
  .configureEach {
    kotlinOptions {
      jvmTarget = "17"
    }
  }

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
  dokkaSourceSets.configureEach {
    includeNonPublic.set(false)
    jdkVersion.set(8)
  }
}

publishing {
  repositories {
    maven {
      name = "nexus"
      url  = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

      credentials {
        username = project.findProperty("nexus.user") as String?
        password = project.findProperty("nexus.pass") as String?
      }
    }
  }

  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set("Json Validation Tools")
        description.set("Kotlin mixins to perform manual validation of JSON contents using Json4k.")
        url.set("https://github.com/Foxcapades/lib-kotlin-json-validation-tools")
        developers {
          developer {
            id.set("epharper")
            name.set("Elizabeth Paige Harper")
            email.set("foxcapades.io@gmail.com")
            url.set("https://github.com/foxcapades")
            organization.set("Foxcapades IO")
          }
        }
        licenses {
          license {
            name.set("MIT")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/Foxcapades/lib-kotlin-json-validation-tools.git")
          developerConnection.set("scm:git:ssh://github.com/Foxcapades/lib-kotlin-json-validation-tools.git")
          url.set("https://github.com/Foxcapades/lib-kotlin-json-validation-tools")
        }
      }
    }
  }
}

signing {
  useGpgCmd()

  sign(configurations.archives.get())
  sign(publishing.publications["maven"])
}