package ru.hokan.flight_animation_project.data.providers.yansen.hotellook.serialization

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import ru.hokan.flight_animation_project.data.dto.City

class CustomResponseBodyConverter(val gson: Gson, val adapter: CityTypeAdapter) : Converter<ResponseBody, List<City>> {

    override fun convert(value: ResponseBody?): List<City> {
        val jsonReader = gson.newJsonReader(value!!.charStream())
        value.use { _ ->
            return adapter.read(jsonReader)
        }
    }
}