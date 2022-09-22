plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("de.westnordost:osmapi-overpass:2.0")
    implementation("org.projectlombok:lombok:1.18.24")
    implementation("mil.nga.sf:sf-geojson:3.1.0")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testImplementation("org.mockito:mockito-core:3.+")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}