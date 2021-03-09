plugins {
    java
    groovy
    checkstyle
    distribution
    id("org.omegat.gradle") version "1.4.2"
    id("com.sarhanm.versioner") version "4.0.2"
}

versioner{
    snapshot=false
    omitBranchMetadata=true
    disableHotfixVersioning=true
}

omegat {
    version = "5.4.1"
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

tasks.distTar {
  compression = Compression.BZIP2
}

distributions {
    main {
        contents {
            from(tasks["jar"], "README.md", "CHANGELOG.md", "COPYING", "DEVELOP.md")
        }
    }
}
