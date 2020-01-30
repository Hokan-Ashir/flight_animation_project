package ru.hokan.flight_animation_project.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.hokan.flight_animation_project.data.providers.yansen.hotellook.DataProviderModule
import ru.hokan.flight_animation_project.data.providers.yansen.hotellook.YansenHotelLookModule
import ru.hokan.flight_animation_project.ui.main.city.selection.CitySelectionFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [YansenHotelLookModule::class, DataProviderModule::class])
abstract class ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    abstract fun inject(citySelectionFragment: CitySelectionFragment)
}