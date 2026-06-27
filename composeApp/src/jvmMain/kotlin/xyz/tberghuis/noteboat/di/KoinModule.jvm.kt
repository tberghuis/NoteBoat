package xyz.tberghuis.noteboat.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import xyz.tberghuis.noteboat.controller.SpeechControllerFactory
import xyz.tberghuis.noteboat.data.AppDatabaseFactory
import xyz.tberghuis.noteboat.data.BackupDbFunFactory
import xyz.tberghuis.noteboat.data.DataStorePreferencesFactory
import xyz.tberghuis.noteboat.data.ImportDbFunFactory

actual val platformModule = module {
  singleOf(::SpeechControllerFactory)
  single<AppDatabaseFactory> { AppDatabaseFactory() }
  singleOf(::DataStorePreferencesFactory)
  singleOf(::BackupDbFunFactory)
  singleOf(::ImportDbFunFactory)
}