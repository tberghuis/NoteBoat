import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.composeHotReload)
  alias(libs.plugins.androidKmpLibrary)

  alias(libs.plugins.ksp)
  alias(libs.plugins.room)


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

//      implementation(project.dependencies.platform(libs.koin.bom))
//      implementation(libs.koin.core)
//      implementation(libs.koin.android)
//      implementation(libs.koin.androidx.compose)

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

//      implementation(platform(libs.koin.bom))
      // https://github.com/InsertKoinIO/koin/issues/1721
//      api(project.dependencies.platform(libs.koin.bom))
//      api(libs.koin.core)

      api(project.dependencies.platform(libs.koin.bom))
      implementation(libs.koin.core)
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)


      implementation(libs.androidx.room.runtime)

//  ksp(libs.androidx.room.compiler)
      // ktx has android specific stuff
//      api(libs.androidx.room.ktx)

      implementation(libs.sqlite.bundled)

      implementation(libs.androidx.datastore.preferences)

      implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

      // todo migrate to navigation 3
      implementation(libs.androidx.navigation.compose)

      implementation(libs.compose.navigationevent)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
    jvmMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutinesSwing)
    }
  }
}


dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
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

//ksp {
//  arg("room.schemaLocation", "$projectDir/schemas")
//}

