package xyz.tberghuis.noteboat.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import xyz.tberghuis.noteboat.tmp.tmp03.TmpKoinVm
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import xyz.tberghuis.noteboat.vm.HomeViewModel
import xyz.tberghuis.noteboat.vm.NewNoteViewModel
import xyz.tberghuis.noteboat.vm.SettingsViewModel

expect val platformModule: Module

val sharedModule = module {
  viewModel<TmpKoinVm> {
    TmpKoinVm(get(), get())
  }
  
  viewModelOf(::HomeViewModel)
  viewModelOf(::SettingsViewModel)
  viewModelOf(::NewNoteViewModel)
  
}


fun initKoin(config: KoinAppDeclaration? = null) {
  startKoin {
    config?.invoke(this)
    modules(platformModule, sharedModule)
  }
}