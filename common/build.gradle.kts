plugins {
    id("library.common")
    id("hilt.common")
}

android {
    namespace = "com.feelsokman.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
