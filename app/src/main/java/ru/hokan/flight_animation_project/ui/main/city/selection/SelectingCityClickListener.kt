package ru.hokan.flight_animation_project.ui.main.city.selection

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.example.flight_animation_project.R
import ru.hokan.flight_animation_project.data.dto.City
import ru.hokan.flight_animation_project.ui.main.city.selection.recycleview.OnItemClickListener
import ru.hokan.flight_animation_project.ui.main.flight.path.selection.FlightPathSelectionFragment
import ru.hokan.flight_animation_project.ui.main.flight.path.selection.FlightPathSelectionViewModel

class SelectingCityClickListener(
    private val activity: FragmentActivity,
    private val fragmentManager: FragmentManager?,
    private val cityType: CityType

) : OnItemClickListener {
    override fun onItemClicked(view : View, city: City) {
        val flightPathSelectionFragment =
            FlightPathSelectionFragment()
        val model = ViewModelProviders.of(activity).get(FlightPathSelectionViewModel::class.java)
        when (cityType) {
            CityType.ARRIVAL -> model.arrivalCity.value = city
            CityType.DEPARTURE -> model.departureCity.value = city
        }
        val inputMethodManager =
            getSystemService(activity.applicationContext, InputMethodManager::class.java)
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
        fragmentManager!!.beginTransaction()
            .replace(R.id.flight_path_selection_activity, flightPathSelectionFragment)
            .commit()
    }
}