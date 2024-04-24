plugins {
    `kotlin-dsl`
}

group = "com.feelsokayman.template.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("applicationComposeCommonPlugin") {
            id = "application.compose.common"
            implementationClass = "ApplicationComposeCommonPlugin"
        }
        register("applicationCommon") {
            id = "application.common"
            implementationClass = "ApplicationCommonPlugin"
        }
        register("libraryComposeCommon") {
            id = "library.compose.common"
            implementationClass = "LibraryComposeCommonPlugin"
        }
        register("libraryCommon") {
            id = "library.common"
            implementationClass = "LibraryCommonPlugin"
        }
        register("hiltCommon") {
            id = "hilt.common"
            implementationClass = "HiltCommonPlugin"
        }
    }
}
