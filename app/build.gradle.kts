plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
}

android {
  compileSdk = 34

  defaultConfig {
    // NOTE does not match package of source code
    applicationId = "site.thomasberghuis.noteboat"
    minSdk = 27
    targetSdk = 34
    versionCode = 19
    versionName = "2.8.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    create("debugTmp") {
      initWith(getByName("debug"))
    }

  }
  compileOptions {
    // was meant to fix kotlinx-datetime, no class found error java...Instant
    // didn't work so switching min sdk to 26
    // coreLibraryDesugaringEnabled true

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17


  }
  kotlinOptions {
    jvmTarget = "17"
    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.14"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  namespace = "xyz.tberghuis.noteboat"
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  implementation(libs.androidx.material)



  implementation("androidx.navigation:navigation-compose:2.7.7")
  val roomVersion = "2.6.1"
  implementation("androidx.room:room-runtime:$roomVersion")
  ksp("androidx.room:room-compiler:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  val composeVersion = "1.6.8"
  implementation("androidx.compose.material:material-icons-extended:$composeVersion")
  implementation("androidx.datastore:datastore-preferences:1.1.1")

  // https://github.com/Kotlin/kotlinx-datetime
  // coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.0.4'
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

  implementation("com.google.accompanist:accompanist-permissions:0.32.0")

}


//dependencies {
//  implementation("androidx.core:core-ktx:1.13.1")
//
//
//
//  implementation("androidx.compose.ui:ui:$composeVersion")
//  implementation("androidx.compose.material:material:$composeVersion")
//  implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
//
//  testImplementation("junit:junit:4.13.2")
//  androidTestImplementation("androidx.test.ext:junit:1.2.1")
//  androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
//  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
//  debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
//

//

//

//

//
//  val lifecycleVersion = "2.8.3"
//  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
//  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
//  implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
//

//}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}