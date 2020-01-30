package ru.hokan.flight_animation_project.data.providers

import ru.hokan.flight_animation_project.data.dto.City

interface DataProvider {
    fun getCities(name : String, processor : (List<City>?) -> Unit)
}