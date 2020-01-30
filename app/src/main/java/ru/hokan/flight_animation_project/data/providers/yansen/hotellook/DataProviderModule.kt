package ru.hokan.flight_animation_project.data.providers.yansen.hotellook

import dagger.Binds
import dagger.Module
import ru.hokan.flight_animation_project.data.providers.DataProvider

@Module
abstract class DataProviderModule {
    @Binds
    abstract fun provideDataProvider(dataProviderImpl: DataProviderImpl) : DataProvider
}