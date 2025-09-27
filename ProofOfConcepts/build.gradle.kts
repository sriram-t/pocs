plugins {
    id("application")
    id("java")
}

group = "org.db"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("org.db.identifiers.UUIDIdentifier")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.f4b6a3:uuid-creator:5.3.6")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("io.github.resilience4j:resilience4j-retry:2.1.0")
    implementation("io.github.resilience4j:resilience4j-core:2.1.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}