plugins {
    id("library.common")
    id("hilt.common")
}

android {
    namespace = "com.feelsokman.common.test"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":testing"))

    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.lifecycle.livedata.ktx)
}
