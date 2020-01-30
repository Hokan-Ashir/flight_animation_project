package ru.hokan.flight_animation_project.ui.main.city.selection

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import ru.hokan.flight_animation_project.data.providers.DataProvider
import ru.hokan.flight_animation_project.ui.main.city.selection.recycleview.CitiesViewAdapter

class CityNameSelectionTextWatcher(
    private val cities_list: RecyclerView,
    private val dataProvider: DataProvider,
    private val clickListener: SelectingCityClickListener
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        dataProvider.getCities(s.toString()) {
            // should filter cities with term IN NAME, not in each and every JSON field values
            val filteredCityNames =
                it!!.filter { it.name!!.contains(s!!, true) }.toTypedArray()
            cities_list.swapAdapter(
                CitiesViewAdapter(
                    filteredCityNames,
                    clickListener
                ), false
            )
        }
    }
}