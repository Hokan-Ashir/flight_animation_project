package ru.hokan.flight_animation_project.data.providers.yansen.hotellook.serialization

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CustomGsonConverterFactory : Converter.Factory() {

    private val gson = Gson()
    private val cityAdapter = CityTypeAdapter()

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *> {
        return CustomResponseBodyConverter(gson, cityAdapter)
    }
}