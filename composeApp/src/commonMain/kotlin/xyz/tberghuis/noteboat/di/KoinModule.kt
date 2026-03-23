package xyz.tberghuis.noteboat.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import xyz.tberghuis.noteboat.tmp.tmp03.TmpKoinVm
import org.koin.core.module.Module

expect val platformModule: Module

val sharedModule = module {
  viewModelOf(::TmpKoinVm)
}


fun initKoin(config: KoinAppDeclaration? = null) {
  startKoin {
    config?.invoke(this)
    modules(platformModule, sharedModule)
  }
}