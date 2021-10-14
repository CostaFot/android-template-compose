buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.7.0")
        classpath("com.google.gms:google-services:4.3.8")
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.google.com/")
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
