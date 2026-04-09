package xyz.tberghuis.noteboat.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import xyz.tberghuis.noteboat.data.AppDatabaseFactory
import xyz.tberghuis.noteboat.tmp.tmp03.SpeechControllerFactory

actual val platformModule = module {

  singleOf(::SpeechControllerFactory)
  single<AppDatabaseFactory> { AppDatabaseFactory() }
}