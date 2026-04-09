import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.composeHotReload)
  alias(libs.plugins.androidKmpLibrary)
  alias(libs.plugins.ksp)
  alias(libs.plugins.room)
  alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {

  android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    minSdk = libs.versions.android.minSdk.get().toInt()
    namespace = "xyz.tberghuis.noteboat"
    experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
  }

  jvm()

  sourceSets {
    androidMain.dependencies {
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.androidx.activity.compose)
      implementation(libs.koin.android)
      implementation(libs.koin.androidx.compose)
      implementation(libs.accompanist.permissions)
    }
    commonMain.dependencies {
      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.ui)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.androidx.lifecycle.viewmodelCompose)
      implementation(libs.androidx.lifecycle.runtimeCompose)

      api(project.dependencies.platform(libs.koin.bom))
      implementation(libs.koin.core)
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)

      implementation(libs.androidx.room.runtime)
      implementation(libs.sqlite.bundled)
      implementation(libs.androidx.datastore.preferences)
      implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

      implementation(libs.compose.navigationevent)

//      implementation(libs.androidx.navigation3.ui)
//      implementation(libs.androidx.navigation3.runtime)
//      implementation(libs.androidx.lifecycle.viewmodel.navigation3)
//      implementation(libs.androidx.material3.adaptive.navigation3)
//      implementation(libs.kotlinx.serialization.core)

      api(libs.androidx.nav3.ui)
      implementation(libs.androidx.lifecycle.viewmodel.nav3)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
    jvmMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutinesSwing)
    }
  }
  compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}

dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspJvm", libs.androidx.room.compiler)
}

compose.desktop {
  application {
    mainClass = "xyz.tberghuis.noteboat.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "xyz.tberghuis.noteboat"
      packageVersion = "1.0.0"
    }
  }
}

room {
  schemaDirectory("$projectDir/schemas")
}