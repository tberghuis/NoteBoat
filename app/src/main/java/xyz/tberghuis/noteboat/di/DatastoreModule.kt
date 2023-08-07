package xyz.tberghuis.noteboat.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.data.dataStore
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastoreModule {
  @Provides
  @Singleton
  fun providePreferencesRepository(@ApplicationContext appContext: Context): PreferencesRepository {
    return PreferencesRepository(appContext.dataStore)
  }
}