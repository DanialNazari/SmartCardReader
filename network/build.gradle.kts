import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.danial.smartcardreader.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField ("String", "BASE_URL", "\"https://api.ocr.space\"")

        val  properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField ("String", "OCR_API_KEY", "\"${properties.getProperty("OCR_API_KEY")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.ktx)
    //testImplementation(libs.junit)
    //androidTestImplementation(libs.androidx.junit)

    // network
    implementation (libs.logging.interceptor)
    implementation (libs.converter.gson)
    implementation (libs.retrofit.mock)

    // debugging
    implementation (libs.timber)

    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)

}