package ru.hokan.flight_animation_project.ui.main.city.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flight_animation_project.R
import kotlinx.android.synthetic.main.city_selection_fragment.*
import ru.hokan.flight_animation_project.data.providers.DataProvider
import ru.hokan.flight_animation_project.di.FlightAnimationApplication
import ru.hokan.flight_animation_project.ui.main.city.selection.recycleview.CitiesViewAdapter
import javax.inject.Inject

class CitySelectionFragment : Fragment() {

    @Inject
    lateinit var dataProvider: DataProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.city_selection_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity!!.application as FlightAnimationApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model = ViewModelProviders.of(activity!!).get(CitySelectionFragmentModel::class.java)
        val selectingCityClickListener = SelectingCityClickListener(activity!!, fragmentManager, model.cityType.value!!)
        createCitiesRecycleView(selectingCityClickListener)
        val textWatcher =
            CityNameSelectionTextWatcher(cities_list, dataProvider, selectingCityClickListener)
        city_name_selection.addTextChangedListener(textWatcher)
    }

    private fun createCitiesRecycleView(selectingCityClickListener: SelectingCityClickListener) {
        cities_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        cities_list.adapter = CitiesViewAdapter(emptyArray(), selectingCityClickListener)
    }
}