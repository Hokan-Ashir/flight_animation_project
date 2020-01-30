package ru.hokan.flight_animation_project.data.providers.yansen.hotellook.serialization

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import ru.hokan.flight_animation_project.data.dto.City

class CityTypeAdapter : TypeAdapter<List<City>>() {

    private val mGson = Gson()

    override fun write(out: JsonWriter?, value: List<City>?) {
        mGson.toJson(value, City::class.java, out)
    }

    override fun read(`in`: JsonReader?): List<City> {
        val result = mutableListOf<City>()
        `in`!!.beginObject()
        when (`in`.nextName()) {
            CITIES_ROOT_ELEMENT -> {
                processCityTag(`in`, result)
            }
            // considering ordering of tags we can cut out all other high-order tags
            else -> return result
        }
        return result
    }

    private fun processCityTag(
        `in`: JsonReader,
        result: MutableList<City>
    ) {
        `in`.beginArray()
        while (`in`.hasNext()) {
            attachExtractedCityElement(`in`, result)
        }
        `in`.endArray()
    }

    private fun attachExtractedCityElement(
        `in`: JsonReader,
        result: MutableList<City>
    ) {
        `in`.beginObject()
        val city = City()
        val iatas = mutableListOf<String>()
        while (`in`.peek() == JsonToken.NAME) {
            when (`in`.nextName()) {
                LATIN_CITY_TAG_NAME -> city.name = `in`.nextString()
                LATIN_COUNTRY_TAG_NAME -> city.country = `in`.nextString()
                LOCATION_TAG_NAME -> {
                    `in`.beginObject()
                    while (`in`.peek() == JsonToken.NAME) {
                        when (`in`.nextName()) {
                            LONGITUDE_TAG_NAME -> city.longitude = `in`.nextDouble()
                            LATITUDE_TAG_NAME -> city.latitude = `in`.nextDouble()
                        }
                    }
                    `in`.endObject()
                }
                IATA_TAG_NAME -> {
                    `in`.beginArray()
                    while (`in`.peek() == JsonToken.STRING) {
                        iatas.add(`in`.nextString())
                    }
                    `in`.endArray()
                }
                else -> `in`.skipValue()
            }
        }
        `in`.endObject()
        iatas.forEach {
            result.add(City().apply {
                latitude = city.latitude
                longitude = city.longitude
                country = city.country
                name = city.name
                iata = it
            })
        }
    }

    companion object {
        const val CITIES_ROOT_ELEMENT = "cities"
        const val LATIN_CITY_TAG_NAME = "latinCity"
        const val LATIN_COUNTRY_TAG_NAME = "latinCountry"
        const val LOCATION_TAG_NAME = "location"
        const val LONGITUDE_TAG_NAME = "lon"
        const val LATITUDE_TAG_NAME = "lat"
        const val IATA_TAG_NAME = "iata"
    }
}