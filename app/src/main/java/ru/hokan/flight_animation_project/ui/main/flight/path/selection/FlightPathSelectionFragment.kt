package ru.hokan.flight_animation_project.ui.main.flight.path.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flight_animation_project.R
import kotlinx.android.synthetic.main.flight_selection_fragment.*
import ru.hokan.flight_animation_project.data.dto.City
import ru.hokan.flight_animation_project.ui.main.city.selection.CitySelectionFragment
import ru.hokan.flight_animation_project.ui.main.city.selection.CitySelectionFragmentModel
import ru.hokan.flight_animation_project.ui.main.city.selection.CityType
import ru.hokan.flight_animation_project.ui.main.flight.process.FlightProcessFragment

class FlightPathSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.flight_selection_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        submit_flight_path.isEnabled = false
        val model = ViewModelProviders.of(activity!!).get(FlightPathSelectionViewModel::class.java)
        model.departureCity.observe(this, Observer {
            departure_city.text = createSelectedCityTextValue(it)
            setSubmitButtonState(model)
        })
        model.arrivalCity.observe(this, Observer<City> {
            arrival_city.text = createSelectedCityTextValue(it)
            setSubmitButtonState(model)
        })

        attachCitySelectionClickListener(arrival_city, CityType.ARRIVAL)
        attachCitySelectionClickListener(departure_city, CityType.DEPARTURE)
        setSubmitButtonListener()
    }

    private fun createSelectedCityTextValue(it: City) =
        it.name + resources.getString(R.string.iata_text_field_prefix) +
                it.iata + resources.getString(R.string.iata_text_field_postfix)

    private fun setSubmitButtonState(model: FlightPathSelectionViewModel) {
        model.departureCity.value?.let {
            model.arrivalCity.value?.let {
                submit_flight_path.isEnabled = (model.departureCity.value != model.arrivalCity.value)
            }
        }
    }

    private fun attachCitySelectionClickListener(textView: TextView, cityType: CityType) {
        textView.setOnClickListener {
            val model =
                ViewModelProviders.of(activity!!).get(CitySelectionFragmentModel::class.java)
            model.cityType.value = cityType
            val citySelectionFragment = CitySelectionFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.flight_path_selection_activity, citySelectionFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setSubmitButtonListener() {
        submit_flight_path.setOnClickListener {
            val flightProcessFragment = FlightProcessFragment()
            fragmentManager!!.beginTransaction()
                .replace(R.id.flight_path_selection_activity, flightProcessFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
