plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    greet
    todo
    packaging
}

android {
    compileSdk = Sdk.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = Sdk.MIN_SDK_VERSION
        targetSdk = Sdk.TARGET_SDK_VERSION

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "serverUrl", "\"https://jsonplaceholder.typicode.com/\"")
    }

    signingConfigs {
        create("release") {
            /*keyAlias keystoreProperties['releaseKeyAlias']
              keyPassword keystoreProperties['releaseKeyPassword']
              storeFile rootProject.file("keystore.jks")
              storePassword keystoreProperties['releaseStorePassword']*/
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
        }

        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    flavorDimensions("style", "monetize")
    productFlavors {
        create("templateStyle") {
            dimension = "style"
            applicationIdSuffix = ".templateStyle"
            versionNameSuffix = "-templateStyle"
            resValue("string", "app_name", "Android Template")
            versionCode = 1
            versionName = "0.1"
        }

        create("free") {
            dimension = "monetize"
            applicationIdSuffix = ".free"
            versionNameSuffix = "-free"
        }

        create("premium") {
            dimension = "monetize"
            applicationIdSuffix = ".premium"
            versionNameSuffix = "-premium"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.kotlinCompilerExtensionVersion
    }

    lint {
        isWarningsAsErrors = true
        isAbortOnError = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Compose.ui)
    // Tooling support (Previews, etc.)
    implementation(Compose.tooling)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(Compose.foundation)
    // Material Design
    implementation(Compose.material)
    // Material design icons
    implementation(Compose.icons)
    implementation(Compose.iconsExtended)
    // Integration with observables
    implementation(Compose.livedata)
    implementation(Compose.rxJava)
    // UI Tests
    implementation(Compose.uiTest)
    implementation(Compose.activity)

    implementation(Support.appCompat)
    implementation(Support.material)
    implementation(Support.constraintLayout)
    implementation(Support.recyclerview)
    implementation(Support.annotations)

    implementation(Ktx.fragment)
    implementation(Ktx.core)
    implementation(Ktx.collections)

    implementation(Lifecycle.liveData)
    implementation(Lifecycle.savedState)
    implementation(Lifecycle.viewModel)

    implementation(Navigation.navigationFragments)
    implementation(Navigation.navigationUI)

    implementation(WorkManager.workManager)

    implementation(RxJava.rxJava2)
    implementation(RxJava.rxAndroid)

    implementation(Permissions.rx)

    implementation(Libs.rxBindingMaterial)
    implementation(Libs.gson)
    implementation(Libs.otto)
    implementation(Libs.timberLogger)

    implementation(Firebase.analytics)
    implementation(Firebase.crashlytics)

    implementation(Dagger.dagger)
    kapt(Dagger.compiler)
    implementation(Dagger.android)
    kapt(Dagger.processor)

    implementation(Libs.kotlinpref)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.loggingInterceptor)
    implementation(Retrofit.retrofit) {
        exclude("com.squareup.okhttp3", "okhttp")
    }
    implementation(Retrofit.gsonConverter) {
        exclude("com.google.code.gson", "gson")
    }
    implementation(Retrofit.rxJava) {
        exclude("io.reactivex.rxjava2", "rxjava")
    }

    debugImplementation(Libs.leakCanary)


    testImplementation(TestingLib.jUnit)
    testImplementation(TestingLib.jUnitKotlin)
    implementation(TestingLib.testCoreX)

    testImplementation(TestingLib.testArchCompX)
    //testImplementation(TestingLib.robolectric)
    testImplementation(TestingLib.mockitoCore)
    testImplementation(TestingLib.mockitoKotlin)
    testImplementation(TestingLib.mockitoInline)
    testImplementation(TestingLib.runner)
    testImplementation(TestingLib.espresso)
    testImplementation(TestingLib.espressoIntents)
    testImplementation(TestingLib.rules)
    testImplementation(TestingLib.truth)
    testImplementation(TestingLib.kluent)
    testImplementation(TestingLib.coroutineTest)

    androidTestImplementation(TestingLib.jUnit)
    androidTestImplementation(TestingLib.jUnitKotlin)
    androidTestImplementation(TestingLib.testCoreX)
    androidTestImplementation(TestingLib.runner)
    androidTestImplementation(TestingLib.espresso)

    debugImplementation(TestingLib.fragmentScenario)

}


// Configure the extension using a DSL block
greeting {
    // Replace defaults here if you want
}

todo {
    // Replace defaults here if you want
    id = 2
}

