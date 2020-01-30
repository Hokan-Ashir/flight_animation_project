package ru.hokan.flight_animation_project.ui.main.flight.path.selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hokan.flight_animation_project.data.dto.City

class FlightPathSelectionViewModel : ViewModel() {
    val arrivalCity = MutableLiveData<City>()
    val departureCity = MutableLiveData<City>()

}
