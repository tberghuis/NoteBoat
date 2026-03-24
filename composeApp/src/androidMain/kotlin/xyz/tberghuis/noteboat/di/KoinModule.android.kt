package xyz.tberghuis.noteboat.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import xyz.tberghuis.noteboat.tmp.tmp03.TmpSpeechControllerFactory
import org.koin.android.ext.koin.androidApplication
import xyz.tberghuis.noteboat.database.FruittieDao
import xyz.tberghuis.noteboat.database.TmpAppDatabase

actual val platformModule = module {

//  val roomFactory = RoomFactory(androidApplication())

  singleOf(::TmpSpeechControllerFactory)

//  single<RoomFactory> { RoomFactory(androidApplication()) }


  single<TmpAppDatabase> { RoomFactory(androidApplication()).createRoomDatabase() }
  single<FruittieDao> { get<TmpAppDatabase>().fruittieDao() }

}