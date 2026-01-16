plugins {
    id("java")
    id("com.gradleup.shadow") version("9.3.1")
}

group = "github.xCykrix"
version = "1.1.0"
val description = "Advanced Hytale Announcement Mod - (Color Support) Titles, Broadcasts, Join/Leave Messages, Messages of the Day, and more! Day 1 Release."

repositories {
    mavenCentral()
    maven("https://repo.entix.eu/releases")
}

dependencies {
    compileOnly(files("./hytale-api/Server/HytaleServer.jar"))
    implementation(files("./hytale-api/tinymessage-2.0.0.jar"))
}

tasks.shadowJar {
    archiveAppendix = "EA"
    archiveClassifier = null;
    relocationPrefix = "github.xCykrix.reloc.${rootProject.name.lowercase()}"
}

// Target Java Build (Java 25)
val targetJavaVersion = 25
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}
