plugins {
    java
    groovy
    checkstyle
    distribution
    id("org.omegat.gradle") version "1.4.2"
    id("com.palantir.git-version") version "0.12.3"
}

// calculate version string from git tag, hash and commit distance
fun getVersionDetails(): com.palantir.gradle.gitversion.VersionDetails = (extra["versionDetails"] as groovy.lang.Closure<*>)() as com.palantir.gradle.gitversion.VersionDetails
if (getVersionDetails().isCleanTag) {
    version = getVersionDetails().lastTag.substring(1)
} else {
    version = getVersionDetails().lastTag.substring(1) + "-" + getVersionDetails().commitDistance + "-" + getVersionDetails().gitHash + "-SNAPSHOT"
}

omegat {
    version = "5.5.0"
    pluginClass = "tokyo.northside.omegat.textra.OmegatTextraMachineTranslation"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    packIntoJar("oauth.signpost:signpost-core:1.2.1.2")
    packIntoJar("oauth.signpost:signpost-commonshttp4:1.2.1.2")
    packIntoJar("org.apache.httpcomponents:httpclient:4.5.12")
    packIntoJar("com.fasterxml.jackson.core:jackson-core:2.12.0")
    packIntoJar("com.fasterxml.jackson.core:jackson-databind:2.12.0")
    packIntoJar(fileTree("lib") {include("*.jar")})
    packIntoJar("org.slf4j:slf4j-api:1.7.25")
    implementation("commons-io:commons-io:2.5")
    implementation("commons-lang:commons-lang:2.6")
    testImplementation("commons-io:commons-io:2.5")
    testImplementation("commons-lang:commons-lang:2.6")
    testImplementation("org.codehaus.groovy:groovy-all:3.0.1")
    testImplementation("org.testng:testng:6.9.10")
}

tasks.withType(Test::class) {
    useTestNG()
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
