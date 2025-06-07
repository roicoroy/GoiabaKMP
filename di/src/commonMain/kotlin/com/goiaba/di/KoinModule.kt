package com.goiaba.di

import com.goiaba.data.services.StrapiAuthImpl
import com.goiaba.data.services.StrapiPostImpl
import com.goiaba.data.services.domain.StrapiAuthRepository
import com.goiaba.data.services.domain.StrapiPostRepository
import com.goiaba.details.DetailsViewModel
import com.goiaba.feature.HomeGraphViewModel
import com.goiaba.shared.util.IntentHandler
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<IntentHandler> { IntentHandler() }
    single<StrapiAuthRepository> { StrapiAuthImpl() }
    single<StrapiPostRepository> { StrapiPostImpl() }
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::DetailsViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}