package xyz.tberghuis.noteboat.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import xyz.tberghuis.noteboat.controller.SpeechControllerFactory
import xyz.tberghuis.noteboat.data.AppDatabaseFactory

actual val platformModule = module {

  singleOf(::SpeechControllerFactory)
  single<AppDatabaseFactory> { AppDatabaseFactory() }
}