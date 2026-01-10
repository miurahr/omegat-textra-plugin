import org.gradle.crypto.checksum.Checksum

plugins {
    java
    groovy
    signing
    checkstyle
    distribution
    id("org.gradle.crypto.checksum") version "1.4.0"
    id("com.diffplug.spotless") version "6.12.0"
    id("org.omegat.gradle") version "1.5.9"
    id("com.palantir.git-version") version "0.13.0"
}

// calculate version string from git tag, hash and commit distance
fun getVersionDetails(): com.palantir.gradle.gitversion.VersionDetails = (extra["versionDetails"] as groovy.lang.Closure<*>)() as com.palantir.gradle.gitversion.VersionDetails
if (getVersionDetails().isCleanTag) {
    version = getVersionDetails().lastTag.substring(1)
} else {
    version = getVersionDetails().lastTag.substring(1) + "-" + getVersionDetails().commitDistance + "-" + getVersionDetails().gitHash + "-SNAPSHOT"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

omegat {
    version = "6.0.0"
    pluginClass = "tokyo.northside.omegat.textra.OmegatTextraMachineTranslation"
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.20.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1.2")
    packIntoJar("commons-lang:commons-lang:2.6")
    testImplementation("org.codehaus.groovy:groovy-all:3.0.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.35.0")

}

repositories {
    mavenCentral()
}

tasks.withType<Checkstyle>().configureEach {
    isIgnoreFailures = true
    exclude("**/dialog/TextraOptionDialog.java")
}

distributions {
    main {
        contents {
            from(tasks["jar"], "README.md", "CHANGELOG.md", "COPYING", "DEVELOP.md")
        }
    }
}

val signKey = listOf("signingKey", "signing.keyId", "signing.gnupg.keyName").find {project.hasProperty(it)}
tasks.withType<Sign> {
    onlyIf { signKey != null }
}

signing {
    when (signKey) {
        "signingKey" -> {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
        }

        "signing.keyId" -> {/* do nothing */
        }

        "signing.gnupg.keyName" -> {
            useGpgCmd()
        }
    }
    sign(tasks.distZip.get())
    sign(tasks.jar.get())
}

val jar by tasks.getting(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

spotless {
    java {
        target(listOf("src/*/java/**/*.java"))
        removeUnusedImports()
        palantirJavaFormat()
        importOrder("org.omegat", "tokyo.northside.omegat.textra", "java", "javax", "", "\\#")
    }
}

tasks.register<Checksum>("createChecksums") {
    dependsOn(tasks.distZip)
    inputFiles.setFrom(listOf(tasks.jar.get(), tasks.distZip.get()))
    outputDirectory.set(layout.buildDirectory.dir("distributions"))
    checksumAlgorithm.set(Checksum.Algorithm.SHA512)
    appendFileNameToChecksum.set(true)
}
