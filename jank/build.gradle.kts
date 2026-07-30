plugins {
    id("library.common")
    id("library.compose.common")
    id("hilt.common")
}

android {
    namespace = "com.feelsokman.jank"
}

dependencies {
    api(libs.androidx.metrics)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)

    testImplementation(libs.junit4)
}
