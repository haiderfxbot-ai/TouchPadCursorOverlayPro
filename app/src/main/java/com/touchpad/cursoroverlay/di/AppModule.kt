package com.touchpad.cursoroverlay.di

import android.content.Context
import com.touchpad.cursoroverlay.data.PreferencesManager
import com.touchpad.cursoroverlay.data.SettingsRepository
import com.touchpad.cursoroverlay.util.WebTouchpadBridge
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        preferencesManager: PreferencesManager
    ): SettingsRepository {
        return SettingsRepository(preferencesManager)
    }

    @Provides
    @Singleton
    fun provideWebTouchpadBridge(
        @ApplicationContext context: Context
    ): WebTouchpadBridge {
        return WebTouchpadBridge(context)
    }
}
