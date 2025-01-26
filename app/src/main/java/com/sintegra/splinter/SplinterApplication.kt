package com.sintegra.splinter

import android.app.Application
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.data.repository.AudioRepositoryImpl
import com.sintegra.splinter.data.service.AudioSource
import com.sintegra.splinter.data.service.AudioSourceImpl
import com.sintegra.splinter.data.service.NativeAudioBridge
import com.sintegra.splinter.ui.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

val splinterModule = module {
    single<AudioSource> { AudioSourceImpl() }
    single<AudioRepository> { AudioRepositoryImpl(get()) }

    viewModelOf(::MainViewModel)
}

class SplinterApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Load the library containing the native code including the JNI functions.
        System.loadLibrary("splintertest")

        startKoin {
            androidLogger()

            androidContext(this@SplinterApplication)

            modules(splinterModule)
        }

        NativeAudioBridge.initializeBridge()
    }
}