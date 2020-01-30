package ru.hokan.flight_animation_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flight_animation_project.R
import ru.hokan.flight_animation_project.ui.main.flight.path.selection.FlightPathSelectionFragment

class FlightPathSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_path_selection_activity)

        savedInstanceState?:let {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.flight_path_selection_activity,
                    FlightPathSelectionFragment()
                )
                .commitNow()
        }
    }

//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        supportFragmentManager.putFragment()
//    }
}
