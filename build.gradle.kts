plugins {
    java
}

group = "me.camdenorrb.timekeeper"
version = "1.0.0"

repositories {

    mavenCentral()

    maven("https://jitpack.io") {
        name = "Jitpack"
    }
}

dependencies {
    compile("com.zaxxer:HikariCP:3.4.1")
    implementation("com.github.camdenorrb:JCommons:1.0.2")
    implementation("org.slf4j:slf4j-simple:1.7.28")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}