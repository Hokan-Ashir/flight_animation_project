package ru.hokan.flight_animation_project.data.providers.yansen.hotellook

import dagger.Module
import dagger.Provides
import ru.hokan.flight_animation_project.data.providers.yansen.hotellook.serialization.CustomGsonConverterFactory
import javax.inject.Singleton

@Module
class YansenHotelLookModule {

    @Provides
    @Singleton
    fun createAPIInstance(): APIInterface {
        val baseUrl = "https://yasen.hotellook.com"

        val retrofit = retrofit2.Retrofit.Builder()
            // that JSON to List<City> conversion should be handled via
            // custom TypeAdapter<City> or TypeAdapter<List<City>> (see lines below)
            // but while registering custom type adapter for List<City>,
            // default gson converters factories inside retrofit process call,
            // while searching for candidate TypToken, endpoint Call<List<City>>
            // finding default CollectionTypeAdapter for lists (JSON arrays) and DO NOT
            // search for more appropriate candidate - TypeAdapter for List<City>
            // which leads to inability to convert initial JSON to List<City>,
            // therefore whole ResponseBody -> JsonReader -> List<City>
            // chain shall be performed via CustomGsonConvertersFactory and CustomResponseBodyConverter
            .addConverterFactory(CustomGsonConverterFactory())
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
//                .registerTypeAdapter(object : TypeToken<List<City>>(){}.type, cityTypeAdapter)
//                .registerTypeAdapter(object : TypeToken<List<Any>>(){}.type, cityTypeAdapter)
//                .create()))
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(APIInterface::class.java)
    }

    @Provides
    @Singleton
    fun createDataProvider(apiInterface: APIInterface): DataProviderImpl {
        return DataProviderImpl(apiInterface)
    }
}