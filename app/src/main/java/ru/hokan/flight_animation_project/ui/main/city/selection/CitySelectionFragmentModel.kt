package ru.hokan.flight_animation_project.ui.main.city.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CitySelectionFragmentModel : ViewModel() {
    val cityType = MutableLiveData<CityType>()
}

enum class CityType {
    ARRIVAL,
    DEPARTURE
}