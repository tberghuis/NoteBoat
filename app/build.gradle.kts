plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.ksp)
  alias(libs.plugins.compose.compiler)
}

android {
  compileSdk = 36

  defaultConfig {
    // NOTE does not match package of source code
    applicationId = "site.thomasberghuis.noteboat"
    minSdk = 27
    targetSdk = 35
    versionCode = 25
    versionName = "2.13.0"

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
  }

  // allow for developing with a different app id, in which
  // compose previews will still work
  // (could not get compose previews to work with a new build type)
  flavorDimensions += "version"
  productFlavors {
    create("default") {
      dimension = "version"
    }
    // change the launcher icon, app name and app id
    create("dev") {
      dimension = "version"
      applicationIdSuffix = ".dev"
      versionNameSuffix = "-dev"
    }
  }

  compileOptions {
    // was meant to fix kotlinx-datetime, no class found error java...Instant
    // didn't work so switching min sdk to 26
    // coreLibraryDesugaringEnabled true

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  composeCompiler {
//    enableStrongSkippingMode = true
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  namespace = "xyz.tberghuis.noteboat"
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)

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

  // the @aar includes libjnidispatch.so
  // dev-whisper whisper-rs jnilib
//  implementation("net.java.dev.jna:jna:5.17.0@aar")
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}

