package xyz.tberghuis.noteboat.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import xyz.tberghuis.noteboat.data.AppDatabase
import xyz.tberghuis.noteboat.data.AppDatabaseFactory
import xyz.tberghuis.noteboat.data.DataStorePreferencesFactory
import xyz.tberghuis.noteboat.data.NoteDao
import xyz.tberghuis.noteboat.data.OptionDao
import xyz.tberghuis.noteboat.data.PreferencesRepository
import xyz.tberghuis.noteboat.vm.EditNoteViewModel
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.vm.NewNoteViewModel
import xyz.tberghuis.noteboat.vm.TrashScreenVm

expect val platformModule: Module

val sharedModule = module {
  single<AppDatabase> { get<AppDatabaseFactory>().create() }
  single<NoteDao> { get<AppDatabase>().noteDao() }
  single<OptionDao> { get<AppDatabase>().optionDao() }

  single<DataStore<Preferences>> {
    get<DataStorePreferencesFactory>().create()
  }
  singleOf(::PreferencesRepository)

  viewModelOf(::HomeViewModel)
  viewModelOf(::NewNoteViewModel)
  viewModelOf(::EditNoteViewModel)
  viewModelOf(::TrashScreenVm)
}

fun initKoin(config: KoinAppDeclaration? = null) {
  startKoin {
    config?.invoke(this)
    modules(platformModule, sharedModule)
  }
}