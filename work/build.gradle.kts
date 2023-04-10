plugins {
    id("library.common")
    id("hilt.common")
}

android {
    namespace = "com.feelsokman.work"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":design"))

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(libs.kotlinx.coroutines.android)
    kapt(libs.hilt.ext.compiler) // enables injecting workers

    testImplementation(project(":testing"))

    androidTestImplementation(project(":testing"))
    androidTestImplementation(libs.androidx.work.testing)
}
