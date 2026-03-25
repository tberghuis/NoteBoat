package xyz.tberghuis.noteboat.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import xyz.tberghuis.noteboat.tmp.tmp03.TmpSpeechControllerFactory
import org.koin.android.ext.koin.androidApplication
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.data.createDataStoreAndroid
import xyz.tberghuis.noteboat.database.FruittieDao
import xyz.tberghuis.noteboat.database.TmpAppDatabase

actual val platformModule = module {

//  val roomFactory = RoomFactory(androidApplication())

  singleOf(::TmpSpeechControllerFactory)

  single<RoomFactory> { RoomFactory(androidApplication()) }


  single<TmpAppDatabase> { get<RoomFactory>().createRoomDatabase() }
  single<FruittieDao> { get<TmpAppDatabase>().fruittieDao() }


  single<AppDatabase> { get<RoomFactory>().createAppDatabase() }
  single<NoteDao> { get<AppDatabase>().noteDao() }


  single<DataStore<Preferences>> {
    createDataStoreAndroid(get())
  }
  singleOf(::PreferencesRepository)
}