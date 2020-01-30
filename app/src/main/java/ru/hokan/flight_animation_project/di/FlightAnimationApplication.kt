package ru.hokan.flight_animation_project.di

import android.app.Application

class FlightAnimationApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}