package ru.hokan.flight_animation_project.data.dto

// making all fields nullable is highly unlikely scenario, but to make some object,
// iteratively populating its fields during deserialization process, that will do
class City {
    var latitude : Double? = null
    var longitude : Double? = null
    var name : String? = null
    var country : String? = null
    // cause 'yansen.hotellook' providers data is somewhat incomplete
    // you MAY face same IATA names for _actually_ different airport
    // like LED for Pulkovo-1 and LED for Pulkovo-2
    var iata : String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as City

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (name != other.name) return false
        if (country != other.country) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude?.hashCode() ?: 0
        result = 31 * result + (longitude?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        return result
    }


}