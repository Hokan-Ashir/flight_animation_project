package ru.hokan.flight_animation_project.ui.main.city.selection.recycleview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.city_layout.view.*
import ru.hokan.flight_animation_project.data.dto.City

class CitiesViewAdapter(
    private val myDataset: Array<City>?,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CitiesViewAdapter.CitiesViewHolder>() {

    class CitiesViewHolder(private val cityView: CityView) : RecyclerView.ViewHolder(cityView) {

        fun bind(city: City, clickListener: OnItemClickListener) {
            cityView.city_name.text = city.name
            cityView.country_name.text = city.country
            cityView.iata_name.text = city.iata

            itemView.setOnClickListener {
                clickListener.onItemClicked(it, city)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesViewHolder {
        val cityView =
            CityView(parent.context)
        return CitiesViewHolder(
            cityView
        )
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val value = myDataset?.get(position)
        holder.bind(value!!, itemClickListener)
    }

    override fun getItemCount() = myDataset!!.size
}

interface OnItemClickListener {
    fun onItemClicked(view: View, city: City)
}