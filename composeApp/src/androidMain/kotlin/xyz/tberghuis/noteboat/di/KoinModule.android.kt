package xyz.tberghuis.noteboat.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import xyz.tberghuis.noteboat.tmp.tmp03.TmpSpeechControllerFactory
import org.koin.android.ext.koin.androidApplication
import xyz.tberghuis.noteboat.data.AppDatabaseFactory
import xyz.tberghuis.noteboat.data.DataStorePreferencesFactory
import xyz.tberghuis.noteboat.data.PreferencesRepository

actual val platformModule = module {
  singleOf(::TmpSpeechControllerFactory)

  single<AppDatabaseFactory> { AppDatabaseFactory(androidApplication()) }
   single<DataStorePreferencesFactory> { DataStorePreferencesFactory(androidApplication()) }
  

}