plugins {
    id("java")
}

group = "pub.gdt.project"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    google()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.dropwizard.metrics:metrics-healthchecks:4.2.19")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("de.rtner:PBKDF2:1.1.4")
}

tasks.test {
}