private val exposed_version: String by project
private val h2_version: String by project
private val logback_version: String by project
private val ktor_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.example"
version = "1.0.0"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:${ktor_version}")
    implementation("io.ktor:ktor-server-netty:${ktor_version}")

    implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
    implementation("io.ktor:ktor-server-config-yaml:${ktor_version}")

    implementation("io.ktor:ktor-server-default-headers:${ktor_version}")
    implementation("io.ktor:ktor-server-auto-head-response:${ktor_version}")
    implementation("io.ktor:ktor-server-call-logging:${ktor_version}")
    implementation("io.ktor:ktor-server-status-pages:${ktor_version}")
    implementation("io.ktor:ktor-server-auth:${ktor_version}")
    implementation("io.ktor:ktor-server-cors:${ktor_version}")
    implementation("org.jetbrains.exposed:exposed-core:${exposed_version}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposed_version}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposed_version}")
    implementation("com.h2database:h2:${h2_version}")

    implementation("ch.qos.logback:logback-classic:${logback_version}")
}

