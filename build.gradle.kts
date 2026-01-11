import java.io.FileInputStream
import java.util.Properties

plugins {
    java
    groovy
    signing
    checkstyle
    distribution
    alias(libs.plugins.spotless)
    alias(libs.plugins.omegat)
    alias(libs.plugins.git.version) apply false
}

val dotgit = project.file(".git")
if (dotgit.exists()) {
    apply(plugin = libs.plugins.git.version.get().pluginId)
    val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
    val details = versionDetails()
    val baseVersion = details.lastTag.substring(1)
    version = when {
        details.isCleanTag -> baseVersion
        else -> baseVersion + "-" + details.commitDistance + "-" + details.gitHash + "-SNAPSHOT"
    }
} else {
    val gitArchival = project.file(".git-archival.properties")
    val props = Properties()
    props.load(FileInputStream(gitArchival))
    val versionDescribe = props.getProperty("describe")
    val regex = "^v\\d+\\.\\d+\\.\\d+$".toRegex()
    version = when {
        regex.matches(versionDescribe) -> versionDescribe.substring(1)
        else -> versionDescribe.substring(1) + "-SNAPSHOT"
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.13"
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
    packIntoJar(libs.commons.lang3)

    implementation(libs.bundles.jackson)
    testImplementation(libs.groovy.all)
    testImplementation(libs.bundles.junit)
    testImplementation(libs.commons.io)
    testImplementation(libs.wiremock)
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
