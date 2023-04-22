plugins {
    id("java")
}

group = "eu.cafestube"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.viaversion.com/")
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/eu.cloudnetservice.cloudnet/bom
    compileOnly("eu.cloudnetservice.cloudnet:wrapper-jvm:4.0.0-RC8")
    compileOnly("eu.cloudnetservice.cloudnet:bridge:4.0.0-RC8")

    compileOnly("com.viaversion:viaversion-api:4.6.3-SNAPSHOT")

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.github.Minestom:Minestom:8ad2c7701f")


    annotationProcessor("eu.cloudnetservice.cloudnet:platform-inject-processor:4.0.0-RC8")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.test {
    useJUnitPlatform()
}