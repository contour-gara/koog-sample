import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.koog.agents)
    implementation(libs.logback.classic)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.extensions.ktor)
    testImplementation(libs.ktor.server.test.host)
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "org.contourgara.KoogSampleApplicationKt"
}

ktor {
    fatJar {
        archiveFileName.set("${project.name}.jar")
    }
}

tasks.jar {
    archiveFileName.set("${project.name}-plain.jar")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT
        )

        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}
