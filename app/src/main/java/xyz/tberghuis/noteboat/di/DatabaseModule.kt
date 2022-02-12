package xyz.tberghuis.noteboat.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.LegacyDatabase
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

  @Provides
  fun provideNoteDao(database: AppDatabase): NoteDao {
    return database.noteDao()
  }

  @Provides
  fun provideOptionDao(database: AppDatabase): OptionDao {
    return database.optionDao()
  }

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
    return Room.databaseBuilder(
      appContext,
      AppDatabase::class.java,
      "noteboatv2.db"
    )
      .createFromAsset("noteboatv2.db")
      .build()
  }
}