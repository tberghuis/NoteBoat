plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.ksp)
}

android {
  namespace = "xyz.tberghuis.noteboat"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "site.thomasberghuis.noteboat"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    // todo update for release
    versionCode = 30
    versionName = "2.18.0"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}


dependencies {
  debugImplementation(libs.compose.uiTooling)

  implementation(projects.composeApp)




  implementation(libs.compose.runtime)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material3)
  implementation(libs.compose.ui)
  implementation(libs.compose.components.resources)
  implementation(libs.compose.uiToolingPreview)
  implementation(libs.androidx.lifecycle.viewmodelCompose)
  implementation(libs.androidx.lifecycle.runtimeCompose)

  implementation(libs.compose.uiToolingPreview)
  implementation(libs.androidx.activity.compose)


  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room.ktx)


  implementation(platform(libs.androidx.compose.bom))

  implementation(libs.androidx.material.icons.extended)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.accompanist.permissions)


  implementation(platform(libs.koin.bom))
  implementation(libs.koin.core)

  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)

}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}
