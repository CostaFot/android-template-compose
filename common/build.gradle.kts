plugins {
    id("library.common")
    id("hilt.common")
}

android {
    lint {
        checkDependencies = true
    }

    namespace = "com.feelsokman.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation("androidx.hilt:hilt-work:1.0.0")
}
