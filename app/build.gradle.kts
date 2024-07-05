plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.ksp)
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
//    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
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
  implementation(libs.androidx.material)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.material.icons.extended)
  implementation(libs.androidx.datastore.preferences)
  // https://github.com/Kotlin/kotlinx-datetime
  // coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.0.4'
  implementation(libs.kotlinx.datetime)
  implementation(libs.accompanist.permissions)
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}

//dependencies {
//  val lifecycleVersion = "2.8.3"
//  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
//  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
//  implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
//}