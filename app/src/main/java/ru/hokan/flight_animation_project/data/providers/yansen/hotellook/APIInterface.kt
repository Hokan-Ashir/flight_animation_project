package ru.hokan.flight_animation_project.data.providers.yansen.hotellook

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.hokan.flight_animation_project.data.dto.City

interface APIInterface {

    @GET("/autocomplete")
    fun getListOfCities(@Query("term") term: String, @Query("lang") language: String): Call<List<City>>
}