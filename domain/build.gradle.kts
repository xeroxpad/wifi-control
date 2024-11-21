plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.koin.core)
    implementation(platform(libs.koin.bom))
    implementation(libs.kotlinx.coroutines)
    implementation(libs.androidnetworktools)
    implementation (libs.ktor.client.core)
    implementation (libs.ktor.client.cio)
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
//    implementation (libs.android.sdk.core)
//    implementation (libs.android.sdk.api)
    testImplementation(libs.junit)
}