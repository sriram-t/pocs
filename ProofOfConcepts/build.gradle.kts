plugins {
    id("application")
}

group = "org.db"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.db.identifiers.UUIDIdentifier")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.f4b6a3:uuid-creator:5.3.6")
    implementation("org.postgresql:postgresql:42.7.4")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}