plugins {
    id("java")
    id("java-library")
}

group = "github.xCykrix"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("./hytale-api/Server/HytaleServer.jar"))
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
