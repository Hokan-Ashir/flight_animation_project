package ru.hokan.flight_animation_project.data.providers.yansen.hotellook

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.hokan.flight_animation_project.data.dto.City
import ru.hokan.flight_animation_project.data.providers.DataProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataProviderImpl @Inject constructor(val apiInterface: APIInterface) : DataProvider {

    override fun getCities(name: String, processor: (List<City>?) -> Unit) {
        apiInterface.getListOfCities(name, DEFAULT_LANGUAGE).enqueue(object : Callback<List<City>> {
            override fun onFailure(call: Call<List<City>>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<List<City>>?, response: Response<List<City>>?) {
                processor.invoke(response?.body())
            }

        })
    }

    companion object {
        // TODO place for i18n
        const val DEFAULT_LANGUAGE = "en"
    }
}