package ru.hokan.flight_animation_project.ui.main.flight.process

import android.graphics.Path
import com.google.android.gms.maps.model.LatLng

 class PathsHolder(val transitionPath : Path,
                       val planePathCoordinates : List<LatLng>,
                       val rotationPath : FloatArray)